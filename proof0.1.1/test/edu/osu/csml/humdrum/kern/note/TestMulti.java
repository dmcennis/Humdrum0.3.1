/*
 * Created on Aug 28, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.note;

import junit.framework.*;
import edu.osu.csml.humdrum.*;

/**
 * Basic test of the Multi class
 * @author Daniel McEnnis
 *
 */
public class TestMulti extends TestCase {
	private Multi test;
	private TestController stub = new TestController(new java.io.OutputStreamWriter(System.err));

	/**
	 * @param arg0
	 */
	public TestMulti(String arg0) {
		super(arg0);
	}

	public void setUp() {
		stub.data.clear();
		stub.rec.clear();
		stub.type.clear();
	}

	public void testConstructor() throws HumdrumException, HumdrumWarning {
		test = new Multi(stub);
	}

	public void testInit() throws HumdrumException, HumdrumWarning {
		test = new Multi(stub);
		test.init("7a 7b");
	}

	public void testTwoNoteFire() throws HumdrumException, HumdrumWarning {
		test = new Multi(stub);
		test.init("7a 7b");
		assertEquals(19, stub.data.size());
		assertEquals(19, stub.type.size());
	}

	public void testThreeNoteFire() throws HumdrumException, HumdrumWarning {
		test = new Multi(stub);
		test.init("7a 7b 7c");
		assertEquals(27, stub.data.size());
		assertEquals(27, stub.type.size());
	}

	public void testRestInMultiStop() throws HumdrumException, HumdrumWarning {
		try {
			test = new Multi(stub);
			test.init("4a 4b 4r");
			fail("Should complain about rests in multistop");
		} catch (HumdrumException e) {
			;
		}
	}

	public void testDifferingDurations() throws HumdrumException, HumdrumWarning {
			test = new Multi(stub);
			test.init("4a 5b 5c");
						
	}
	
	public void testEqualsTrue()throws HumdrumException, HumdrumWarning{
		test = new Multi(stub);
		test.init("5a 5b 5c");
		Multi test2 = new Multi(stub);
		test2.init("5a 5b 5c");
		assertTrue(test.equals(test2));
	}
	
	public void testEqualsFalse() throws HumdrumException, HumdrumWarning{
		test = new Multi(stub);
		test.init("5a 5b 5c");
		Multi test2 = new Multi(stub);
		test2.init("5a 5b");
		assertFalse(test.equals(test2));
	}

	
	public void testClone()throws HumdrumException, HumdrumWarning{
		test = new Multi(stub);
		test.init("5a 5b 5c");
		Multi test2 = (Multi)test.clone(stub);
		assertTrue(test.equals(test2));
	}

	public static Test suite() {
		return new TestSuite(TestMulti.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestMulti.class);
	}
}
