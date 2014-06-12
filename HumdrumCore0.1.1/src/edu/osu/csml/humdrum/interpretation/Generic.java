/*
 * Created on Aug 27, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.interpretation;

import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.Model;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.Controller;

/**
 * Used to represent all intepretations without processing instructions.
 * Listens for nothing, does nothing. Purely a placeholder
 * @author Daniel McEnnis
 * 
 */
public class Generic implements Interpretation {

	String content;
	
	/**
	 * Dont know what the interpretation stands for, so can't identify
	 * conflicts. 
	 * 
	 */
	public boolean isCompatible(Interpretation k) {
		return true;
	}
	
	public Generic(String c){
		content = c;
	}
	
	/**
	 * each object is identical, so clone is same as new object
	 */
	public View clone(Controller master) {
		return new Generic(content);
	}

	/**
	 * Intentionally does nothing 
	 * Should this throw an error? never should be called in the first place.
	 * 
	 */
	public void fire(Class type, Object trigger)
		throws HumdrumException, HumdrumWarning {
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(Controller master) {
		return new Generic("");
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(java.lang.String, edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(String token, Controller master) {
		return new Generic(token);
	}

}
