/*
 * Created on Aug 29, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

/**
 * Reports (potential) problems with humdrum files that aren't fatal
 * 
 * @author Daniel McEnnis
 *  
 */
public class HumdrumWarning extends Exception {
	/**
	 *  
	 */
	public static void setWarningLevel(int val) {
		warningLevel = val;
	}
	/**
	 *  
	 */
	protected static int warningLevel=0;
	/**
	 * detailed explanation of the problem
	 */
	protected String explanation;
	/**
	 * name of the controller from the context where the problem was detected.
	 */
	protected Controller master;
	/**
	 * create a new warning
	 * 
	 * @param reason
	 *            reason this warning was created
	 * @param master
	 *            context needed for informative error messages
	 */
	public HumdrumWarning(String reason, Controller master) {
		explanation = reason;
		this.master = master;
	}

	/**
	 * Human readable description of the problem reported
	 */
	public String toString() {
		if (warningLevel > 0) {
			return "";
		} else {
			return "Humdrum Warning: "
				+ explanation
				+ " at line "
				+ master.getLineNumber()
				+ " in spine "
				+ master.getSpineNumber()
				+ System.getProperty("line.separator");
		}
	}
}
