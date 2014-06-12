/*
 * Created on Aug 22, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;


/**
 * Interface describing an arbitrary piece of data located within a Humdrum
 * token. Generally, models also communicate extensively with their
 * controllers, but this is not required and so this functionality is not
 * defined here
 * @author Daniel McEnnis
 * 
 */
public interface Model {
	abstract public Object clone(Controller master);
	
	abstract public String output();
}
