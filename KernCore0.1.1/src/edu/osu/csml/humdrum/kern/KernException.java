/*
 * Created on Jun 8, 2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package edu.osu.csml.humdrum.kern;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.HumdrumException;

/**
 * Same as a humdrum exception, but specific to a Kern syntax violation. Should
 * be used if the entity is syntacially correct for humdrum, but not for the
 * kern.
 * @author Daniel McEnnis
 * 
 */
public class KernException extends HumdrumException {

	/**
	 * Create a new kern exception specifying the reason
	 * @param reason
	 * @param master
	 */
	public KernException(String reason, Controller master) {
		super(reason, master);
	}

	/**
	 * Display the error in human readable form
	 */
	public String toString() {
		return "Kern Error: "
			+ explanation
			+ " at line "
			+ master.getLineNumber()
			+ " in spine "
			+ master.getSpineNumber()
			+ System.getProperty("line.separator");
	}
}
