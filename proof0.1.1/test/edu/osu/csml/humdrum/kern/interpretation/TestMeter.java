/*
 * Created on Sep 1, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.interpretation;

import java.io.StringWriter;

import junit.framework.*;
import edu.osu.csml.humdrum.*;
import edu.osu.csml.humdrum.kern.*;
import edu.osu.csml.humdrum.kern.note.Barline;
import edu.osu.csml.humdrum.kern.note.Single;
import edu.osu.csml.humdrum.proofCommand.proof;

/**
 * Basic test of the Meter interpretation
 * @author Daniel McEnnis
 *
 */
public class TestMeter extends TestCase {
	StringWriter err;
	EventController stub;
	Meter test;
	Barline b;
	Single s;
	private proof proof= new proof();

	/**
	 * @param arg0
	 */
	public TestMeter(String arg0) {
		super(arg0);
	}

	public void setUp() {
		err = new StringWriter();
		stub = new EventController(new TestController(err));
	}

	public void testCreate() throws KernException, KernWarning {
		test = new Meter("*M4/4", stub);
	}

	public void testBadCreate() throws KernException, KernWarning {
			try {
				test = new Meter("*M0/3", stub);
				fail("0 should not be allowed as beats per measure");
			} catch (KernException e) {
				assertEquals("No other exceptions expected",0,err.getBuffer().length());
			}
	}

	public void testBasicMeasure() throws HumdrumException, HumdrumWarning {
		test = new Meter("*M2/4", stub);
		(b = new Barline(stub)).init("=");
		(s = new Single(stub)).init("4a");
		(s = new Single(stub)).init("4a");
		(b = new Barline(stub)).init("=");
		assertEquals("Unexpected Error occured:"+err.toString(),0,err.getBuffer().length());
	}

	public void testMeasrueWithPickup()
		throws HumdrumException, HumdrumWarning {
		test = new Meter("*M2/4", stub);
		(s = new Single(stub)).init("4a");
		(b = new Barline(stub)).init("=");
		(s = new Single(stub)).init("4a");
		(s = new Single(stub)).init("4a");
		(b = new Barline(stub)).init("=");
		assertEquals("Pickup not handled:"+err.toString(),0,err.getBuffer().length());
	}

	public void testMeasureWithTrailingNotes()
		throws HumdrumException, HumdrumWarning {
		test = new Meter("*M2/4", stub);
		(b = new Barline(stub)).init("=");
		(s = new Single(stub)).init("4a");
		(s = new Single(stub)).init("4a");
		(b = new Barline(stub)).init("=");
		(s = new Single(stub)).init("4a");
		assertEquals("Not handling trailing notes:"+err.toString(),0,err.getBuffer().length());
	}

	public void testBadMeasure() throws HumdrumException, HumdrumWarning {
			test = new Meter("*M2/4", stub);
			(b = new Barline(stub)).init("=");
			(s = new Single(stub)).init("4a");
			(s = new Single(stub)).init("4a");
			(s = new Single(stub)).init("4a");
			(b = new Barline(stub)).init("=");
			assertTrue("Should throw warning, but nothing found",err.getBuffer().length()>0);
			assertTrue("Must be warnings only",err.getBuffer().toString().matches("(.*Warning.*"+System.getProperty("line.separator")+")+"));
	}
	
	public void testGroupetto() throws HumdrumException,HumdrumWarning{
		test = new Meter("*M2/4",stub);
		(b = new Barline(stub)).init("=");
		(s = new Single(stub)).init("4a");
		(s = new Single(stub)).init("4bQ");
		(s = new Single(stub)).init("4a");
		(b = new Barline(stub)).init("=");
		assertEquals("Groupetto failed to register:"+err.getBuffer(),0,err.getBuffer().length());
	}

	public void testGraceNotes() throws HumdrumException,HumdrumWarning{
		test = new Meter("*M2/4",stub);
		(b = new Barline(stub)).init("=");
		(s = new Single(stub)).init("4a");
		(s = new Single(stub)).init("bq");
		(s = new Single(stub)).init("4a");
		(b = new Barline(stub)).init("=");		
		assertEquals("Not handling grace notes correctly",0,err.getBuffer().length());
	}

	public static Test suite() {
		return new TestSuite(TestMeter.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestMeter.class);
	}
}
