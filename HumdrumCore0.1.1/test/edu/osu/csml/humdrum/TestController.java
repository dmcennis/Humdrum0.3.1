/*
 * Created on Aug 27, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

import java.io.Writer;
import java.util.Vector;

/**
 * Dummy implementation of COntroller interface useful to test correctness of
 * objects that interact with controllers.
 * @author Daniel McEnnis
 * 
 */
public class TestController implements Controller {
	/**
	 * contains a list of all data types passed into or registered with the
	 * controller - in order
	 */
	public Vector type = new Vector();
	/**
	 * contains a list of references to all objects fired to this controller
	 */
	public Vector data = new Vector();
	/**
	 * contains a list of references to all interpretations that have
	 * registered with this object in order
	 */
	public Vector rec = new Vector();
	private int spine;
	private int line;
	private Writer err;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.Controller#getErrStream()
	 */
	public Writer getErrStream() {
		return err;
	}

	public TestController(Writer err) {
		spine = 1;
		line = 1;
		this.err = err;
	}
	public TestController(int line, int spine) {
		this.line = line;
		this.spine = spine;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.Controller#fire(java.lang.Class,
	 *      edu.osu.csml.humdrum.Model)
	 */
	public void fire(Class type, Object data) {
		this.type.add(type);
		this.data.add(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.Controller#register(java.lang.Class,
	 *      edu.osu.csml.humdrum.View)
	 */
	public void register(Class type, View rec) {
		this.type.add(type);
		this.rec.add(rec);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.Controller#getParent()
	 */
	public Controller getParent() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.Controller#getLineNumber()
	 */
	public int getLineNumber() {
		return line;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.Controller#getSpineNumber()
	 */
	public int getSpineNumber() {
		return spine;
	}

	public void clear() {
		rec.clear();
		data.clear();
		type.clear();
	}

}
