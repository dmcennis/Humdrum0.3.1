/*
 * Created on Sep 12, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.interpretation;

import java.util.HashSet;
import java.util.Iterator;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.Model;
import edu.osu.csml.humdrum.Note;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.KernException;
import edu.osu.csml.humdrum.kern.KernInterpretationFactory;
import edu.osu.csml.humdrum.kern.KernWarning;
import edu.osu.csml.humdrum.kern.note.EndOfNote;
import edu.osu.csml.humdrum.kern.note.Multi;
import edu.osu.csml.humdrum.kern.note.Pitch;
import edu.osu.csml.humdrum.kern.note.Single;
import edu.osu.csml.humdrum.kern.note.Tie;
import edu.osu.csml.humdrum.EndOfToken;
import edu.osu.csml.humdrum.SpineEOF;

/**
 * keeps tracks of ties accross note boundaries to see that they adhere to Kern
 * syntax
 * @author Daniel McEnnis
 * 
 */
public class TieCheck implements Interpretation {
	Controller master;

	static boolean st = false;
	
	static synchronized public void init(){
		if(!st){
			KernInterpretationFactory.registerGlobal(new TieCheck(new EventController()));
			st=true;
		}
	}
	// persistant values
	/**
	 * pitch of the current tied note. If there are no outstanding ties, this
	 * value is null
	 */
	private HashSet lastPitch;
	/**
	 * gives the state of the last tie.
	 */
	private Tie lastTie;

	// Only have meaning in intermediary states
	volatile private Pitch tmp;
	volatile private HashSet currentPitch;
	volatile private Tie currentTie;
	volatile private boolean skip = false;

