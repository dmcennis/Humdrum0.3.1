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
 * Basic test of the Single class
 * @author Daniel McEnnis
 *
 */
public class TestSingle extends TestCase {
	private TestController stub = new TestController(new java.io.OutputStreamWriter(System.err));
	private Single test;

	/**
	 * @param arg0
	 */
	public TestSingle(String arg0) {
		super(arg0);
	}

	public void setUp(){
		stub.clear();
	}

	public void testCreation(){
		test = new Single(stub);
	}
	
	public void testInit() throws HumdrumException,HumdrumWarning{
		test = new Single(stub);
		test.init("7a");
	}
	
	public void testFire() throws HumdrumException,HumdrumWarning{
		test = new Single(stub);
		test.init("7a");
		assertTrue("Failed to register notes",stub.data.size()>2);
		assertEquals("Problems with firing of events",stub.type.size(),stub.data.size());
	}
	
	public void testEqualsTrue() throws HumdrumException,HumdrumWarning{
		test = new Single(stub);
		Single test2 = new Single(stub);
		test.init("4.aa");
		test2.init("aa4.");
		assertTrue(test.equals(test2));
	}
	
	public void testEqualsFalse() throws HumdrumException,HumdrumWarning{
		test = new Single(stub);
		Single test2 = new Single(stub);
		test.init("4.aa");
		test2.init("6.bb");
		assertFalse(test.equals(test2));
	}
	
	public void testClone() throws HumdrumException,HumdrumWarning{
		test = new Single(stub);
		test.init("7aa");
		Single test2 = (Single)test.clone(stub);
		assertTrue(test.equals(test2));
	}

	public static Test suite() {
		return new TestSuite(TestSingle.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestSingle.class);
	}
}
