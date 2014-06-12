/*
 * Created on Feb 28, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.*;

/**
 * Basic test of the DupMeasureCheck
 * 
 * @author Daniel McEnnis
 */
public class TestDupMeasureCheck extends TestCase {
	Document test;
	StringReader demo;
	FileReader testLoader;
	StringWriter err;
	String prefix = "test"+File.separator;

	/**
	 * @param arg0
	 */
	public TestDupMeasureCheck(String arg0) {
		super(arg0);
	}

	public void setUp() {
		err = new StringWriter();
		test = new Document();
	}

	public void testDuplicatedMeasureSimple() throws FileNotFoundException {
		test.parseDocument(new FileReader(prefix + "MeasureDup.hum"), err);
		if (err.getBuffer().length() == 0) {
			fail("Should throw warning about duplicated measure");
		}
		String tmp = new String(err.getBuffer());
		if (!tmp
			.matches(".*Warning.*" + System.getProperty("line.separator"))) {
			fail("Should be warning, not error" + err.getBuffer());
		}
	}

	public void testDuplicatedMeasureComplex() throws FileNotFoundException {
		test.parseDocument(
			new FileReader(prefix + "MeasureDupComplex.hum"),
			err);
		if (err.getBuffer().length() == 0) {
			fail("Should throw warning about duplicated measure");
		}
		String tmp = new String(err.getBuffer());
		if (!tmp
			.matches(".*Warning.*" + System.getProperty("line.separator"))) {
			fail("Should be warning, not error" + err.getBuffer());
		}
	}

	public static Test suite() {
		return new TestSuite(TestDupMeasureCheck.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestDupMeasureCheck.class);
	}
}
