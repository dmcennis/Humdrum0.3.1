/*
 * Created on Aug 21, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.note;

import java.io.OutputStreamWriter;

import junit.framework.*;
import edu.osu.csml.humdrum.kern.*;
import edu.osu.csml.humdrum.TestController;

/**
 * Basic test of the Duration class
 * @author Daniel McEnnis
 *
 */
public class TestDuration extends TestCase {
	Duration temp;
	TestController stub = new TestController(new OutputStreamWriter(System.err));

	/**
	 * @param arg0
	 */
	public TestDuration(String arg0) {
		super(arg0);
	}

	public void setUp() {
		temp = new Duration(stub);
	}

	public void testSanity() throws KernException, KernWarning {
		temp.init("4a");
		assertEquals(0.25, temp.getDuration(), 0.0001);
	}

	public void testSingleDot() throws KernException, KernWarning {
		temp.init("4.a");
		assertEquals(0.375, temp.getDuration(), 0.0001);
	}

	public void testMultipleDot() throws KernException, KernWarning {
		temp.init("2..a");
		assertEquals(0.875, temp.getDuration(), 0.0001);
	}

	public void testDoubleDuration() throws KernWarning {
		try {
			temp.init("2..a5");
			fail("Should have thrown exception");
		} catch (KernException e) {
			;
		}
	}

	public void testBadDots() throws KernWarning {
		try {
			temp.init("2a.");
			fail("Should have thrown exception");
		} catch (KernException e) {
			;
		}
	}
	
	public void testBreve() throws KernException, KernWarning {
		temp.init("0g");
		assertEquals(1.0,temp.getDuration(),0.0001);
	}
		
	public void testBreveWithDots() throws KernException, KernWarning {
		temp.init("0..a");
		assertEquals(1.75, temp.getDuration(), 0.0001);
}
	public void testEqualsTrue() throws KernException, KernWarning{
		temp.init("2..a");
		Duration temp2 = new Duration(stub);
		temp2.init("2..b");
		assertTrue(temp.equals(temp2));
	}
	
	public void testEqualsFalse() throws KernException, KernWarning{
		temp.init("2..a");
		Duration temp2 = new Duration(stub);
		temp2.init("4a");
		assertFalse(temp.equals(temp2));
	}
	
	public void testClone() throws KernException, KernWarning{
		temp.init("2..a");
		Duration temp2 = (Duration)temp.clone(new TestController(new java.io.OutputStreamWriter(System.err)));
		assertTrue(temp.equals(temp2));
	}
	
	public void testModifiedAppogiatura()throws KernException, KernWarning{
		temp.init("2.ap");
		assertTrue("Failed to recognize appoggiaturas",temp.isModifiedByAppoggiatura());
	}
	
	public void testAppoggiatura()throws KernException, KernWarning{
	temp.init("2.aP");
	assertTrue("Failed to recognize appoggiaturas",temp.isAppoggiatura());
}

	public void testGraceNote()throws KernException, KernWarning{
		temp.init("aq");
		assertTrue("Failed to recognize grace notes",temp.isAcciaccaturas());
	}
	
	public void testGroupetto()throws KernException, KernWarning{
		temp.init("2aQ");
		assertTrue("failed to recognize groupetto note",temp.isGroupetto());
	}
	
	public static Test suite() {
		return new TestSuite(TestDuration.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestDuration.class);
	}
}
