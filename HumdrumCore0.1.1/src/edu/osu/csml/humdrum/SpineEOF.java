/*
 * Created on Sep 1, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

/**
 * Model created by the humdrum document when it encounters a valid end of
 * spine interpretation. This object is passed as an event, letting the
 * terminated spine perform whatever cleanup is neccessary
 * @author Daniel McEnnis
 * 
 */
public class SpineEOF implements Model {
	private Controller master;

	/**
	 * Basic constructor - nothing fancy needed
	 * 
	 * @param master
	 */
	public SpineEOF(Controller master) {
		this.master = master;
	}

	/**
	 * Provided for completness. Defined in model interface even if there isn't
	 * an obvious use for it.
	 * 
	 * @see edu.osu.csml.humdrum.Model#clone(edu.osu.csml.humdrum.Controller)
	 */
	public Object clone(Controller master) {
		return new SpineEOF(master);
	}

	/**
	 * send death message to this spine
	 * @param token
	 */
	public void init(String token) {
		master.fire(SpineEOF.class, this);
	}

	@Override
	public String output() {
		return "";
	}

}
