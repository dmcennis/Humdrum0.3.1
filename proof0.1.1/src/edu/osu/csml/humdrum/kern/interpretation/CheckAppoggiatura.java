/*
 * Created on Sep 30, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.interpretation;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.Model;
import edu.osu.csml.humdrum.SpineEOF;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.KernException;
import edu.osu.csml.humdrum.kern.KernInterpretationFactory;
import edu.osu.csml.humdrum.kern.KernWarning;
import edu.osu.csml.humdrum.kern.note.Duration;

/**
 * Verifies that appoggiaturas are used correctly
 * @author Daniel McEnnis
 * 
 */
public class CheckAppoggiatura implements Interpretation {

	static boolean started = false;
	
	static synchronized public void init(){
		if(!started){
			KernInterpretationFactory.registerGlobal(new CheckAppoggiatura(new EventController()));
			started=true;
		}
	}
	private boolean hasP = false;
	private Controller master;
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if ((obj instanceof CheckAppoggiatura)
			&& (((CheckAppoggiatura) obj).hasP == this.hasP)) {
			return true;
		}
		return false;
	}

	/**
	 * creates a new object and registers for Duration and SpineEOF with master
	 * 
	 * @param master
	 */
	public CheckAppoggiatura(Controller master) {
		this.master = master;
		master.register(Duration.class, this);
		master.register(SpineEOF.class, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.Interpretation#isCompatible(edu.osu.csml.humdrum.Interpretation)
	 */
	public boolean isCompatible(Interpretation k) {
		if (k instanceof CheckAppoggiatura) {
			if (this.hasP == ((CheckAppoggiatura) k).hasP) {
				return true;
			}
			return false;
		}
		return true;
	}

	/**
	 * Verifies taht appogiaturas are appropriately finished - they can't
	 * legally end a kern spine.
	 */
	public void fire(Class type, Object trigger)
		throws KernException, KernWarning {
		if ((type == SpineEOF.class) && (this.hasP)) {
			throw new KernException(
				"Apoggiatura started, but never finished",
				master);
		} else if (type == Duration.class) {
			handleAppoggiatura((Duration) trigger);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.View#clone(edu.osu.csml.humdrum.Controller)
	 */
	public Interpretation clone(Controller master) {
		CheckAppoggiatura tmp = new CheckAppoggiatura(master);
		tmp.hasP = this.hasP;
		return tmp;
	}

	/**
	 * processes appoggiaturas and verifies that previous state is not in
	 * conflict with new state passed in by trigger
	 * 
	 * @param trigger
	 * @throws KernException
	 */
	protected void handleAppoggiatura(Duration trigger) throws KernException {
		if (trigger.isAppoggiatura()) {
			hasP = true;
		} else if (trigger.isModifiedByAppoggiatura() && !hasP) {
			throw new KernException(
				"Claims to be modified, but no previous notes were appoggiaturas",
				master);
		} else if (trigger.isModifiedByAppoggiatura() && hasP) {
			hasP = false;
		} else if (
			!trigger.isAppoggiatura()
				&& !trigger.isModifiedByAppoggiatura()
				&& hasP) {
			throw new KernException(
				"The note after an apoggiatura must be marked",
				master);
		}
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(Controller master) {
		return new CheckAppoggiatura(master);
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(java.lang.String, edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(String token, Controller master) {
		return new CheckAppoggiatura(master);
	}
}
