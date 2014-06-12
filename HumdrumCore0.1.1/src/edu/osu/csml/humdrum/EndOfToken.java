/*
 * Created on Sep 15, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.Model;

/**
 * Model used exclusively to signal controllers that a token is now completely
 * parsed and no additional data will be presented for this token.
 * @author Daniel McEnnis
 * 
 */
public class EndOfToken implements Model {

	public Object clone(Controller master) {
		return new EndOfToken();
	}

	@Override
	public String output() {
		return "";
	}

	
}
