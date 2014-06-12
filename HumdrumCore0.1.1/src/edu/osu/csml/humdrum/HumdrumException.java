/*
 * Created on Aug 29, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

/**
 * Represents a severe, unrecoverable error within a humdrum document. Super
 * class for all other humdrum based exceptions
 * @author Daniel McEnnis
 * 
 */
public class HumdrumException extends Exception {
	protected String explanation;
	protected Controller master;
	
	/**
	 * Crete a new Humdrum exception - include a string explaining the error.
	 * @param reason
	 * @param master
	 */
	public HumdrumException(String reason, Controller master) {
		explanation = reason;
		this.master = master;
	}

	/**
	 * Display the explanation of the fatal error to the user
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Humdrum Exception: "
			+ explanation
			+ " at line "
			+ master.getLineNumber()
			+ " in spine "
			+ master.getSpineNumber()
			+ System.getProperty("line.separator");
	}
}
