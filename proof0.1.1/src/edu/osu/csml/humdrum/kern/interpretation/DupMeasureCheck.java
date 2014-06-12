/*
 * Created on Feb 28, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.interpretation;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.GlobalCheckFactory;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.Line;
import edu.osu.csml.humdrum.Measure;
import edu.osu.csml.humdrum.Model;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.KernException;
import edu.osu.csml.humdrum.kern.KernInterpretationFactory;
import edu.osu.csml.humdrum.kern.KernWarning;
import edu.osu.csml.humdrum.kern.note.Barline;

/**
 * Checks to see if a measure is identical to one entered before. Useful to
 * catch duplicated measures where the measures are not numbered.
 * 
 * @author Daniel McEnnis
 */
public class DupMeasureCheck implements View {
	
	static boolean started = false;
	
	static synchronized public void init(){
		if(!started){
			GlobalCheckFactory.register(new DupMeasureCheck(new EventController()));
			started=true;
		}
	}
	/**
	 * Does this line contain a barline
	 */
	private boolean barlineDetected = false;

	/**
	 * Currently not in a measure
	 */
	public static final int NONE = 0;

	/**
	 * In the middle of a measure
	 */
	public static final int OPEN = 1;

	/**
	 * In the last line of the measure
	 */
	public static final int SHUT = 2;

	/**
	 * state of the current measure
	 */
	private int state = NONE;

	/**
	 * holds reference to the previous measure to be compared against - if it
	 * exists
	 */
	private Measure previous = null;

	/**
	 * holds reference to the current measure
	 */
	private Measure current = null;

	/**
	 * handle for printing correct line numbers
	 */
	private Controller master;

	/**
	 * used to enforce singleton standards
	 */
	private static DupMeasureCheck __internal = null;

	/**
	 * protected constructor used internally to start checks
	 * 
	 * @param master
	 */
	public DupMeasureCheck(Controller master) {
		this.master = master;
		master.register(Barline.class, this);
		master.register(Line.class, this);
	}


	/**
	 * Splits incoming lines into measures, loading the internal Measure
	 * objects. 
	 * <p>Algorithm for parsing Measures is:
	 * <ul>
	 * <li>all data immediately after a line containing a barline
	 * <li>the next line containing a barline
	 * </ul>
	 */
	public void fire(Class type, Object trigger)
		throws KernException, KernWarning {
		if (type == Barline.class) {
			barlineDetected = true;
		} else if (type == Line.class) {
			if (barlineDetected) {
				if (state == NONE) {
					state = OPEN;
				} else if (state == OPEN) {
					state = SHUT;
				}
				barlineDetected = false;
			}
			if ((state == OPEN) && (current == null)) {
				current = new Measure();
			} else if ((state == OPEN) && (current != null)) {
				current.addLine((Line) trigger);
			} else if (state == SHUT) {
				if (current == null) {
					current = new Measure();
				}
				current.addLine((Line) trigger);
				if (current.equals(previous)) {
					throw new KernWarning(
						"Duplicate measures detected",
						master);
				}
				previous = current;
				current = new Measure();
				state = OPEN;
			}
		}
	}

	/**
	 * Only one DupLineCheck should exist at any given time, but give the
	 * opportunity for trouble.
	 * 
	 * @return reference to deep copy
	 */
	public View clone(Controller master) {
		DupMeasureCheck tmp = new DupMeasureCheck(master);
		tmp.barlineDetected = this.barlineDetected;
		tmp.current = this.current;
		tmp.master = master;
		tmp.previous = this.previous;
		tmp.state = this.state;
		return tmp;
	}


	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(Controller master) {
		return new DupMeasureCheck(master);
	}


	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(java.lang.String, edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(String token, Controller master) {
		return new DupMeasureCheck(master);
	}


	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#isCompatible(edu.osu.csml.humdrum.Interpretation)
	 */
	@Override
	public boolean isCompatible(Interpretation k) {
		return true;
	}

}