	/**
	 * If every tie present in this object is equal to a tie in the other
	 * object and vice versa, the two ties are equal, else false
	 * 
	 */
	public boolean equals(Object obj) {
		if (obj instanceof TieCheck) {
			TieCheck tmp = (TieCheck) obj;
			if (this.hashSetIsEqual(tmp.lastPitch, this.lastPitch)
				&& (tmp.lastTie.equals(this.lastTie))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Compatibility problems only if the other spine includes ties. If every
	 * tie in this spine equals the ties in the other spine, then still
	 * compatible
	 */
	public boolean isCompatible(Interpretation k) {
		if (k instanceof TieCheck) {
			TieCheck tmp = (TieCheck) k;
			for(Iterator it = tmp.lastPitch.iterator();it.hasNext();){
				if(!this.lastPitch.contains(it.next())){
					return false;
				}
			}
			for(Iterator it = this.lastPitch.iterator();it.hasNext();){
				if(!tmp.lastPitch.contains(it.next())){
					return false;
				}
			}
			if (tmp.lastTie.equals(this.lastTie)) {
				return true;
			}
			return false;
		}
		return true;
	}

	/**
	 * Create a new TieCheck object, registering with the controller as well.
	 * 
	 * @param master
	 */
	public TieCheck(Controller master) {
		this.master = master;
		lastPitch = new HashSet();
		currentPitch = new HashSet();
		lastTie = new Tie(master);
		currentTie = new Tie(master);
		master.register(Tie.class, this);
		master.register(Pitch.class, this);
		master.register(EndOfNote.class, this);
		master.register(EndOfToken.class, this);
		master.register(SpineEOF.class, this);
		master.register(Note.class, this);
	}

	/**
	 * Since TieCheck has requirements for consecutive tokens, many more data
	 * objects must be checked then otherwise would be needed.
	 * 
	 */
	public void fire(Class type, Object trigger)
		throws KernException, KernWarning {
		if (type == Note.class) {
			checkNote((Note)trigger);
		} else if (type == Tie.class) {
			handleTie((Tie) trigger);
		} else if (type == Pitch.class) {
			tmp = (Pitch) trigger;
		} else if (type == EndOfNote.class) {
			validateNote();
		} else if (type == EndOfToken.class) {
			validateToken();
		} else if (type == SpineEOF.class) {
			cleanup();
		} else {
			throw new KernException(
				"BUG: Should not be registered for "
					+ trigger.getClass().getName(),
				master);
		}
	}
	/**
	 * If its a note event, we don't need to listen any more if its not a note
	 * permitted to have ties.
	 * 
	 * @param trigger
	 */
	protected void checkNote(Model trigger) {
		if (!((trigger instanceof Single) || (trigger instanceof Multi))) {
			skip = true;
		}
	}

	/**
	 * Validate that the incoming tie value is valid in the current context,
	 * then update the context
	 * 
	 * @param input
	 * @throws KernException
	 * @throws KernWarning
	 */
	protected void handleTie(Tie input) throws KernException, KernWarning {
		if (currentTie.isPresent(Tie.NONE)) {
			currentTie = input;
		} else if (!currentTie.equals(input)) {
			throw new KernException(
				"Multiple types of tie - "
					+ currentTie.toString()
					+ " and "
					+ input.toString(),
				master);
		} else {
			throw new KernException(
				"BUG: Unanticipated else clause in Tie in TieCheck",
				master);
		}
	}
	/**
	 * THe spine is closing down - check to see of any ties are still dangling.
	 * 
	 * @throws KernException
	 */
	protected void cleanup() throws KernException {
		Tie tmp = new Tie(master);
		if (!tmp.canFollow(lastTie)) {
			throw new KernException("Tie not closed", master);
		}
	}

	/**
	 * If we are dealing with a note, check to see if the note's tie (or lack
	 * therof) is legal.
	 * 
	 * @throws KernException
	 */
	protected void validateToken() throws KernException {
		//make sure this token comes from a note
		if (!skip) {
			// make sure the token has been 'fixed'
			if ((tmp != null) && (!currentTie.isPresent(Tie.NONE))) {
				currentPitch.add(tmp);
				tmp = null;
			}

			if (currentTie == null) {
				currentTie = new Tie(master);
			} else if (currentTie.canFollow(lastTie)) {
				if (!lastTie.isPresent(Tie.NONE)
					&& (!hashSetIsEqual(lastPitch, currentPitch))) {

					throw new KernException(
						"Tied pitches must stay the same accross notes",
						master);
				} else if (
					!lastTie.isPresent(Tie.NONE)
						&& (hashSetIsEqual(lastPitch, currentPitch))) {
					lastPitch = currentPitch;
					lastTie = currentTie;
					currentTie = new Tie(master);
					currentPitch = new HashSet();
				} else if (
					lastTie.isPresent(Tie.NONE)
						&& !currentTie.isPresent(Tie.NONE)
						&& currentPitch.isEmpty()) {
					throw new KernException("Ties require a pitch", master);
				} else if (
					lastTie.isPresent(Tie.NONE)
						&& !currentTie.isPresent(Tie.NONE)) {
					lastTie = currentTie;
					currentTie = new Tie(master);
					lastPitch = currentPitch;
					currentPitch = new HashSet();
				}
			} else {
				if (currentTie.isPresent(Tie.CONTINUE)) {
					throw new KernException(
						"Invalid Tie- Can not continue a tie ('_') that was never opened ('[')",
						master);
				} else if (currentTie.isPresent(Tie.END)) {
					throw new KernException(
						"Invalid Tie- Can not end a tie (']') that was never opened",
						master);
				} else if (currentTie.isPresent(Tie.OPEN)) {
					throw new KernException(
						"Invalid Tie- Can not nest ties",
						master);
				} else {
					throw new KernException(
						"Invalid Tie- Ties must be only consecutive notes",
						master);
				}
			}
		} else {
			skip = false;
		}
	}

	/**
	 * update the context with the new note, noting an invalid tie if
	 * accompanying info is not also present in the note
	 * 
	 * @throws KernException
	 */
	protected void validateNote() throws KernException {
		if (!currentTie.isPresent(Tie.NONE)) {
			if (tmp == null) {
				throw new KernException("Ties require a pitch", master);
			} else {
				currentPitch.add(tmp);
				tmp = null;
			}
		}
	}

	/**
	 * Crete a deep copy of the state. Ignore volatile members, they are
	 * irrelevant to state.
	 * 
	 * @see edu.osu.csml.humdrum.View#clone(edu.osu.csml.humdrum.Controller)
	 */
	public Interpretation clone(Controller master) {
		TieCheck tmp = new TieCheck(master);
		tmp.lastTie = this.lastTie;
		tmp.lastPitch = this.lastPitch;
		return tmp;
	}

	/**
	 * This is needed to circumvent shortcomings in HashSet.contains and
	 * HashSet.
	 */
	public boolean hashSetIsEqual(HashSet a, HashSet b) {
		for (Iterator i = a.iterator(); i.hasNext();) {
			if (!hashSetContains(b, i.next())) {
				return false;
			}
		}
		for (Iterator i = b.iterator(); i.hasNext();) {
			if (!hashSetContains(a, i.next())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This is needed to circumvent shortcomings in HashSet.contains and
	 * HashSet.
	 */
	
	public boolean hashSetContains(HashSet a, Object tmp) {
		for (Iterator i = a.iterator(); i.hasNext();) {
			if (tmp.equals(i.next())) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(Controller master) {
		return new TieCheck(master);
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(java.lang.String, edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(String token, Controller master)
			throws HumdrumException, HumdrumWarning {
		return new TieCheck(master);
	}
}
