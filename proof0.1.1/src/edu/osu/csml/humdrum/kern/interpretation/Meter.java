/*
 * Created on Sep 1, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.interpretation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.Model;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.KernException;
import edu.osu.csml.humdrum.kern.KernInterpretationFactory;
import edu.osu.csml.humdrum.kern.KernWarning;
import edu.osu.csml.humdrum.kern.note.Barline;
import edu.osu.csml.humdrum.kern.note.Duration;


/**
 * Validate that the sum of the durations in the measure matches the duration
 * of the meter
 *  
 * @author Daniel McEnnis
 * 
 */
public class Meter implements Interpretation {
	static boolean st = false;
	
	static synchronized public void init(){
		if(!st){
			KernInterpretationFactory.registerSpecific("\\*M\\d+/\\d+",new Meter(new EventController()));
			st=true;
		}
	}

	private Controller master;
	private double maxDuration = -1.0;
	private double curDuration = 0.0;
	private boolean started = false;
	/**
	 * Is equal iff the current meter and current duration sum match
	 */
	public boolean equals(Object obj) {
		if ((obj instanceof Meter)
			&& (((Meter) obj).maxDuration == this.maxDuration)
			&& (((Meter) obj).curDuration == this.curDuration)
			&& (((Meter) obj).started == this.started)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Needed for this class to be subclassed - NOT for general use
	 * 
	 * @param master
	 */
	protected Meter(Controller master) {
		this.master = master;
		master.register(Duration.class, this);
		master.register(Barline.class, this);
	}

	/**
	 * takes meter string from interpretation factory and parse it into a meter
	 * 
	 * @param token
	 * @param master
	 * @throws KernException
	 */
	public Meter(String token, Controller master) throws KernException {
		this.master = master;
		Pattern met = Pattern.compile("\\*[mM](\\d+)/(\\d+)");
		Matcher m = met.matcher(token);
		if (m.matches()) {
			double lhs;
			double rhs;
			if (Integer.parseInt(m.group(1)) == 0) {
				throw new KernException(
					"0 is not a valid number of beats for a measure",
					master);
			} else {
				lhs = (double) Integer.parseInt(m.group(1));
			}
			if (Integer.parseInt(m.group(2)) == 0) {
				rhs = 1.0;
			} else {
				rhs = 1 / ((double) Integer.parseInt(m.group(2)));
			}
			maxDuration = lhs * rhs;
		}
		master.register(Duration.class, this);
		master.register(Barline.class, this);

	}

	/**
	 * If not a meter, compatible. If a meter, but equal, then compatible. Else
	 * incompatible
	 *  
	 */
	public boolean isCompatible(Interpretation k) {
		if (k instanceof Meter) {
			if (this.equals(k)) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * process duration and barline events
	 */
	public void fire(Class type, Object trigger)
		throws KernException, KernWarning {
		if ((type == Duration.class)
			&& started
			&& !((Duration) trigger).isGroupetto()) {
			curDuration += ((Duration) trigger).getDuration();
			if (curDuration > maxDuration) {
				throw new KernWarning(
					"Number of beats in measure exceeds meter signature",
					master);
			}
		} else if ((type == Barline.class) && !started) {
			started = true;
		} else if (
			(type == Barline.class) && !((Barline) trigger).isDouble()) {
			if (Math.abs(curDuration - maxDuration) > 0.0000001) {
				throw new KernWarning(
					"Number of beats in the measure do not match the meter",
					master);
			}
			curDuration = 0.0;
		}
	}

	/**
	 * create a deep copy of the measure interpretation
	 */
	public Interpretation clone(Controller master) {
		Meter tmp = new Meter(master);
		tmp.maxDuration = this.maxDuration;
		tmp.curDuration = this.curDuration;
		tmp.started = this.started;
		return tmp;
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(Controller master) {
		return new Meter(master);
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(java.lang.String, edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(String token, Controller master) throws HumdrumException, HumdrumWarning{
		return new Meter(token,master);
	}

}
