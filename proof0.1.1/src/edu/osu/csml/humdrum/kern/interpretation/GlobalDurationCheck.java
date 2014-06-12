/*
 * Created on Mar 1, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.interpretation;

import java.util.Vector;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.GlobalCheckFactory;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.Line;
import edu.osu.csml.humdrum.Model;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.KernException;
import edu.osu.csml.humdrum.kern.KernInterpretationFactory;
import edu.osu.csml.humdrum.kern.KernWarning;

/**
 * Perform cross-spine checks that esure that spines are syncronized in time
 * 
 * @author Daniel McEnnis
 */
public class GlobalDurationCheck implements View {

	static boolean started = false;
	
	static synchronized public void init(){
		if(!started){
			GlobalCheckFactory.register(new GlobalDurationCheck(new EventController()));
			started=true;
		}
	}
	
	/**
	 * Master controller from Document
	 */
	Controller master;
	
	/**
	 * Controller used to isolate DurationSum objects from further events
	 */
	Controller dummy;
	/**
	 * List of DurationSum classes in any given line
	 */
	Vector durationSum = new Vector();

	/**
	 * Initialize the check, registering with Document to recieve the
	 * DurationSum messages from participating spines.
	 * 
	 * @param master
	 *            Document-level controller
	 */
	public GlobalDurationCheck(Controller master) {
		this.master = master;
		master.register(DurationSum.class, this);
		master.register(Line.class, this);
		dummy = new EventController();
	}

	/**
	 * Process DurationSum messages, then perform sanity checks when it
	 * recieves a Line event signifying that all tokens in this line have been
	 * processed
	 */
	public void fire(Class type, Object trigger)
		throws KernException, KernWarning {
		if (type == DurationSum.class) {
			durationSum.add(((DurationSum)trigger).clone(dummy));
		} else if (type == Line.class) {
			verifyDuration();
			durationSum.clear();
		} else {
			throw new KernException(
				"BUG: Shouldn't recieve events of type :" + type.getName(),
				master);
		}

	}

	/**
	 * Verify that spines' durations are syncronized with each other
	 */
	private void verifyDuration() throws KernException {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < durationSum.size(); ++i) {
			DurationSum tmp = (DurationSum) durationSum.get(i);
			if (tmp.getDuration() < min) {
				min = tmp.getDuration();
			}
		}
		for (int i = 0; i < durationSum.size(); ++i) {
			DurationSum tmp = (DurationSum) durationSum.get(i);
			if ((tmp.getDuration() != min) && tmp.hasDuration()) {
				durationSum.clear();
				throw new KernException(
					"2 or more spines have data that is not syncronized (not aligned in time)",
					master);
			}
		}
	}

	/**
	 * This really should't be called, but is included here for completeness
	 */
	public View clone(Controller master) {
		GlobalDurationCheck ret = new GlobalDurationCheck(master);
		ret.durationSum = new Vector();
		for (int i = 0; i < this.durationSum.size(); ++i) {
			ret.durationSum.add(
				((DurationSum) (durationSum.get(i))).clone(master));
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(Controller master) {
		return new GlobalDurationCheck(master);
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(java.lang.String, edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(String token, Controller master) {
		return new GlobalDurationCheck(master);
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
