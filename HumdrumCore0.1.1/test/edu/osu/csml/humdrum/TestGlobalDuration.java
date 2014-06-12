/*
 * Created on Mar 1, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.*;

/**
 * Basic test of the GlobalDurationCheck
 * 
 * @author Daniel McEnnis
 */
public class TestGlobalDuration extends TestCase {
	Document test;
	StringReader demo;
	FileReader testLoader;
	StringWriter err;
	String prefix = "test"+File.separator;
	
	/**
	 * @param arg0
	 */
	public TestGlobalDuration(String arg0) {
		super(arg0);
	}

	public void setUp(){
		err  = new StringWriter();
		test =new Document();
	}
	
	public void testBadCrossSpineDuration() throws FileNotFoundException{
		test.parseDocument(new FileReader(prefix+"BadDuration.krn"),err);
		if(err.getBuffer().length()==0){
			fail("Should throw error about bad duration");
		}
		String tmp = new String(err.getBuffer());
		if(!tmp.matches(".*Error.*"+System.getProperty("line.separator"))){
			fail("Should be error, not a warning" + err.getBuffer());
		}
	}
	
	public void testDurationSumDoesntSum() throws FileNotFoundException{
		test.parseDocument(new FileReader(prefix+"TestDurationSumBug.krn"),err);
		if(err.getBuffer().length()!=0){
			fail("No error should be thrown: "+err.toString());
		}
	}
	
	public static Test suite() {
		return new TestSuite(TestGlobalDuration.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestGlobalDuration.class);
	}
}
