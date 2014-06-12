/*
 * Created on Sep 12, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.note;

import java.io.StringWriter;
import java.util.Arrays;

import edu.osu.csml.humdrum.TestController;
import junit.framework.TestCase;

/**
 * Basic tests of the StemBow class
 * @author Daniel McEnnis
 *
 */
public class TestStemBow extends TestCase {
	TestController stub;
	StringWriter err;
	StemBow test;
	final int[] check = new int[]{StemBow.UP_STEM,StemBow.DOWN_STEM,StemBow.UP_BOW,StemBow.DOWN_BOW,StemBow.BREATH,StemBow.NONE};
	boolean[] data = new boolean[6];
	/**
	 * @param arg0
	 */
	public TestStemBow(String arg0) {
		super(arg0);
	}

	public void setUp(){
		stub = new TestController(err = new StringWriter());
		test = new StemBow(stub);
		Arrays.fill(data,false);
	}
	
	public void testStemUp(){
		test.init("4a/");
		data[0] = true;
		verifyStructure(data);
	}
	
	public void testStemDown(){
		test.init("4a\\");
		data[1] = true;
		verifyStructure(data);
	}
	
	public void testBowUp(){
		test.init("4av");
		data[2]=true;
		verifyStructure(data);
	}
	
	public void testBowDown(){
		test.init("4au");
		data[3]=true;
		verifyStructure(data);
	}
	
	public void testBreath(){
		test.init("4a;");
		data[4] = true;
		verifyStructure(data);
	}
	
	public void testNone(){
		test.init("4a");
		data[5] = true;
		verifyStructure(data);
	}
	
	public void testBadDuplicate(){
		test.init("4a//");
		assertTrue("Duplicate entry not detected",err.getBuffer().length()>0);
		assertTrue("Found error other than duplicate entries: "+new String(err.getBuffer()),(new String(err.getBuffer())).matches(".*Error.*[dD]uplicate.*"+System.getProperty("line.separator")));
	}
	
	public void testClone(){
		Object test2 = test.clone(stub);
		assertEquals("Cloned object should be equal",test,test2);
	}
	
	public void testEqual(){
		StemBow test2 = new StemBow(stub);
		test.init("4a/");
		test2.init("5b/");
		assertEquals("Identical stem marks should be equal",test,test2);
	}
	
	public void testNotEqual(){
		StemBow test2 = new StemBow(stub);
		test.init("4a/");
		test2.init("4a;");
		assertFalse("Stem mark and breath marks are not equal",test.equals(test2));
	}

	public static void main(String[] args) {
	}
	
	public void verifyStructure(boolean[] data){
		for(int i =0;i<data.length;++i){
			if(data[i]){
				assertTrue("Item " + i + " should be present",test.isPresent(check[i]));
			}else{
				assertFalse("Item " + i + " should not be present",test.isPresent(check[i]));				
			}
		}
	}
}
