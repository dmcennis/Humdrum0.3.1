/*
 * Created on Sep 12, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.interpretation;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.GlobalCheckFactory;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.Model;
import edu.osu.csml.humdrum.SpineEOF;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.KernException;
import edu.osu.csml.humdrum.kern.KernInterpretationFactory;
import edu.osu.csml.humdrum.kern.KernWarning;
import edu.osu.csml.humdrum.kern.note.SlurPhraseBeam;

/**
 * Verify that all phrases, slurs, and beams follow proper kern syntax
 * 
 * @author Daniel McEnnis
 * 
 */
public class PhraseLikeCheck implements Interpretation {
	static boolean st = false;
	
	static synchronized public void init(){
		if(!st){
			KernInterpretationFactory.registerGlobal(new PhraseLikeCheck(new EventController()));
			st=true;
		}
	}

	private Controller master;

	/**
	 * keep track of how many of each type of phraseLike object.
	 */
	private int[] count = new int[] { 0, 0, 0 };

	/**
	 * verify if this object is currently elided - and awaiting an elided close
	 */
	private boolean[] elided = new boolean[] { false, false, false };

	/**
	 * Names used in error messages
	 */
	private static final String[] names = new String[] { "Phrase", "Slur",
			"Beam" };

	/**
	 * whether or not this token is permitted to be elided - allows more code
	 * reuse
	 */
	private static final boolean[] hasElided = new boolean[] { true, true,
			false };

	/**
	 * create a new controller and register for events
	 */
	public PhraseLikeCheck(Controller master) {
		this.master = master;
		master.register(SpineEOF.class, this);
		master.register(SlurPhraseBeam.class, this);
	}

	/**
	 * Compatible with non-Phrases. Compatible with phrases only if equal.
	 */
	public boolean isCompatible(Interpretation k) {
		if (k instanceof PhraseLikeCheck) {
			for (int i = 0; i < 3; ++i) {
				if (((PhraseLikeCheck) k).elided[i] != this.elided[i]) {
					return false;
				}
				if (((PhraseLikeCheck) k).count[i] != this.count[i]) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * called exclusively by the controller master when data of the type
	 * SpineEOF or SlurPhraseBeam is parsed.
	 */
	public void fire(Class type, Object trigger) throws KernException,
			KernWarning {
		if (type == SpineEOF.class) {
			doEOF();
		} else if (type == SlurPhraseBeam.class) {
			handleType((SlurPhraseBeam) trigger);
		} else {
			throw new KernException("BUG: recieved event "
					+ trigger.getClass().getName()
					+ " but should not be registered for it", master);
		}
	}

	/**
	 * Create a deep copy of the object
	 * 
	 */
	public Interpretation clone(Controller master) {
		PhraseLikeCheck tmp = new PhraseLikeCheck(master);
		for (int i = 0; i < this.count.length; ++i) {
			tmp.count[i] = this.count[i];
			tmp.elided[i] = this.elided[i];
		}
		return tmp;
	}

	/**
	 * Spine is closing down. Look for unclosed slurs,etc.
	 * 
	 */
	private void doEOF() {
		for (int i = 0; i < count.length; ++i) {
			if (elided[i] || (count[i] > 0)) {
				doError("An unclosed " + names[i]);
			}
		}
	}

	/**
	 * Do checks on all possible combinations of previous and current state.
	 * 
	 * @param trigger
	 *            provides change to current state
	 * @throws KernException
	 */
	private void handleType(SlurPhraseBeam trigger) throws KernException {
		for (int i = 0; i < names.length; ++i) {
			if (elided[i]) {
				if ((trigger.get(SlurPhraseBeam.typeOfEntry[2][i]) > 0)
						|| (trigger.get(SlurPhraseBeam.typeOfEntry[0][i]) > 0)) {
					throw new KernException("Can not nest with elided "
							+ SlurPhraseBeam.name[i], master);
				} else if ((trigger.get(SlurPhraseBeam.typeOfEntry[3][i]) == 0)
						&& (trigger.get(SlurPhraseBeam.typeOfEntry[1][i]) > 0)) {
					throw new KernException(
							"Can not close elided " + SlurPhraseBeam.name[i]
									+ " with non-elided close", master);
				} else if (trigger.get(SlurPhraseBeam.typeOfEntry[3][i]) == 1) {
					elided[i] = false;
				}
			} else {
				if (SlurPhraseBeam.typeOfEntry[3].length > i) {
					if ((trigger.get(SlurPhraseBeam.typeOfEntry[2][i]) == 0)
							&& (trigger.get(SlurPhraseBeam.typeOfEntry[3][i]) > 0)) {
						throw new KernException("Closing an unopened elided "
								+ SlurPhraseBeam.name[i], master);
					} else if ((trigger.get(SlurPhraseBeam.typeOfEntry[2][i]) > 0)
							&& (trigger.get(SlurPhraseBeam.typeOfEntry[3][i]) == 0)
							&& (trigger.get(SlurPhraseBeam.typeOfEntry[1][i]) > 0)) {
						throw new KernException("Can not close an elided "
								+ SlurPhraseBeam.name[i]
								+ " with a non-elided close", master);
					} else if ((trigger.get(SlurPhraseBeam.typeOfEntry[2][i]) > 0)
							&& (trigger.get(SlurPhraseBeam.typeOfEntry[3][i]) == 0)) {
						elided[i] = true;
					}
				}
			} // else elided[i]==false
			if (count[i] + trigger.get(SlurPhraseBeam.typeOfEntry[0][i])
					- trigger.get(SlurPhraseBeam.typeOfEntry[1][i]) < 0) {
				throw new KernException("Closing an unopened "
						+ SlurPhraseBeam.name[i], master);
			} else {
				count[i] += trigger.get(SlurPhraseBeam.typeOfEntry[0][i])
						- trigger.get(SlurPhraseBeam.typeOfEntry[1][i]);
			}
		} // for each type of slur/phrase/beam
	}

	/**
	 * Since this is handling 3 different data objects, use custom error routine
	 * to prepare an appropriate error message
	 * 
	 * @param message
	 */
	private void doError(String message) {
		try {
			throw new KernException(message, master);
		} catch (KernException e) {
			try {
				master.getErrStream().write(e.toString());
			} catch (java.io.IOException e1) {
				;
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(Controller master) {
		return new PhraseLikeCheck(master);
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(java.lang.String, edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(String token, Controller master)
			throws HumdrumException, HumdrumWarning {
		return new PhraseLikeCheck(master);
	}

}
