/*
 * Created on Aug 23, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

import java.io.Writer;

/**
 * the entire hierarchy of these controllers functions as the dispatcher in client-dispatch-server pattern in POSA1 p312.
 * 
 * This is similar to applying the mediator pattern (Design Patterns p273) to the observer pattern (Design Patterns p293) except only
 * the sending and recieving of messages is mediated.
 *
 * @author Daniel McEnnis
 * 
 */
public interface Controller {
	public void fire(Class type, Object data);
	public void register(Class type, View rec);
	public Controller getParent();
	public int getLineNumber();
	public int getSpineNumber();
	public Writer getErrStream();
}
