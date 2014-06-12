/*
 * Created on Aug 30, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.*;

/**
 * Provide basic testing of the Document class
 * @author Daniel McEnnis
 *
 */
public class TestDocument extends TestCase {
	Document test;
	StringReader demo;
	FileReader testLoader;
	StringWriter err;
	String prefix = "test"+File.separator;

	/**
	 * @param arg0
	 */
	public TestDocument(String arg0) {
		super(arg0);
	}
	
	public void setUp(){
		err  = new StringWriter();
		test =new Document();
	}
	
	public void testGetLineNumberDefault(){
		test.parseDocument(new StringReader(""),err);
		assertEquals(0,test.master.getLineNumber());
		
	}

	public void testGetLineMeaningful() throws java.io.IOException,HumdrumWarning,HumdrumException{
		test.parseDocument(new FileReader(prefix+"GetLineNumberComplex.hum"),new java.io.OutputStreamWriter(System.err));
		assertEquals(63,test.master.getLineNumber());
	}

	public void testGetSpineDefault(){
		test.parseDocument(new StringReader(""),new java.io.OutputStreamWriter(System.err));
		assertEquals(1,test.master.getSpineNumber());
	}
	
	public void testReadHeaderBasic()throws java.io.IOException,HumdrumException,HumdrumWarning{
		test.parseDocument(new FileReader(prefix+"CommentOnlytest.hum"),err);
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}
	}
	
	
	public void testReadBodySingleSpineBasic()throws java.io.IOException,HumdrumException,HumdrumWarning{
		test.parseDocument(new FileReader(prefix+"DeclareGeneric.hum"),err);
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}

	}
	
	public void testReadBodySingleSpineComment()throws java.io.IOException,HumdrumException,HumdrumWarning{
		test.parseDocument(new FileReader(prefix+"CommentsInBody.hum"),err);
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}

	}
	public void testReadBodySingleSpineInterpretation()throws java.io.IOException,HumdrumException,HumdrumWarning{
		test.parseDocument(new FileReader(prefix+"GenericInterpretation.hum"),err);
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}

	}
	
	public void testReadBodySingleSpineData()throws java.io.IOException,HumdrumException,HumdrumWarning{
		test.parseDocument(new FileReader(prefix+"DataInGeneric.hum"),err);
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}

		
	}
	
	public void testReadBodyMultiSpineBasic()throws java.io.IOException,HumdrumException,HumdrumWarning{
		test.parseDocument(new FileReader(prefix+"TwoGeneric.hum"),err);
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}

	}
	
	public void testUnevenEndOfSpine()throws java.io.IOException,HumdrumException,HumdrumWarning{
		test.parseDocument(new FileReader(prefix+"TwoGenSplitTermination.hum"),err);
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}

	}
	
	public void testInterpretationBeforeDeclaration()throws java.io.IOException,HumdrumException,HumdrumWarning{
		test.parseDocument(new FileReader(prefix+"InterpretationsBeforeDeclarations.hum"),err);
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}

	}
		
	public void testReadBodyMultiSpineData()throws java.io.IOException,HumdrumException,HumdrumWarning{
		test.parseDocument(new FileReader(prefix+"2KernInit.krn"),err);
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}
	}
		
	public void testReadBodyMultiSpineNew()throws java.io.IOException,HumdrumException,HumdrumWarning{
		test.parseDocument(new FileReader(prefix+"TestNew.krn"),err);
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}

	}
	
	public void testReadBodyMultiSpineExchangeLeft()throws java.io.IOException,HumdrumException,HumdrumWarning{
			test.parseDocument(new FileReader(prefix+"TestExchangeLeft.krn"),err);
		if(err.getBuffer().length()==0){
			fail("Should have complained about invalid key signature at line 6, spine 1");
		}
		String tmp = new String(err.getBuffer());
		if(!tmp.matches(".*Warning.*line.*6.*spine.*1.*"+System.getProperty("line.separator"))){
			fail("Should be warning on line 6, spine 1:" + err.getBuffer());
		}
	}
	
	public void testReadBodyMultiSpineExchangeRight()throws java.io.IOException,HumdrumException,HumdrumWarning{
		test.parseDocument(new FileReader(prefix+"TestExchangeRight.krn"),err);
		if(err.getBuffer().length()==0){
			fail("Should have complained about invalid key signature at line 6, spine 2");
		}
		String tmp = new String(err.getBuffer());
		if(!tmp.matches(".*Warning.*line.*8.*spine.*2.*"+System.getProperty("line.separator"))){
			fail("Should be warning on line 8, spine 2:" + err.getBuffer());
		}
	}
	
	public void testReadBodyMultiSpineJoin2()throws java.io.IOException,HumdrumException,HumdrumWarning{
		test.parseDocument(new FileReader(prefix+"MergeTest.krn"),err);
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}
	}
	
	public void testReadBodyMultiSpineJoin3()throws java.io.IOException,HumdrumException,HumdrumWarning{
		test.parseDocument(new FileReader(prefix+"MergeTest3Spine.krn"),err);
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}
	}
	
	public void testJoinWarning()throws java.io.IOException,HumdrumException,HumdrumWarning{
		test.parseDocument(new FileReader(prefix+"MergeTestWarning.krn"),err);
		if(err.getBuffer().length()==0){
			fail("Should warn about incompatible interpretations: " + err.getBuffer());
		}
		else if(! (new String(err.getBuffer()).matches(".*Warning.*incompatible interpretations.*"+System.getProperty("line.separator")))){
			fail("Expected warning about interpretations, but found: " + err.getBuffer());
		}		
	}
	
	public void testSuppressedWarnings()throws java.io.IOException,HumdrumException,HumdrumWarning{
		test = new Document(10);
		test.parseDocument(new FileReader(prefix+"MergeTestWarning.krn"),err);
		if(err.getBuffer().length()!=0){
			fail("Warnings should be suppressed" + err.getBuffer());
		}
	}
	
	public static Test suite() {
		return new TestSuite(TestDocument.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestDocument.class);
	}
}
