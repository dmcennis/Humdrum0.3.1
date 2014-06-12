/*
 * Created on Aug 29, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

/**
 * all instances of this interface create a new interpretation based on the
 * text of the interpretation provided.
 * @author Daniel McEnnis
 * 
 */
public interface InterpretationFactory {
	/**
	 * create a new interpretation described by 'token' within the context of
	 * the controller 'master'
	 * 
	 * @param token
	 *            string describing the interpretation to be created
	 * @param master
	 *            Event controller defining the current context.
	 * @return reference to the newly created interpretation
	 * @throws HumdrumException
	 * @throws HumdrumWarning
	 */
	abstract public Interpretation parseInterpretation(
		String token,
		Controller master)
		throws HumdrumException, HumdrumWarning;

	/**
	 * @param spineMaster
	 * @return
	 */
	public abstract InterpretationFactory prototype(EventController spineMaster);
}
