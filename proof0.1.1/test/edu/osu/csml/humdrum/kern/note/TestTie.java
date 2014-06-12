/*
 * Created on Sep 12, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.note;

import java.io.StringWriter;

import edu.osu.csml.humdrum.TestController;
import edu.osu.csml.humdrum.kern.KernException;
import edu.osu.csml.humdrum.kern.KernWarning;
import junit.framework.TestCase;

/**
 * Basic tests of the Tie class
 * @author Daniel McEnnis
 *
 */
public class TestTie extends TestCase {
	StringWriter err;
	TestController stub;
	Tie test;
	/**
	 * @param arg0
	 */
	public TestTie(String arg0) {
		super(arg0);
	}

	public void setUp() {
		err = new StringWriter();
		stub = new TestController(err);
		test = new Tie(stub);
	}

	public void testTrivialOpen() throws KernException, KernWarning {
		test.init("7a[");
		if (err.getBuffer().length() > 0) {
			fail(
				"No errors should have occurred" + err.getBuffer().toString());
		}
		assertTrue(test.isPresent(Tie.OPEN));
	}

	public void testTrivialContinue() throws KernException, KernWarning {
		test.init("7a_");
		if (err.getBuffer().length() > 0) {
			fail(
				"No errors should have occurred" + err.getBuffer());
		}
		assertTrue(test.isPresent(Tie.CONTINUE));
	}

	public void testTrivialEnd() throws KernException, KernWarning {
		test.init("4a]");
		if (err.getBuffer().length() > 0) {
			fail(
				"No errors should have occurred" + err.getBuffer().toString());
		}
		assertTrue(test.isPresent(Tie.END));
	}

	public void testBadDuplicateOpen() throws KernException, KernWarning {
		test.init("4a[[");
		if (err.getBuffer().length() == 0) {
			fail("Should complain about duplicate entries");
		} else if (
			!(err.getBuffer().toString().matches(
				".*Error.*[Dd]uplicate(.*"+System.getProperty("line.separator")+")+"))) {
			fail(
				"Error found - but not the right one: "
					+ err.getBuffer().toString());
		}
	}

	public void testBadMultipleEnd() throws KernException, KernWarning {
		test.init("4]a]");
		if (err.getBuffer().length() == 0) {
			fail("Should complain about duplicate entries");
		} else if (
			!(err.getBuffer().toString().matches(
				".*Error.*only appear once.*"+System.getProperty("line.separator")))) {
			fail(
				"Error found, but not the right one:"
					+ err.getBuffer().toString());
		}
	}

	public void testEquals() throws KernException, KernWarning {
		Tie test2 = new Tie(stub);
		test.init("4a[");
		test2.init("3c[");
		assertTrue("should be equal but wasn't", test.equals(test2));
	}

	public void testNotEquals() throws KernException, KernWarning {
		Tie test2 = new Tie(stub);
		test.init("4a[");
		test2.init("4a_");
		assertFalse("should not be equal but was", test.equals(test2));
	}

	public void testClone() throws KernException, KernWarning {
		Tie test2 = (Tie) test.clone(stub);
		assertTrue(
			"Cloned Tie should be the same as the original",
			test.equals(test2));
	}

	public static void main(String[] args) {
	}
}
