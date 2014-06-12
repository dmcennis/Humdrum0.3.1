/*
 * Created on Feb 28, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

import java.util.Vector;

/**
 * @author Daniel McEnnis
 * 
 * Class describing a single unprocessed measure of humdrum data. See
 * {@link DupMeasureCheck#Fire(Controller,Model) DupMeasureCheck.Fire}for the
 * description of the measure parsing algorithm
 */
public class Measure {
	/**
	 * ordered list of humdrum lines
	 */
	private Vector content;

	/**
	 *  
	 */
	public Measure() {
		content = new Vector();
	}

	/**
	 * attach a new humdrum line to the end of the measure
	 * 
	 * @param line
	 *            humdrum data provided by document
	 */
	public void addLine(Line line) {
		content.add(line);
	}

	/**
	 * Executes line by line, character by character comparison of the 2
	 * measure objects.
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Measure) {
			return this.content.equals(((Measure) obj).content);
		} else {
			return false;
		}
	}

}
