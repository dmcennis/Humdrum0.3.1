/*
 * Created on Aug 29, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

/**
 * interface defining how a kern spine should create data objects from a token
 * @author Daniel McEnnis
 * 
 */
public interface NoteFactory {
	/**
	 * stub defining how a kern spine should create a note
	 * 
	 * @param token
	 *            string containing the current humdrum token
	 * @param master
	 *            current context
	 * @return reference to a note
	 * @throws HumdrumException
	 * @throws HumdrumWarning
	 */
	public abstract Note getNote(String token, Controller master)
		throws HumdrumException, HumdrumWarning;

	/**
	 * @param spineMaster
	 * @return
	 */
	public abstract NoteFactory prototype(EventController spineMaster);
}
