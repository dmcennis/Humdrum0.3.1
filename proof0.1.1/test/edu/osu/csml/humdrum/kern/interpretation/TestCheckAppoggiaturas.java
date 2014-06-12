/*
 * Created on Sep 30, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.interpretation;

import java.io.StringWriter;

import junit.framework.*;
import edu.osu.csml.humdrum.*;
import edu.osu.csml.humdrum.EndOfToken;
import edu.osu.csml.humdrum.kern.*;
import edu.osu.csml.humdrum.kern.note.*;
import edu.osu.csml.humdrum.proofCommand.proof;

/**
 * Basic check of CheckAppoggiaturas class 
 * @author Daniel McEnnis
 *
 */
public class TestCheckAppoggiaturas extends TestCase {
	EventController stub;
	Duration el;
	CheckAppoggiatura test;
	StringWriter err;
	private proof proof= new proof();

	/**
	 * @param arg0
	 */
	public TestCheckAppoggiaturas(String arg0) {
		super(arg0);
	}

	public void setUp() {
		stub =
			new EventController(
				new TestController(err = new StringWriter()));
		test = new CheckAppoggiatura(stub);
	}

	public void testNull() throws KernException, KernWarning {
		(el = new Duration(stub)).init("4a");
		stub.fire(Duration.class,el);
		stub.fire(EndOfToken.class, new EndOfToken());
		(el = new Duration(stub)).init("2b");
		stub.fire(Duration.class,el);
		stub.fire(EndOfToken.class, new EndOfToken());
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		assertEquals("No errors should be reported",0,err.getBuffer().length());
	}

	public void testBasic() throws KernException, KernWarning {
		(el = new Duration(stub)).init("4aP");
		stub.fire(Duration.class,el);
		stub.fire(EndOfToken.class, new EndOfToken());
		(el = new Duration(stub)).init("2bp");
		stub.fire(Duration.class,el);
		stub.fire(EndOfToken.class, new EndOfToken());
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		assertEquals("No error should have been reported",0,err.getBuffer().length());
	}
	
	public void testElongated() throws KernException, KernWarning {
	(el = new Duration(stub)).init("4aP");
	stub.fire(Duration.class,el);
	stub.fire(EndOfToken.class, new EndOfToken());
	(el = new Duration(stub)).init("4aP");
	stub.fire(Duration.class,el);
	stub.fire(EndOfToken.class, new EndOfToken());
	(el = new Duration(stub)).init("2bp");
	stub.fire(Duration.class,el);
	stub.fire(EndOfToken.class, new EndOfToken());
	stub.fire(SpineEOF.class, new SpineEOF(stub));
	assertEquals("No error should have been reported",0,err.getBuffer().length());
}

	public void testUnEndedBasic() throws KernException, KernWarning {
			(el = new Duration(stub)).init("4aP");
			stub.fire(Duration.class,el);
			stub.fire(EndOfToken.class, new EndOfToken());
			stub.fire(SpineEOF.class, new SpineEOF(stub));
			assertTrue("An error should have been reported",err.getBuffer().length()>0);
			assertTrue("Should be an error, not a warning",(new String(err.getBuffer())).matches(".*Error.*"+System.getProperty("line.separator")));
	}
	
	public void testIncompleteBasic()throws KernException,KernWarning{
			(el = new Duration(stub)).init("4aP");
			stub.fire(Duration.class,el);
			stub.fire(EndOfToken.class, new EndOfToken());
			(el = new Duration(stub)).init("4a");
			stub.fire(Duration.class,el);
			stub.fire(EndOfToken.class, new EndOfToken());
		assertTrue("An error should have been reported",err.getBuffer().length()>0);
		assertTrue("Should be an error, not a warning:"+err.getBuffer(),(new String(err.getBuffer())).matches(".*Error.*"+System.getProperty("line.separator")));
	}
	
	public void testBareModifiedNote()throws KernException, KernWarning{
			(el = new Duration(stub)).init("4ap");
			stub.fire(Duration.class,el);
			stub.fire(EndOfToken.class, new EndOfToken());
			assertTrue("An error should have been reported",err.getBuffer().length()>0);
			assertTrue("Should be an error, not a warning:"+err.getBuffer(),(new String(err.getBuffer())).matches(".*Error.*"+System.getProperty("line.separator")));
	}
	
	public void testTooManyModified() throws KernException,KernWarning{
			(el = new Duration(stub)).init("4aP");
			stub.fire(Duration.class,el);
			stub.fire(EndOfToken.class, new EndOfToken());
			(el = new Duration(stub)).init("2bp");
			stub.fire(Duration.class,el);
			stub.fire(EndOfToken.class, new EndOfToken());
			(el = new Duration(stub)).init("2bp");
			stub.fire(Duration.class,el);
			stub.fire(EndOfToken.class, new EndOfToken());
			stub.fire(SpineEOF.class, new SpineEOF(stub));
			assertTrue("An error should have been reported",err.getBuffer().length()>0);
			assertTrue("Should be an error, not a warning:"+err.getBuffer(),(new String(err.getBuffer())).matches(".*Error.*"+System.getProperty("line.separator")));
	}

	public static Test suite() throws KernException, KernWarning {
		return new TestSuite(TestCheckAppoggiaturas.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestCheckAppoggiaturas.class);
	}
}
