/*
 * Created on Mar 3, 2004
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
import edu.osu.csml.humdrum.Line;
import edu.osu.csml.humdrum.Model;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.KernException;
import edu.osu.csml.humdrum.kern.KernInterpretationFactory;
import edu.osu.csml.humdrum.kern.KernWarning;
import edu.osu.csml.humdrum.kern.note.Barline;
import edu.osu.csml.humdrum.kern.note.Single;

/**
 * Global context that ensures that barlines are not in the same line as notes.
 */
public class MixedBarlineNoteCheck implements View {

	static boolean st = false;
	
	static synchronized public void init(){
		if(!st){
			GlobalCheckFactory.register(new MixedBarlineNoteCheck(new EventController()));
			st=true;
		}
	}
	/**
	 * Controller provided by Document
	 */
	private Controller master;

	/**
	 * Has there been a barline during this line
	 */
	private boolean barline = false;

	/**
	 * has there been a note during this line
	 */
	private boolean note = false;

	/**
	 * Create a check at the global level that barlines and notes are not mixed
	 * 
	 * @param master
	 */
	public MixedBarlineNoteCheck(Controller master) {
		this.master = master;
		master.register(Single.class, this);
		master.register(Barline.class, this);
		master.register(Line.class, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.View#fire(java.lang.Class,
	 *      edu.osu.csml.humdrum.Model)
	 */
	public void fire(Class type, Object trigger)
		throws KernException, KernWarning {
		if (type == Single.class) {
			note = true;
			if (note && barline) {
				throw new KernException(
					"Can not have both notes and barlines in the same line",
					master);
			}
		} else if (type == Barline.class) {
			barline = true;
			if (note && barline) {
				throw new KernException(
					"Can not have both notes and barlines in the same line",
					master);
			}
		} else if (type == Line.class) {
			note = false;
			barline = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.View#clone(edu.osu.csml.humdrum.Controller)
	 */
	public View clone(Controller master) {
		MixedBarlineNoteCheck ret = new MixedBarlineNoteCheck(master);
		ret.barline = this.barline;
		ret.note = this.note;
		return ret;
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(Controller master) {
		return new MixedBarlineNoteCheck(master);
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(java.lang.String, edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(String token, Controller master)
			throws HumdrumException, HumdrumWarning {
		return new MixedBarlineNoteCheck(master);
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#isCompatible(edu.osu.csml.humdrum.Interpretation)
	 */
	@Override
	public boolean isCompatible(Interpretation k) {
		// TODO Auto-generated method stub
		return true;
	}

}
