/*
 * Created on Sep 1, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.interpretation;

import java.io.StringWriter;

import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.TestController;
import edu.osu.csml.humdrum.kern.note.Barline;
import edu.osu.csml.humdrum.EndOfToken;
import edu.osu.csml.humdrum.kern.note.Single;
import edu.osu.csml.humdrum.proofCommand.proof;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;

import junit.framework.*;

/**
 * Basic check of the BarlineCheck Class
 * @author Daniel McEnnis
 *
 */
public class TestBarlineCheck extends TestCase {
	BarlineCheck test;
	StringWriter err;
	EventController stub;
	Barline data;
	Single none;
	private proof proof= new proof();

	/**
	 * @param arg0
	 */
	public TestBarlineCheck(String arg0) {
		super(arg0);
	}

	public void setUp() {
		err = new StringWriter();
		stub =
			new EventController(new TestController(err));
		test = new BarlineCheck(stub);
		data = new Barline(stub);
		none = new Single(stub);
	}

	public void testDoubleBarline() throws HumdrumException, HumdrumWarning {
		data.init("==");
		none.init("4a");
		if (err.getBuffer().length() == 0) {
			fail("Should complain about data after double barline");
		} else if (
			!(new String(err.getBuffer())).matches(".*Error.*[Dd]ouble.*"+System.getProperty("line.separator"))) {
			fail("Wrong error thrown: " + err.getBuffer().toString());
		}
	}

	public void testNumbering() throws HumdrumException, HumdrumWarning {
		data.init("=1");
		data = new Barline(stub);
		data.init("=2");
	}

	public void testBadNumbering() throws HumdrumException, HumdrumWarning {
			data.init("=2");
			stub.fire(EndOfToken.class,new EndOfToken());
			data = new Barline(stub);
			data.init("=1");
			stub.fire(EndOfToken.class,new EndOfToken());
			assertTrue("No errors despite bad numbering",err.getBuffer().length()>0);
			assertTrue("Wrong error reported:"+err.getBuffer().toString(),err.getBuffer().toString().matches(".*Error.*[Ii]ncreasing.*"+System.getProperty("line.separator")));
	}

	public void testClone() throws HumdrumException, HumdrumWarning {
		data.init("==2");
		Object test2 = test.clone(stub);
		assertEquals("Cloned objects should be equal", test, test2);
	}

	public void testNotEquals() throws HumdrumException, HumdrumWarning {
		data.init("==2");
		stub = new EventController(new TestController(new StringWriter()));
		Barline test2 = new Barline(stub);
		data = new Barline(stub);
		data.init("=1");
		assertFalse("Claim to be equal but aren't", test.equals(test2));
	}

	public void testEquals() throws HumdrumException, HumdrumWarning {
		data.init("=2");
		stub = new EventController(new TestController(new StringWriter()));
		BarlineCheck test2 = new BarlineCheck(stub);
		data = new Barline(stub);
		data.init("=2");
		assertTrue("Should be equal but aren't", test.equals(test2));
	}

	public static Test suite() {
		return new TestSuite(TestBarlineCheck.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestBarlineCheck.class);
	}
}
