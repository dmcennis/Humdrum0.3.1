/*
 * Created on Aug 27, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.interpretation;

import junit.framework.*;
import edu.osu.csml.humdrum.*;
import edu.osu.csml.humdrum.kern.*;
import edu.osu.csml.humdrum.kern.note.Pitch;
import edu.osu.csml.humdrum.proofCommand.proof;

/**
 * Basic test of the KeySignature interpretation
 * @author Daniel McEnnis
 *
 */
public class TestKeySignature extends TestCase {
	String token;
	KeySignature test;
	Pitch note;
	TestController stub = new TestController(new java.io.OutputStreamWriter(System.err));
	private proof proof= new proof();

	/**
	 * @param arg0
	 */
	public TestKeySignature(String arg0) {
		super(arg0);
	}

	public void setUp() {
		note = new Pitch(stub);
		stub.data.clear();
		stub.rec.clear();
		stub.type.clear();
	}
	public void testCreationTrivial() throws KernException, KernWarning {
		token = "*k[]";
		test = new KeySignature(token, stub);
	}

	public void testCreationOneSharp() throws KernException, KernWarning {
		token = "*k[f#]";
		test = new KeySignature(token, stub);
	}

	public void testCreationMultiSharp() throws KernException, KernWarning {
		token = "*k[f#c#]";
		test = new KeySignature(token, stub);
	}

	public void testCreationOneFlat() throws KernException, KernWarning {
		token = "*k[b-]";
		test = new KeySignature(token, stub);
	}

	public void testCreationMultiFlat() throws KernException, KernWarning {
		token = "*k[b-e--]";
		test = new KeySignature(token, stub);
	}

	public void testCreationMixed() throws KernException, KernWarning {
		token = "*k[e#b-]";
		test = new KeySignature(token, stub);
	}
	public void testCreationInvlaidNote() throws KernWarning {
		try {
			token = "*k[-a-b]";
			test = new KeySignature(token, stub);
			fail("Should complain about invalid key signature");
		} catch (KernException e) {
			;
		}
	}

	public void testFireSingleSharp() throws KernException {
		try {
			token = "*k[f#]";
			test = new KeySignature(token, stub);
			note.init("f");
			test.fire(Pitch.class,note);
			fail("Should throw a warning");
		} catch (KernWarning e) {
			;
		}
	}

	public void testFireMultiFlat() throws KernException {
		try {
			token = "*k[e#b--]";
			test = new KeySignature(token, stub);
			note.init("b");
			test.fire(Pitch.class,note);
			fail("Should throw a warning");
		} catch (KernWarning e) {
			;
		}
	}

	public void testFireDoubleSharp() throws KernException {
		try {
			token = "*k[e#b--f##]";
			test = new KeySignature(token, stub);
			note.init("f#");
			test.fire(Pitch.class,note);
		} catch (KernWarning e) {
			fail("Should only warn if no accidental is given");
		}
	}

	public void testRegistered() throws KernException, KernWarning {
		token = "*k[f#c#]";
		test = new KeySignature(token,stub);
		Class type = (Class)stub.type.firstElement();
		assertTrue(type == Pitch.class);
		super.assertSame(stub.rec.firstElement(),test);
	}
	
	public void testEqualsTrue() throws KernException, KernWarning {
		test = new KeySignature("*k[f#c#]",stub);
		KeySignature test2 = new KeySignature("*k[f#c#]",stub);
		assertTrue(test.equals(test2));
	}
	
	public void testEqualsFalse() throws KernException, KernWarning {
		test = new KeySignature("*k[f#c#]",stub);
		KeySignature test2= new KeySignature("*k[f#c#g#]",stub);
		assertFalse(test.equals(test2));
	}
	
	public void testClone() throws KernException, KernWarning {
		test = new KeySignature("*k[f#c#]",stub);
		KeySignature test2 = (KeySignature)test.clone(stub);
		assertTrue(test.equals(test2));
	}

	public static Test suite() throws KernException, KernWarning {
		return new TestSuite(TestKeySignature.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestKeySignature.class);
	}
}
