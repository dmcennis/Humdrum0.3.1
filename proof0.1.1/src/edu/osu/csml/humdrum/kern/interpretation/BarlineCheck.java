/*
 * Created on Sep 1, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.interpretation;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.DupLineCheck;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.GlobalCheckFactory;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.Model;
import edu.osu.csml.humdrum.Note;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.KernException;
import edu.osu.csml.humdrum.kern.KernInterpretationFactory;
import edu.osu.csml.humdrum.kern.KernWarning;
import edu.osu.csml.humdrum.kern.note.Barline;

/**
 * Verify that barlines increment barline numbers appropriately and that no
 * notes follows a double barline
 * @author Daniel McEnnis
 * 
 */
public class BarlineCheck implements Interpretation {
	static boolean started = false;
	
	static synchronized public void init(){
		if(!started){
			KernInterpretationFactory.registerGlobal(new BarlineCheck(new EventController()));
			started=true;
		}
	}
	private Controller master;
	/**
	 * reference to last barline seen. Initially none
	 */
	private Barline last;
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof BarlineCheck) {
			BarlineCheck rhs = (BarlineCheck) obj;
			if (this.last.equals(rhs.last)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * create new Barline object and register Barline and Note hooks with
	 * master
	 * 
	 * @param master
	 */
	public BarlineCheck(Controller master) {
		this.master = master;
		last = new Barline(master);
		master.register(Barline.class, this);
		master.register(Note.class, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.Interpretation#isCompatible(edu.osu.csml.humdrum.Interpretation)
	 */
	public boolean isCompatible(Interpretation k) {
		if (k instanceof BarlineCheck) {
			return this.equals(k);
		}
		return true;
	}

	/**
	 * called by master when data of interest is produced by Models. Handle
	 * finding Barline data and finding Note data
	 */
	public void fire(Class type, Object trigger)
		throws KernException, KernWarning {
		if (type == Note.class) {
			if (last.isDouble()) {
				throw new KernException(
					"A double barline should be the last token in a spine",
					master);
			}
		} else if (type == Barline.class) {
			Barline note = (Barline) trigger;
			if (note.isDouble()) {
				last = note;
			} else if (note.isGreater(last)) {
				last = note;
			} else {
				throw new KernException(
					"Measure numbers must be strictly increasing",
					master);
			}
		} else {
			throw new KernException(
				"BUG: Barline Context recieving events it should not be recieving",
				master);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.View#clone(edu.osu.csml.humdrum.Controller)
	 */
	public Interpretation clone(Controller master) {
		BarlineCheck tmp = new BarlineCheck(master);
		tmp.last = (Barline) this.last.clone(master);
		return tmp;
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(Controller master) {
		return new BarlineCheck(master);
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(java.lang.String, edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(String token, Controller master) {
		return new BarlineCheck(master);
	}

}
