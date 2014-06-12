/*
 * Created on Sep 12, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.interpretation;

import java.io.StringWriter;

import model.NullNote;

import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Note;
import edu.osu.csml.humdrum.SpineEOF;
import edu.osu.csml.humdrum.TestController;
import edu.osu.csml.humdrum.EndOfToken;
import edu.osu.csml.humdrum.kern.note.Single;
import edu.osu.csml.humdrum.proofCommand.proof;
import junit.framework.*;

/**
 * Basic Check of the TieCheck interpretation
 * @author Daniel McEnnis
 *
 */
public class TestTieCheck extends TestCase {
	EventController stub;
	StringWriter err;
	TieCheck test;
	Single note;
	private proof proof= new proof();

	/**
	 * @param arg0
	 */
	public TestTieCheck(String arg0) {
		super(arg0);
	}

	public void setUp() {
		stub =
			new EventController(new TestController(err = new StringWriter()));
		test = new TieCheck(stub);
	}

	public void testTrivialBase() throws HumdrumException, HumdrumWarning {
		final String[] data = new String[] { "4a[", "2a]" };
		processNotes(data);
		assertEquals(
			"Exception thrown when none present:" + err.getBuffer().toString(),
			0,
			err.getBuffer().length());
	}

	public void testSimple() throws HumdrumException, HumdrumWarning {
		final String[] data = new String[] { "4b[", "4b_", "4b]" };
		processNotes(data);
		assertEquals(
			"No error should be thrown here:" + new String(err.getBuffer()),
			0,
			err.getBuffer().length());
	}

	public void testSuspended() throws HumdrumException, HumdrumWarning {
		final String[] data = new String[] { "4a[", ".", "4a]" };
		processNotes(data);
		assertEquals(
			"No error should be thrown here:" +err.getBuffer().toString(),
			0,
			err.getBuffer().length());
	}

	public void testBadOpenOnly() throws HumdrumException, HumdrumWarning {
		final String[] data = new String[] { "4b[" };
		processNotes(data);
		assertTrue(
			"There should be an error but none found:"
				+ new String(err.getBuffer()),
			err.getBuffer().length() > 0);
		assertTrue(
			"There should be 1 error about open tie, but not found:"
				+ new String(err.getBuffer()),
			err.getBuffer().toString().matches(".*Error.*[Tt]ie.*"+System.getProperty("line.separator")));
	}

	public void testBadOpenContinue() throws HumdrumException, HumdrumWarning {
		final String[] data = new String[] { "4b[", "3b_" };
		processNotes(data);
		assertTrue(
			"There should be an error but none found:"
				+ new String(err.getBuffer()),
			err.getBuffer().length() > 0);
		assertTrue(
			"There should be an error about an unclosed tie, but not found:"
				+ new String(err.getBuffer()),
			err.getBuffer().toString().matches(".*Error.*[Tt]ie.*"+System.getProperty("line.separator")));
	}

	public void testBadContinue() throws HumdrumException, HumdrumWarning {
		final String[] data = new String[] { "3b_" };
		processNotes(data);
		assertTrue(
			"There should be an error but none found:"
				+ new String(err.getBuffer()),
			err.getBuffer().length() > 0);
		assertTrue(
			"There should be an error about invalid tie, but not found:"
				+ new String(err.getBuffer()),
			(new String(err.getBuffer())).matches(".*Error.*[Tt]ie.*"+System.getProperty("line.separator")));
	}

	public void testBadEnd() throws HumdrumException, HumdrumWarning {
		final String[] data = new String[] { "3b]" };
		processNotes(data);
		assertTrue(
			"There should be an error but none found:"
				+ new String(err.getBuffer()),
			err.getBuffer().length() > 0);
		assertTrue(
			"There should be an error about invalid tie, but not found:"
				+ new String(err.getBuffer()),
			(new String(err.getBuffer())).matches(".*Error.*[Tt]ie.*"+System.getProperty("line.separator")));
	}

	public void testSuspendedBadContinue()
		throws HumdrumException, HumdrumWarning {
		final String[] data = new String[] { "3b[", "3b]", "3b_" };
		processNotes(data);
		assertTrue(
			"There should be an error but none found:"
				+ new String(err.getBuffer()),
			err.getBuffer().length() > 0);
		assertTrue(
			"There should be an error about invalid tie, but not found:"
				+ new String(err.getBuffer()),
			(new String(err.getBuffer())).matches(".*Error.*[Tt]ie.*"+System.getProperty("line.separator")));
	}

	public void testBadPitch() throws HumdrumException, HumdrumWarning {
		final String[] data = new String[] { "3b[", "3a]" };
		processNotes(data);
		assertTrue(
			"There should be an error but none found:"
				+ new String(err.getBuffer()),
			err.getBuffer().length() > 0);
		assertTrue(
			"There should be an error about a bad pitch, but not found:"
				+ new String(err.getBuffer()),
			(new String(err.getBuffer())).matches(".*Error.*[Pp]itch(.*"+System.getProperty("line.separator")+")+"));
	}

	public void testClone() throws HumdrumException, HumdrumWarning {
		note = new Single(stub);
		note.init("4a[");
		Object test2 = test.clone(stub);
		assertEquals("test2 should be a clone of test but isn't", test, test2);
	}

	public void testEquals() throws HumdrumException, HumdrumWarning {
		note = new Single(stub);
		note.init("4a[");
		stub = new EventController(new TestController(err));
		TieCheck test2 = new TieCheck(stub);
		note = new Single(stub);
		note.init("3a[");
		assertEquals(
			"These items should be identical, but aren't",
			test,
			test2);
	}

	public void testPitchNotEquals() throws HumdrumException, HumdrumWarning {
		note = new Single(stub);
		note.init("4a[");
		stub.fire(EndOfToken.class,new EndOfToken());
		stub = new EventController(new TestController(err));
		TieCheck test2 = new TieCheck(stub);
		note = new Single(stub);
		note.init("3b[");
		stub.fire(EndOfToken.class,new EndOfToken());
		assertTrue(
			"These items should have differing pitches, but are labeled equal",
			!test.equals(test2));
	}

	public void testTypeNotEquals() throws HumdrumException, HumdrumWarning {
		note = new Single(stub);
		note.init("4a[");
		note = new Single(stub);
		note.init("4a_");
		stub.fire(EndOfToken.class,new EndOfToken());
		stub = new EventController(new TestController(err));
		TieCheck test2 = new TieCheck(stub);
		note = new Single(stub);
		note.init("3b[");
		stub.fire(EndOfToken.class,new EndOfToken());
		assertTrue(
			"These items should have differing tokens, but are labeled equal",
			!test.equals(test2));
	}

	public static Test suite() {
		return new TestSuite(TestTieCheck.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestTieCheck.class);
	}

	protected void processNotes(String[] data)
		throws HumdrumWarning, HumdrumException {
		for (int i = 0; i < data.length; ++i) {
			if (data[i].equals(".")) {
				Note tmp = new NullNote(stub);
				tmp.init(data[i]);
			} else {
				note = new Single(stub);
				note.init(data[i]);
			}
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
	}
}
