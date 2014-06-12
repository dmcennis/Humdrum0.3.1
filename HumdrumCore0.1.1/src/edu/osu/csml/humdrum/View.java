/*
 * Created on Aug 22, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;


/**
 * Interface defining how interpretations interact with controllers, and by
 * extension with model (data) objects
 * @author Daniel McEnnis
 * 
 */
public interface View {
	/**
	 * routine utilized by controller objects to notify interpretations that
	 * data of interest to them has become available. Only ever called by
	 * Controllers. This really should be restricted to classes that implement
	 * the controller interface, but that functionality is not present in java.
	 * 
	 * @param type
	 *            class of the data object
	 * @param trigger
	 *            reference to the data object
	 * @throws KernException
	 * @throws KernWarning
	 */
	abstract public void fire(Class type, Object trigger)
		throws HumdrumException, HumdrumWarning;
	/**
	 * make a deep copy of this interpretation. Usually part of implementing a
	 * 'spine split' interpretation
	 * 
	 * @param master
	 * @return deep copy of this View in the context of 'master'
	 */
	abstract public View clone(Controller master);
	
	abstract public View prototype(Controller master);
	
	abstract public View prototype(String token,Controller master) throws HumdrumException, HumdrumWarning;
	
	abstract public boolean isCompatible(Interpretation k);
}
