/*
 * Created on Mar 1, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.interpretation;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.Model;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.KernException;
import edu.osu.csml.humdrum.kern.KernInterpretationFactory;
import edu.osu.csml.humdrum.kern.KernWarning;
import edu.osu.csml.humdrum.kern.note.Duration;
import edu.osu.csml.humdrum.EndOfToken;

/**
 * Keeps a running total of all Durations that have occurred in the spine.
 * Fires an event used by GlobalDurationCheck to verify that all spines are
 * syncronized in time
 * 
 * @author Daniel McEnnis
 *  
 */
public class DurationSum implements Interpretation, Model {

	static boolean started = false;
	
	static int done = init();
	
	static synchronized public int init(){
		if(!started){
			KernInterpretationFactory.registerGlobal(new DurationSum(new EventController()));
			started=true;
		}
		return 1;
	}

	/**
	 * Last Duration included. Syncronization checks is always one duration
	 * behind the current total. This duration must be buffered since the
	 * object will not otherwise be available when it is needed.
	 */
	private Duration d;

	/**
	 * Register if the current token as a duration or not.
	 */
	private boolean hasDuration = false;

	/**
	 * Current total of durations in this spine. Only spines with the lowest
	 * sum may have non-null notes.
	 */
	private double sum;

	/**
	 * Spine level controller
	 */
	private Controller master;

	/**
	 * Create this interpretation attached to spine with controller master.
	 * Also register with the controller for the events needed to keep a
	 * running total of durations.
	 * 
	 * @param master
	 */
	public DurationSum(Controller master) {
		this.master = master;
		sum = 0.0;
		d = null;
		master.register(Duration.class, this);
		master.register(EndOfToken.class, this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.Interpretation#isCompatible(edu.osu.csml.humdrum.Interpretation)
	 */
	public boolean isCompatible(Interpretation k) {
		if (k instanceof DurationSum) {
			return this.equals(k);
		} else {
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.View#clone(edu.osu.csml.humdrum.Controller)
	 */
	public Interpretation clone(Controller master) {
		DurationSum ret = new DurationSum(master);
		ret.sum = this.sum;
		ret.d = this.d;
		ret.hasDuration = this.hasDuration;
		return ret;
	}

	/**
	 * Process all events rquired to properly maintain an accurate running
	 * total of Duration in this spine.
	 */
	public void fire(Class type, Object trigger)
		throws KernException, KernWarning {
		if (type == Duration.class) {
			if (!Double.isNaN(((Duration) trigger).getDuration())) {
				d = (Duration) trigger;
				hasDuration = true;
			}
		} else if (type == EndOfToken.class) {
			master.getParent().fire(DurationSum.class, this);
			if(d!=null){
				sum += d.getDuration();
				d = null;
			}
			hasDuration = false;
		} else {
			throw new KernException(
				"BUG: DurationSum recieving events it should ot be registered for :"
					+ type.getName(),
				master);
		}
	}

	/**
	 * Provide current total of time in this spine
	 * 
	 * @return total time spent in this spine
	 */
	public double getDuration() {
		return sum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof DurationSum) {
			DurationSum tmp = (DurationSum) obj;
			if ((tmp.sum == this.sum)&&(this.hasDuration == tmp.hasDuration)) {
				if ((tmp.d == null) && (this.d == null)) {
					return true;
				}
				if ((tmp.d != null) && tmp.d.equals(this.d)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Does this token contain a non-null note with a meaningful duration
	 * 
	 * @return does this token have a duration object in it.
	 */
	public boolean hasDuration() {
		return hasDuration;
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(Controller master) {
		return new DurationSum(master);
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(java.lang.String, edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(String token, Controller master) {
		return new DurationSum(master);
	}

	@Override
	public String output() {
		return "";
	}

}
