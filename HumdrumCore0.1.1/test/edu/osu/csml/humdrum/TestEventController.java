/*
 * Created on Aug 30, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

import junit.framework.*;

/**
 * Provide basic testing of the EventController class
 * @author Daniel McEnnis
 *
 */
public class TestEventController extends TestCase {
	private TestController stub = new TestController(new java.io.OutputStreamWriter(System.err));
	private EventController test = new EventController(stub);

	/**
	 * @param arg0
	 */
	public TestEventController(String arg0) {
		super(arg0);
	}

	// These end up being tested through other tests
	public void testNull(){
		
	}
//	public void testDefault(){
//		fail("Needs to be written");
//	}
//	
//	public void testClone(){
//		fail("Clone test not written");
//	}
//	
//	public void testMerge(){
//		fail("Merge test not written");
//	}
	
	public void setUp(){
		
	}

	public static Test suite() {
		return new TestSuite(TestEventController.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestEventController.class);
	}
}
