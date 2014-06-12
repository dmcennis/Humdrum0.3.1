/*
 * Created on Sep 11, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

import junit.framework.*;

/**
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 * @author Daniel McEnnis
 *
 */
public class TestSpineFactory extends TestCase {


	/**
	 * @param arg0
	 */
	public TestSpineFactory(String arg0) {
		super(arg0);
	}

	public static Test suite() {
		return new TestSuite(TestSpineFactory.class);
	}
	
	public void testCreateKern(){
		Spine test = SpineFactory.parseSpine("**kern",new TestController(new java.io.OutputStreamWriter(System.err)));
		assertTrue(test instanceof Spine);
	}

	public void testCreateGeneric(){
		Spine test = SpineFactory.parseSpine("**gen",new TestController(new java.io.OutputStreamWriter(System.err)));
		assertTrue(test instanceof GenericSpine);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestSpineFactory.class);
	}
}
