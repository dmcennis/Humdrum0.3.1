/*
 * Created on Aug 30, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.note;

import java.io.StringWriter;
import java.util.Arrays;

import edu.osu.csml.humdrum.TestController;
import edu.osu.csml.humdrum.kern.KernException;
import edu.osu.csml.humdrum.kern.KernWarning;

import junit.framework.*;

/**
 * Basic test of the Accent class
 * @author Daniel McEnnis
 *
 */
public class TestAccent extends TestCase {
	Accent test;
	StringWriter err;
	TestController stub;
	int[] expectedPresent = new int[] { Accent.ACCENT, //0
		Accent.ARPEGGIATION, //1
		Accent.ATACCA, //2
		Accent.GENERIC_ARTICULATION, //3
		Accent.GENERIC_ORNAMENT, //4
		Accent.INVERTED_MORDENT_SEMI, //5
		Accent.INVERTED_MORDENT_WHOLE, //6
		Accent.MORDENT_SEMI, //7
		Accent.MORDENT_WHOLE, //8
		Accent.NONE, //9
		Accent.ORNAMENTAL_TURN, //10
		Accent.PIZZICATO, //11
		Accent.SFOZANDO, //12
		Accent.STACCATO, //13
		Accent.TENUTO, //14
		Accent.TRILL_SEMI, //15
		Accent.TRILL_WHOLE, //16
		Accent.TURN, //17
		Accent.WAGNERIAN_TURN }; //18
	boolean[] data = new boolean[19];
	/**
	 * @param arg0
	 */
	public TestAccent(String arg0) {
		super(arg0);
	}

	public void setUp() {
		stub = new TestController(err = new StringWriter());
		test = new Accent(stub);
		Arrays.fill(data, false);
	}

	public void testNull() throws KernException, KernWarning {
		test.init("4a");
		data[9] = true;
		verifyStructure(data);
	}

	public void testSemiTrill() throws KernException, KernWarning {
		test.init("4at");
		data[15] = true;
		verifyStructure(data);
	}

	public void testWholeTrill() throws KernException, KernWarning {
		test.init("4aT");
		data[16] = true;
		verifyStructure(data);
	}

	public void testTurn() throws KernException, KernWarning {
		test.init("4aS");
		data[17] = true;
		verifyStructure(data);
	}

	public void testWagnerianTurn() throws KernException, KernWarning {
		test.init("4a$");
		data[18] = true;
		verifyStructure(data);
	}

	public void testArpeggiation() throws KernException, KernWarning {
		test.init("4a:");
		data[1] = true;
		verifyStructure(data);
	}

	public void testGenericOrnament() throws KernException, KernWarning {
		test.init("4aO");
		data[4] = true;
		verifyStructure(data);
	}

	public void testStaccato() throws KernException, KernWarning {
		test.init("4a'");
		data[13] = true;
		verifyStructure(data);
	}

	public void testPizzicato() throws KernException, KernWarning {
		test.init("4a\"");
		data[11] = true;
		verifyStructure(data);
	}

	public void testAttacca() throws KernException, KernWarning {
		test.init("4a`");
		data[2] = true;
		verifyStructure(data);
	}

	public void testTenuto() throws KernException, KernWarning {
		test.init("4a~");
		data[14] = true;
		verifyStructure(data);
	}

	public void testCaret() throws KernException, KernWarning {
		test.init("4a^");
		data[0] = true;
		verifyStructure(data);
	}

	public void testGenericArticulation() throws KernException, KernWarning {
		test.init("4aI");
		data[3] = true;
		verifyStructure(data);
	}

	public void testMordentWhole() throws KernException, KernWarning {
		test.init("4aM");
		data[8] = true;
		verifyStructure(data);
	}

	public void testInvertedMordentWhole() throws KernException, KernWarning {
		test.init("4aW");
		data[6] = true;
		verifyStructure(data);
	}

	public void testMordentSemi() throws KernException, KernWarning {
		test.init("4am");
		data[7] = true;
		verifyStructure(data);
	}

	public void testInvertedMordentSemi() throws KernException, KernWarning {
		test.init("4aw");
		data[5] = true;
		verifyStructure(data);
	}

	public void testSfozando() throws KernException, KernWarning {
		test.init("4az");
		data[12] = true;
		verifyStructure(data);
	}

	public void testOrnamentalTurn() throws KernException, KernWarning {
		test.init("4aR");
		data[10] = true;
		verifyStructure(data);
	}

	public void testBadTwoOrnaments() throws KernException, KernWarning {
		test.init("4aOS");
		assertTrue(
			"Generic and specific ornaments in the same token",
			err.getBuffer().length()>0);
		assertTrue(
			"Error detected, but not the right one: "
				+ new String(err.getBuffer()),
			(new String(err.getBuffer())).matches(
				".*Error.*[Gg]eneric.*[Ss]pecific.*"+System.getProperty("line.separator")));
	}

	public void testBadTwoArticulations() throws KernException, KernWarning {
		test.init("4aI^");
		assertTrue(
			"Generic and specific ornaments in the same token",
			err.getBuffer().length() > 0);
		assertTrue(
			"Error detected, but not the right one: "
				+ new String(err.getBuffer()),
			(new String(err.getBuffer())).matches(
				".*Error.*[Gg]eneric.*[Ss]pecific.*"+System.getProperty("line.separator")));
	}

	public void testClone() throws KernException, KernWarning {
		test.init("4b^");
		Object test2 = test.clone(stub);
		assertEquals("A cloned copy should be equal", test, test2);
	}

	public void testEquals() throws KernException, KernWarning {
		test.init("4b^");
		Accent test2 = new Accent(stub);
		test2.init("3a^");
		assertEquals(
			"Two identical accents claim to be different",
			test,
			test2);
	}

	public void testNotEquals() throws KernException, KernWarning {
		test.init("4b^");
		Accent test2 = new Accent(stub);
		test2.init("4b$");
		assertFalse("These are not the same", test.equals(test2));
	}

	public static Test suite() {
		return new TestSuite(TestAccent.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestAccent.class);
	}

	protected void verifyStructure(boolean[] data) {
		if (data.length != expectedPresent.length) {
			fail("BUG: List of accents present is the wrong size");
		}
		for (int i = 0; i < data.length; ++i) {
			if (data[i]) {
				assertTrue(
					"Item " + i + " asserted as not present when it is",
					test.isPresent(expectedPresent[i]));
			} else {
				assertTrue(
					"Item " + i + " asserted as present when it is not",
					!test.isPresent(expectedPresent[i]));
			}
		}
	}
}
