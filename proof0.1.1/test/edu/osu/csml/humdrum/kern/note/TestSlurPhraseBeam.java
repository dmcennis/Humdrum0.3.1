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
import junit.framework.*;

/**
 * Basic tests of the SlurPhraseBeam class
 * @author Daniel McEnnis
 *
 */
public class TestSlurPhraseBeam extends TestCase {
	SlurPhraseBeam test;
	TestController stub;
	StringWriter err;

	/**
	 * @param arg0
	 */
	public TestSlurPhraseBeam(String arg0) {
		super(arg0);
	}

	public void setUp() {
		stub = new TestController(err = new StringWriter());
		test = new SlurPhraseBeam(stub);
	}

	public void testTrivialCase() throws KernException, KernWarning {
		test.init("7a");
		assertTrue(test.get(SlurPhraseBeam.NONE) > 0);
	}

	public void testSimpleSlur() throws KernException, KernWarning {
		test.init("(4b");
		assertTrue(test.get(SlurPhraseBeam.OPEN_SLUR) == 1);
		assertEquals(0, err.getBuffer().length());
	}

	public void testSimpleClosedSlur() throws KernException, KernWarning {
		test.init("4b)");
		assertTrue(test.get(SlurPhraseBeam.CLOSE_SLUR) == 1);
		assertEquals(0, err.getBuffer().length());
	}

	public void testSimpleElidedSlur() throws KernException, KernWarning {
		test.init("&(4b");
		assertTrue(
			"Should have found an elided slur:" + new String(err.getBuffer()),
			test.get(SlurPhraseBeam.OPEN_ELIDED_SLUR) == 1);
		assertEquals(0, err.getBuffer().length());
	}

	public void testBadDuplicateElidedSlur()
		throws KernException, KernWarning {
		try {
			test.init("&(&(4b");
			fail("Duplicate Elided Slurs should be detected");
		} catch (KernException e) {
			;
		}
	}

	public void testBadMultiElidedSlur() throws KernException, KernWarning {
		try {
			test.init("&(4b&(");
			fail("Extra Slur should be detected");
		} catch (KernException e) {
			;
		}
	}

	public void testSimpleClosedElidedSlur()
		throws KernException, KernWarning {
		test.init("4b&)");
		assertTrue(test.get(SlurPhraseBeam.CLOSE_ELIDED_SLUR) == 1);
		assertEquals(0, err.getBuffer().length());
	}

	public void testBadDuplicateClosedElidedSlur()
		throws KernException, KernWarning {
		try {
			test.init("4b&)&)");
			fail("Duplicate Slurs should be detected");
		} catch (KernException e) {
			;
		}
	}

	public void testBadMultiClosedElidedSlur()
		throws KernException, KernWarning {
		try {
			test.init("&)4b&)");
			fail("Extra Slur should be detected");
		} catch (KernException e) {
			;
		}
	}

	public void testSimplePhrase() throws KernException, KernWarning {
		test.init("{4b");
		assertEquals(
			"Should find a phrase:" + new String(err.getBuffer()),
			1,
			test.get(SlurPhraseBeam.OPEN_PHRASE));
		assertEquals(
			"There should be no errors here:" + new String(err.getBuffer()),
			0,
			err.getBuffer().length());
	}

	public void testSimpleClosedPhrase() throws KernException, KernWarning {
		test.init("4b}");
		assertEquals(1, test.get(SlurPhraseBeam.CLOSE_PHRASE));
		assertEquals(0, err.getBuffer().length());
	}

	public void testSimpleElidedPhrase() throws KernException, KernWarning {
		test.init("&{4b");
		assertEquals(1, test.get(SlurPhraseBeam.OPEN_ELIDED_PHRASE));
		assertEquals(0, err.getBuffer().length());
	}

	public void testBadDuplicateElidedPhrase()
		throws KernException, KernWarning {
		try {
			test.init("&{&{4b");
			fail("Duplicate Slurs should be detected");
		} catch (KernException e) {
			;
		}
	}

	public void testBadMultiElidedPhrase() throws KernException, KernWarning {
		try {
			test.init("&{4b&{");
			fail("Extra Slur should be detected");
		} catch (KernException e) {
			;
		}
	}

	public void testSimpleClosedElidedPhrase()
		throws KernException, KernWarning {
		test.init("4b&}");
		assertEquals(1, test.get(SlurPhraseBeam.CLOSE_ELIDED_PHRASE));
		assertEquals(0, err.getBuffer().length());
	}

	public void testBadDuplicateClosedElidedPhrase()
		throws KernException, KernWarning {
		try {
			test.init("4b&}&}");
			fail("Duplicate Slurs should be detected");
		} catch (KernException e) {
			;
		}
	}

	public void testBadMultiClosedElidedPhrase()
		throws KernException, KernWarning {
		try {
			test.init("&}4b&}");
			fail("Extra Slur should be detected");
		} catch (KernException e) {
			;
		}
	}

	public void testSimpleBeam() throws KernException, KernWarning {
		test.init("4bL");
		assertEquals(1, test.get(SlurPhraseBeam.OPEN_BEAM));
		assertEquals(0, err.getBuffer().length());
	}

	public void testDuplicateBeam() throws KernException, KernWarning {
		test.init("4bLL");
		assertEquals(2, test.get(SlurPhraseBeam.OPEN_BEAM));
		assertEquals(0, err.getBuffer().length());
	}

	public void testBadMultiBeam() throws KernException, KernWarning {
		try {
			test.init("L4bL");
			fail("Extra Beam should be detected");
		} catch (KernException e) {
			;
		}
	}

	public void testSimpleClosedBeam() throws KernException, KernWarning {
		test.init("4bJ");
		assertEquals(1, test.get(SlurPhraseBeam.CLOSE_BEAM));
		assertEquals(0, err.getBuffer().length());
	}

	public void testDuplicateClosedBeam() throws KernException, KernWarning {
		test.init("4bJJ");
		assertEquals(2, test.get(SlurPhraseBeam.CLOSE_BEAM));
	}

	public void testBadMultiClosedBeam() throws KernException, KernWarning {
		try {
			test.init("J4bJ");
			fail("Misplaced J should be detected");
		} catch (KernException e) {
			;
		}
	}

	public void testClone() {
		Object test2 = test.clone(stub);
		assertEquals("Cloned objects should be the same", test, test2);
	}

	public void testEquals() throws KernException {
		SlurPhraseBeam test2 = new SlurPhraseBeam(stub);
		test.init("4aL");
		test2.init("5bL");
		assertEquals("Identical markings should be equal", test, test2);
	}

	public void testNotEquals() throws KernException {
		SlurPhraseBeam test2 = new SlurPhraseBeam(stub);
		test.init("4aL");
		test2.init("5b{");
		assertFalse(
			"Differing markings should not be equal",
			test.equals(test2));
	}

	public static Test suite() {
		return new TestSuite(TestSlurPhraseBeam.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestSlurPhraseBeam.class);
	}
}
