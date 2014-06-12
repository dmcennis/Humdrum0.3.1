/*
 * Created on Jun 8, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package edu.osu.csml.humdrum.kern;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.HumdrumWarning;

/**
 * Defines warnings that are perfectly valid Humdrum but probably a kern mistake
 * @author Daniel McEnnis
 *
 */
public class KernWarning extends HumdrumWarning {

	/**
	 * Cretae a warning with a specific reason
	 * @param reason
	 * @param master
	 */
	public KernWarning(String reason,Controller master){
		super(reason,master);
	}
	
	/** 
	 * provide human readable version of warning
	 */
	public String toString(){
		return "Kern Warning: "+explanation + " at line " + master.getLineNumber() + " in spine "+ master.getSpineNumber()+System.getProperty("line.separator");
	}

}
