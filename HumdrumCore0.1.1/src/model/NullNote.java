/*
 * Created on Aug 21, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package model;

import java.io.Writer;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.Note;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;

/**
 * Model representing a placeholder note
 * @author Daniel McEnnis
 *
 */
public class NullNote extends Note {

	/**
	 * One placeholder note is the same as any other
	 */
	public Object clone(Controller master) {
		return new NullNote(master);
	}

	/**
	 * Placeholder, so this is generic
	 */
	public NullNote(Controller handler){
	super(handler);
}



	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.Note#init(java.lang.String, java.util.HashSet)
	 */
	public void init(String token)
		throws HumdrumException, HumdrumWarning {
			super.init(token);
			handler.fire(NullNote.class, this);
	}

	@Override
	public String output() {
		return ".";
	}

}
