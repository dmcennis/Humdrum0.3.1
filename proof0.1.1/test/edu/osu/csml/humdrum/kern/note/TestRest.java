/*
 * Created on Aug 27, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.note;

import junit.framework.*;
import edu.osu.csml.humdrum.*;
import edu.osu.csml.humdrum.kern.*;

/**
 * Basic Test of the Rest class
 * @author Daniel McEnnis
 *
 */
public class TestRest extends TestCase {
	private TestController stub = new TestController(new java.io.OutputStreamWriter(System.err));
	private Rest rest = new Rest(stub);
	private Duration test;

	/**
	 * @param arg0
	 */
	public TestRest(String arg0) {
		super(arg0);
	}

	public void setUp() {
		stub.data.clear();
		stub.rec.clear();
		stub.type.clear();
	}

	public void testBasic() throws HumdrumException, HumdrumWarning {
		rest.init("2r");
		assertNotNull(stub.data);
		assertNotNull(stub.type);
		Rest rest = (Rest)stub.data.get(stub.data.size()-1);
		Duration data= (Duration)stub.data.get(stub.data.size()-2);
		assertEquals(0.5, data.getDuration(), 0.0001);
	}

	public void testBadRest() throws HumdrumException, HumdrumWarning{
		try{
			rest.init("r2r");
			fail("Should complain about invlaid note");
		}
		catch(KernException e)
		{
			;
		}
	}	
	
	public void testEqualsTrue()throws HumdrumException, HumdrumWarning{
		rest.init("2r");
		Rest rest2 = new Rest(stub);
		rest2.init("2r");
		assertTrue(rest.equals(rest2));
	}
	
	public void testEqualsFalse() throws HumdrumException, HumdrumWarning{
		rest.init("2r");
		Rest rest2 = new Rest(stub);
		rest2.init("4r");
		assertFalse(rest.equals(rest2));
	}
	
	public void testClone()throws HumdrumException, HumdrumWarning{
		rest.init("2r");
		Rest rest2 = (Rest)rest.clone(stub);
		assertTrue(rest.equals(rest2));
	}

	public static Test suite() {
		return new TestSuite(TestRest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestRest.class);
	}
}
