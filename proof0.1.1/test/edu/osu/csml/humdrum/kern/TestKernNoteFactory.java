/*
 * Created on Aug 20, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern;

import model.NullNote;
import junit.framework.*;
import edu.osu.csml.humdrum.*;
import edu.osu.csml.humdrum.kern.note.Multi;
import edu.osu.csml.humdrum.kern.note.Rest;
import edu.osu.csml.humdrum.kern.note.Single;
import edu.osu.csml.humdrum.proofCommand.proof;


/**
 * Basic check of the KernNoteFactory class
 * @author Daniel McEnnis
 *
 */
public class TestKernNoteFactory extends TestCase {
	KernNoteFactory parser = new KernNoteFactory();
	TestController stub = new TestController(new java.io.OutputStreamWriter(System.err));
	private proof proof= new proof();

	/**
	 * @param arg0
	 */
	public TestKernNoteFactory(String arg0) {
		super(arg0);
	}
	
	public void setUp(){
		stub.clear();
	}

	public void testMakeSingleNote() throws HumdrumException, HumdrumWarning {
		assertEquals(Single.class,
			parser.getNote("7a",stub
			).getClass());
	}

	public void testMakeRestNote() throws HumdrumException, HumdrumWarning {
		assertEquals(
			parser.getNote("7r", stub).getClass(),
			Rest.class);
	}

	public void testMakeMultiNote() throws HumdrumException, HumdrumWarning {
		assertEquals(
			parser.getNote("7a 7g", stub).getClass(),
			Multi.class);
	}

	public void testNullNote() throws HumdrumException, HumdrumWarning {
		assertEquals(
			parser.getNote(".", stub).getClass(),
			NullNote.class);
	}

	public static Test suite() {
		return new TestSuite(TestKernNoteFactory.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestKernNoteFactory.class);
	}
}
