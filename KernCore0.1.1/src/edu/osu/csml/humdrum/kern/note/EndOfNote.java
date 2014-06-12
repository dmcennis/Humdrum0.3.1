/*
 * Created on Sep 15, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.note;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.Model;

/**
 * Model designed explicitly for alerting the control that a note has been
 * finished.
 * @author Daniel McEnnis
 * 
 */
public class EndOfNote implements Model {

	/**
	 * Used exclusively by Multi to register boundaries between MultiStopNotes
	 */
	public Object clone(Controller master) {
		return new EndOfNote();
	}

	@Override
	public String output() {
		return "";
	}

}
