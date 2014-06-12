/*
 * Created on Aug 30, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.note;

import java.io.StringWriter;

import junit.framework.*;
import edu.osu.csml.humdrum.*;

/**
 * Basic test of the Barline class
 * @author Daniel McEnnis
 *
 */
public class TestBarline extends TestCase {
	private StringWriter err;
	private TestController stub;
	private Barline test;

	/**
	 * @param arg0
	 */
	public TestBarline(String arg0) {
		super(arg0);
	}
	
	public void setUp(){
		err = new StringWriter();
		stub = new TestController(err);
		test = new Barline(stub);
	}
	
	public void testBasic() throws HumdrumException,HumdrumWarning{
		test.init("=");
		assertTrue(stub.type.contains(Note.class));
		assertTrue(stub.data.get(0) instanceof Barline);
	}
	
	public void testNumberBasic() throws HumdrumException,HumdrumWarning{
		test.init("=1");
		assertTrue(stub.type.contains(Note.class));
		assertTrue(stub.data.get(0) instanceof Barline);
	}
	
	public void testDoubleBarline() throws HumdrumException,HumdrumWarning{
		test.init("==");
		assertTrue(test.isDouble());
	}
	
	public void testGreaterThan() throws HumdrumException,HumdrumWarning{
		test.init("=15");
		Barline test2 = new Barline(stub);
		test2.init("=16");
		assertTrue(test2.isGreater(test));
	}
	
	public void testGreaterThanLetter()  throws HumdrumException,HumdrumWarning{
		test.init("=9a");
		Barline test2 = new Barline(stub);
		test2.init("=9b");
		assertTrue(test2.isGreater(test));
	}
	
	public void testDescriptors() throws HumdrumException,HumdrumWarning{
		test.init("=1:|!:;");
	}
	
	public static Test suite() {
		return new TestSuite(TestBarline.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestBarline.class);
	}
}
