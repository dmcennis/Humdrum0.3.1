/*
 * Created on Aug 21, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.note;

import junit.framework.*;
import edu.osu.csml.humdrum.TestController;
import edu.osu.csml.humdrum.kern.*;

/**
 * Basic test of the Pitch class
 * @author Daniel McEnnis
 *
 */
public class TestPitch extends TestCase {
	private String token;
	private Pitch temp;
	private TestController stub = new TestController(new java.io.OutputStreamWriter(System.err));

	public void setUp() {
		stub.clear();
		temp = new Pitch(stub);
	}

	/**
	 * @param arg0
	 */
	public TestPitch(String arg0) {
		super(arg0);
	}

	public void test_a_Parse() throws KernException, KernWarning {
		token = "7a";
		temp.init(token);
		assertEquals(temp.getNote(), "a");
		assertEquals(temp.getPitch(), -3);
	}

	public void test_b_Parse() throws KernException, KernWarning {
		token = "7b";
		temp.init(token);
		assertEquals(temp.getNote(), "b");
		assertEquals(temp.getPitch(), -1);
	}

	public void test_c_Parse() throws KernException, KernWarning {
		token = "7c";
		temp.init(token);
		assertEquals(temp.getNote(), "c");
		assertEquals(temp.getPitch(), 0);
	}

	public void test_d_Parse() throws KernException, KernWarning {
		token = "7d";
		temp.init(token);
		assertEquals(temp.getNote(), "d");
		assertEquals(temp.getPitch(), 2);
	}

	public void test_e_Parse() throws KernException, KernWarning {
		token = "7e";
		temp.init(token);
		assertEquals(temp.getNote(), "e");
		assertEquals(temp.getPitch(), 4);
	}

	public void test_f_Parse() throws KernException, KernWarning {
		token = "7f";
		temp.init(token);
		assertEquals(temp.getNote(), "f");
		assertEquals(temp.getPitch(), 5);
	}

	public void test_g_Parse() throws KernException, KernWarning {
		token = "7g";
		temp.init(token);
		assertEquals(temp.getNote(), "g");
		assertEquals(temp.getPitch(), 7);
	}

	public void test_A_Parse() throws KernException, KernWarning {
		token = "7A";
		temp.init(token);
		assertEquals(temp.getNote(), "A");
		assertEquals(temp.getPitch(), -15);
	}

	public void test_B_Parse() throws KernException, KernWarning {
		token = "7B";
		temp.init(token);
		assertEquals(temp.getNote(), "B");
		assertEquals(temp.getPitch(), -13);
	}

	public void test_C_Parse() throws KernException, KernWarning {
		token = "7C";
		temp.init(token);
		assertEquals(temp.getNote(), "C");
		assertEquals(temp.getPitch(), -12);
	}

	public void test_D_Parse() throws KernException, KernWarning {
		token = "7D";
		temp.init(token);
		assertEquals(temp.getNote(), "D");
		assertEquals(temp.getPitch(), -10);
	}

	public void test_E_Parse() throws KernException, KernWarning {
		token = "7E";
		temp.init(token);
		assertEquals(temp.getNote(), "E");
		assertEquals(temp.getPitch(), -8);
	}

	public void test_F_Parse() throws KernException, KernWarning {
		token = "7F";
		temp.init(token);
		assertEquals(temp.getNote(), "F");
		assertEquals(temp.getPitch(), -7);
	}

	public void test_G_Parse() throws KernException, KernWarning {
		token = "7G";
		temp.init(token);
		assertEquals(temp.getNote(), "G");
		assertEquals(temp.getPitch(), -5);
	}

	public void testSingleSharpParse() throws KernException, KernWarning {
		token = "7G#";
		temp.init(token);
		assertEquals("G",temp.getNote());
		assertEquals(temp.getPitch(), -4);
	}

	public void testDoubleSharpParse() throws KernException, KernWarning {
		token = "7G##";
		temp.init(token);
		assertEquals("G",temp.getNote());
		assertEquals(-3,temp.getPitch());
		assertEquals(2,temp.getAccidental());
	}

	public void testFlatParse() throws KernException, KernWarning {
		token = "7G-";
		temp.init(token);
		assertEquals("G",temp.getNote());
		assertEquals( -6,temp.getPitch());
		assertEquals(-1,temp.getAccidental());
	}

	public void testDoubleFlatParse() throws KernException, KernWarning {
		token = "7G--";
		temp.init(token);
		assertEquals("G",temp.getNote());
		assertEquals(-7,temp.getPitch());
		assertEquals(-2,temp.getAccidental());
	}

	public void testDoubleUpperLetterParse()
		throws KernException, KernWarning {
		token = "7GG";
		temp.init(token);
		assertEquals("GG",temp.getNote());
		assertEquals(-17,temp.getPitch());
	}
	
	public void testBadPitch() throws KernWarning{
		try{
			token="a6b";
			temp.init(token);
			System.out.println("testBadPitch - Should have complained about multiple pitches");
			fail();
		}
		catch(KernException e){
			;
		}
	}
	
	public void testEqualsTrue() throws KernWarning,KernException{
		token ="7A";
		String token2="9A";
		temp.init(token);
		Pitch temp2 = new Pitch(stub);
		temp2.init(token2);
		assertTrue(temp.equals(temp2));
	}
	
	public void testEqualsFalse() throws KernWarning,KernException{
		temp.init("7A");
		Pitch temp2 = new Pitch(stub);
		temp2.init("7AA");
		assertFalse(temp.equals(temp2));
	}
	
	public void testClone() throws KernWarning, KernException{
		temp.init("7A");
		Pitch temp2 = (Pitch)temp.clone(new TestController(new java.io.OutputStreamWriter(System.err)));
		assertTrue(temp.equals(temp2));
	}

	public static Test suite() {
		return new TestSuite(TestPitch.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestPitch.class);
	}
}
