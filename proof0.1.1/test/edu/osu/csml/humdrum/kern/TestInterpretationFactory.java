/*
 * Created on Aug 27, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.TestController;
import edu.osu.csml.humdrum.interpretation.Generic;
import edu.osu.csml.humdrum.kern.interpretation.KeySignature;
import edu.osu.csml.humdrum.proofCommand.proof;

/**
 * Basic Check of KernInterpretationFactory object 
 * @author Daniel McEnnis
 *
 */
public class TestInterpretationFactory extends TestCase {
	private String token;
	private TestController stub = new TestController(new java.io.OutputStreamWriter(System.err));
	private Interpretation test;
	private KernInterpretationFactory kF = new KernInterpretationFactory(stub);
	private proof proof= new proof();

	/**
	 * @param arg0
	 */
	public TestInterpretationFactory(String arg0) {
		super(arg0);
	}

	public void setUp() {
		stub.clear();
	}

	public void testCreateKeySignature() throws HumdrumException, HumdrumWarning {
		test = kF.parseInterpretation("*k[b-]", stub);
		assertTrue(test instanceof KeySignature);
	}

	public void testGeneric() throws HumdrumException, HumdrumWarning {
		test = kF.parseInterpretation("*", stub);
		assertTrue(test instanceof Generic);
	}

	public static Test suite() {
		return new TestSuite(TestInterpretationFactory.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestInterpretationFactory.class);
	}

}
