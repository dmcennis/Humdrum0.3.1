/*
 * Created on Feb 27, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

/**
 * Placeholder broadcasting the end of a line
 * 
 * @author daniel
 */
public class Line implements Model {
	/**
	 * Contents of this humdrum line
	 */
	private String data;
	
	/**
	 * Controller that passes events to this object
	 */
	private Controller master;

	/**
	 * Create a new Line from a string (always called by Document)
	 * @param data line of data 
	 * @param master controller that processes it
	 */
	public Line(String data,Controller master){
		this.master = master;
		this.data = data;
	}
	
	
	/** 
	 *  Create a new Line referencing the same string as this one
	 */
	public Object clone(Controller master) {
		return new Line(data,master);
	}

	/** 
	 * Lines can be equal even if their controllers aren't
	 */
	public boolean equals(Object obj) {
		if(obj instanceof Line){
			return this.data.equals(((Line)obj).data);
		}else{
			return false;
		}
	}


	@Override
	public String output() {
		return data;
	}

}
