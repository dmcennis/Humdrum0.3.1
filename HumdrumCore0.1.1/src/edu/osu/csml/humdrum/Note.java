/*
 * Created on Aug 20, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

/**
 * Describes a note as defined by the kern context
 * @author Daniel McEnnis
 * 
 */

public abstract class Note implements Model {
	/**
	 * context of this object. Handed in from parent
	 */
	protected Controller handler;

	/**
	 * Create a deep copy of the contents of this note, potentially in a new
	 * context
	 */
	public abstract Object clone(Controller master);

	/**
	 * create a new note within the context of handler
	 * 
	 * @param handler
	 *            Controller defining the current context
	 */
	public Note(Controller handler) {
		this.handler = handler;
	}

	/**
	 * Parse the string token and create a note object as defined by the
	 * string. Throw exceptions if there is a problem
	 * 
	 * @param token
	 */
	public void init(String token) throws HumdrumException, HumdrumWarning {
		// notify all listeners that a new note has been produced.
		handler.fire(Note.class, this);
	}

}
