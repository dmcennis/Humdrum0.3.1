/*
 * Created on Mar 1, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.interpretation;

import edu.osu.csml.humdrum.*;
import edu.osu.csml.humdrum.kern.*;
import edu.osu.csml.humdrum.kern.note.Duration;
import edu.osu.csml.humdrum.EndOfToken;
import edu.osu.csml.humdrum.proofCommand.proof;
import junit.framework.*;

/**
 * Basic test of the DurationSum Interpretation
 * 
 * @author Daniel McEnnis
 */
public class TestDurationSum extends TestCase {
	DurationSum test;
	Duration dur;
	EndOfToken eot;
	EventController master;
	TestController root;
	private proof proof= new proof();
	

	/**
	 * @param arg0
	 */
	public TestDurationSum(String arg0) {
		super(arg0);
	}
	
	public void setUp(){
		root = new TestController(0,0);
		master = new EventController(root);
	}
	
	public void testBasicCreation(){
		test = new DurationSum(master);
	}
	
	public void testEqualsNoDangles() throws KernException, KernWarning{
		test = new DurationSum(master);

		(dur = new Duration(master)).init("4");
		master.fire(Duration.class,dur);
		master.fire(EndOfToken.class,new EndOfToken());

		master = new EventController(root);
		DurationSum test2 = new DurationSum(master);

		(dur = new Duration(master)).init("4");
		master.fire(Duration.class,dur);
		master.fire(EndOfToken.class,new EndOfToken());
		
		assertEquals("These should be equal, but are returning false",test,test2);
	}
	public void testEqualsWithDangles() throws KernException, KernWarning{
		test = new DurationSum(master);
		(dur = new Duration(master)).init("4");
		master.fire(Duration.class,dur);
		master.fire(EndOfToken.class,new EndOfToken());
		(dur = new Duration(master)).init("4");
		master.fire(Duration.class,dur);
		
		master = new EventController(root);
		DurationSum test2 = new DurationSum(master);
		(dur = new Duration(master)).init("4");
		master.fire(Duration.class,dur);
		master.fire(EndOfToken.class,new EndOfToken());
		(dur = new Duration(master)).init("4");
		master.fire(Duration.class,dur);
		assertTrue("These should be equal, but are returning false",test.equals(test2));
	}
	
	
	
	public void testNotEqualsStraight() throws KernException, KernWarning{
		test = new DurationSum(master);
		(dur = new Duration(master)).init("4");
		master.fire(Duration.class,dur);
		master.fire(EndOfToken.class,new EndOfToken());
		DurationSum test2 = new DurationSum(master);
		assertFalse("Should be false, but is reporting true",test.equals(test2));
	}
	
	public void testNotEqualsDangles() throws KernException, KernWarning{
		test = new DurationSum(master);
		(dur = new Duration(master)).init("4");
		master.fire(Duration.class,dur);
		master.fire(EndOfToken.class,new EndOfToken());
		(dur = new Duration(master)).init("4");
		master.fire(Duration.class,dur);
		master = new EventController(root);
		DurationSum test2 = new DurationSum(master);
		(dur = new Duration(master)).init("4");
		master.fire(Duration.class,dur);
		master.fire(EndOfToken.class,new EndOfToken());
		assertFalse("Should be false, but is reporting true",test.equals(test2));
	}
	
	public void testGetDuration() throws KernException, KernWarning{
		test = new DurationSum(master);
		(dur = new Duration(master)).init("4");
		master.fire(Duration.class,dur);
		master.fire(EndOfToken.class,new EndOfToken());
		(dur = new Duration(master)).init("4");
		master.fire(Duration.class,dur);
		master.fire(EndOfToken.class,new EndOfToken());
		assertEquals(0.50,test.getDuration(),0.001);
	}
	
	public void testGetSomeDuration() throws KernException, KernWarning{
		test = new DurationSum(master);
		(dur = new Duration(master)).init("4");
		master.fire(Duration.class,dur);
		assertEquals("Duration should be 0.0, not "+test.getDuration() +".  The duration should not yet be added in",0.0,test.getDuration(),0.001);
	}
	
	public void testFire(){
		test = new DurationSum(master);
		master.fire(EndOfToken.class, new EndOfToken());
		assertEquals(2,root.data.size());
		assertTrue("Problem with EndOfToken - should be registered with parent classes but isn't",root.data.get(1) instanceof EndOfToken);
		assertTrue("DurationSum is not firing the correct Model",root.data.get(0) instanceof DurationSum);
	}
	
	public void testClone() throws KernException, KernWarning{
		test = new DurationSum(master);
		(dur = new Duration(master)).init("4");
		master.fire(Duration.class,dur);
		master.fire(EndOfToken.class, new EndOfToken());
		DurationSum test2 = (DurationSum)test.clone(master);
		assertEquals("Cloned objects should be equal",test,test2);
	}
	
	public void testHasDuration() throws KernException, KernWarning{
		test = new DurationSum(master);
		(dur = new Duration(master)).init("4");
		master.fire(Duration.class,dur);
		assertTrue(test.hasDuration());
	}
	
	public void testHasNotDuration() throws KernException, KernWarning{
		test = new DurationSum(master);
		assertFalse(test.hasDuration());
	}

	public static Test suite() throws KernException, KernWarning {
		return new TestSuite(TestDurationSum.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestDurationSum.class);
	}
}
