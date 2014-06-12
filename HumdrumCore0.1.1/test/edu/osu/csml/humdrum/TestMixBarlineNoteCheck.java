/*
 * Created on Mar 3, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.*;

/**
 * Basic test of MixBarlineNoteCheck
 * 
 * @author Daniel McEnnis
 */
public class TestMixBarlineNoteCheck extends TestCase {

	Document test;
	StringReader demo;
	FileReader testLoader;
	StringWriter err;
	String prefix = "test"+File.separator;

	
	/**
	 * @param arg0
	 */
	public TestMixBarlineNoteCheck(String arg0) {
		super(arg0);
	}
	public void setUp(){
		err  = new StringWriter();
		test =new Document();
	}
	
	
	public void testMixedBarlinesNotes()throws java.io.IOException,HumdrumException,HumdrumWarning{
		test.parseDocument(new FileReader(prefix+"MixBarlineNoteCheck.krn"),err);
		if(err.getBuffer().length()==0){
			fail("Should have complained about mixed barlines and notes");
		}
		String tmp = new String(err.getBuffer());
		if(!tmp.matches(".*Error.*line.*5.*"+System.getProperty("line.separator"))){
			fail("Should be an Error on line 5" + err.getBuffer());
		}
	}
	
	public static Test suite() {
		return new TestSuite(TestMixBarlineNoteCheck.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestMixBarlineNoteCheck.class);
	}
	
}
