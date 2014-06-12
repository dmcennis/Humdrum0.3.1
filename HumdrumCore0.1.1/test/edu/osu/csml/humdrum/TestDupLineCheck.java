/*
 * Created on Feb 28, 2004
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
 * Basic check of the DupLineCheck class
 * 
 * @author Daniel McEnnis
 */
public class TestDupLineCheck extends TestCase {
	Document test;
	StringReader demo;
	FileReader testLoader;
	StringWriter err;
	String prefix = "test"+File.separator;
	

	/**
	 * 
	 * @param arg0
	 */
	public TestDupLineCheck(String arg0) {
		super(arg0);
	}

	public void testFireWarning() throws FileNotFoundException {
			test.parseDocument(new FileReader(prefix+"LineDup.hum"),err);
			if(err.getBuffer().length()==0){
				fail("Should throw warning about duplicated lines");
			}
			String tmp = new String(err.getBuffer());
			if(!tmp.matches(".*Warning.*"+System.getProperty("line.separator"))){
				fail("Should be warning, not an error" + err.getBuffer());
			}
			
	}
	
	public void setUp(){
		err  = new StringWriter();
		test =new Document();
	}
	
	
	
	public static Test suite() {
		return new TestSuite(TestDupLineCheck.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestDupLineCheck.class);
	}
}
