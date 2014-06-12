/*
 * Created on Sep 12, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.interpretation;

import java.io.StringWriter;

import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.SpineEOF;
import edu.osu.csml.humdrum.TestController;
import edu.osu.csml.humdrum.kern.KernException;
import edu.osu.csml.humdrum.EndOfToken;
import edu.osu.csml.humdrum.kern.note.SlurPhraseBeam;
import edu.osu.csml.humdrum.proofCommand.proof;

import junit.framework.*;

/**
 * Basic check of the PhraseLikeCheck interpretation
 * @author Daniel McEnnis
 *
 */
public class TestPhraseLikeCheck extends TestCase {
	private StringWriter err;
	private EventController stub;
	private SlurPhraseBeam phraseTest;
	private PhraseLikeCheck test;
	private proof proof= new proof();

	/**
	 * @param arg0
	 */
	public TestPhraseLikeCheck(String arg0) {
		super(arg0);
	}
	public void setUp() {
		err = new StringWriter();
		stub =
			new EventController(new TestController(err = new StringWriter()));
		test = new PhraseLikeCheck(stub);
	}

	public void testTrivialSlur() throws KernException{
		final String[] data = new String[] { "(7a", "7b)" };
		for (int i = 0; i < data.length; ++i) {
			phraseTest = new SlurPhraseBeam(stub);
			phraseTest.init(data[i]);
			stub.fire(SlurPhraseBeam.class,phraseTest);
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}
	}
	
	public void testTrivialPhrase() throws KernException{
		final String[] data = new String[] { "{7a", "7b}" };
		for (int i = 0; i < data.length; ++i) {
			phraseTest = new SlurPhraseBeam(stub);
			phraseTest.init(data[i]);
			stub.fire(SlurPhraseBeam.class,phraseTest);
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}
	}
	
	public void testTrivialBeam() throws KernException{
		final String[] data = new String[] { "7aL", "7bJ" };
		for (int i = 0; i < data.length; ++i) {
			phraseTest = new SlurPhraseBeam(stub);
			phraseTest.init(data[i]);
			stub.fire(SlurPhraseBeam.class,phraseTest);
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}
	}

	public void testTwoBeams() throws KernException{
		final String[] data = new String[] { "7aLL", "7bJJ" };
		for (int i = 0; i < data.length; ++i) {
			phraseTest = new SlurPhraseBeam(stub);
			phraseTest.init(data[i]);
			stub.fire(SlurPhraseBeam.class,phraseTest);
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}
	}
		

	public void testSimpleSlur() throws KernException{
		final String[] data = new String[] { "(7a","4c", "7b)" };
		for (int i = 0; i < data.length; ++i) {
			phraseTest = new SlurPhraseBeam(stub);
			phraseTest.init(data[i]);
			stub.fire(SlurPhraseBeam.class,phraseTest);
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}
	}

	public void testSimplePhrase() throws KernException{
		final String[] data = new String[] { "{7a","4c", "7b}" };
		for (int i = 0; i < data.length; ++i) {
			phraseTest = new SlurPhraseBeam(stub);
			phraseTest.init(data[i]);
			stub.fire(SlurPhraseBeam.class,phraseTest);
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}
	}

	public void testSimpleBeam() throws KernException{
		final String[] data = new String[] { "7aL","4c", "7bJ" };
		for (int i = 0; i < data.length; ++i) {
			phraseTest = new SlurPhraseBeam(stub);
			phraseTest.init(data[i]);
			stub.fire(SlurPhraseBeam.class,phraseTest);
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}
	}
	
	public void testNestedSlur() throws KernException{
		final String[] data = new String[] { "(7a","(4c", "7b)","4g","4f)" };
		for (int i = 0; i < data.length; ++i) {
			phraseTest = new SlurPhraseBeam(stub);
			phraseTest.init(data[i]);
			stub.fire(SlurPhraseBeam.class,phraseTest);
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}
	}
	
	public void testNestedPhrase() throws KernException{
		final String[] data = new String[] { "{7a","{4c", "7b}","4g}" };
		for (int i = 0; i < data.length; ++i) {
			phraseTest = new SlurPhraseBeam(stub);
			phraseTest.init(data[i]);
			stub.fire(SlurPhraseBeam.class,phraseTest);
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}
	}
	
	public void testNestedBeam() throws KernException{
		final String[] data = new String[] { "7aL","4cL", "7bJ","5dJ" };
		for (int i = 0; i < data.length; ++i) {
			phraseTest = new SlurPhraseBeam(stub);
			phraseTest.init(data[i]);
			stub.fire(SlurPhraseBeam.class,phraseTest);
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}
	}
	
	public void testElidedSlur()throws KernException{
		final String[] data = new String[] { "(7a","&(4c", "7b&)","4g","4f)" };
		for (int i = 0; i < data.length; ++i) {
			phraseTest = new SlurPhraseBeam(stub);
			phraseTest.init(data[i]);
			stub.fire(SlurPhraseBeam.class,phraseTest);
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}
	}
	
	public void testElidedPhrase()throws KernException{
		final String[] data = new String[] { "{7a","&{4c", "7b&}","4g","4f}" };
		for (int i = 0; i < data.length; ++i) {
			phraseTest = new SlurPhraseBeam(stub);
			phraseTest.init(data[i]);
			stub.fire(SlurPhraseBeam.class,phraseTest);
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		if(err.getBuffer().length()>0){
			fail(new String(err.getBuffer()));
		}
	}
	
	public void testUnclosedSlur()throws KernException{
		final String[] data = new String[] { "(7a" };
		for (int i = 0; i < data.length; ++i) {
			phraseTest = new SlurPhraseBeam(stub);
			phraseTest.init(data[i]);
			stub.fire(SlurPhraseBeam.class,phraseTest);
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		if(err.getBuffer().length()==0){
			fail("Should have recognized unclosed slur");
		} else if(!(new String(err.getBuffer())).matches(".*Error.*[Ss]lur.*"+System.getProperty("line.separator"))){
			fail("Should have 2 exceptions:" + new String(err.getBuffer()));
		}
	}
		
	public void testUnclosedPhrase()throws KernException{
		final String[] data = new String[] { "{7a" };
		for (int i = 0; i < data.length; ++i) {
			phraseTest = new SlurPhraseBeam(stub);
			phraseTest.init(data[i]);
			stub.fire(SlurPhraseBeam.class,phraseTest);
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		if(err.getBuffer().length()==0){
			fail("Should have recognized unclosed phrase");
		} else if(!(new String(err.getBuffer())).matches(".*Error.*[Pp]hrase.*"+System.getProperty("line.separator"))){
			fail("Should have 2 exceptions:" + new String(err.getBuffer()));
		}
	}
		
	public void testUnclosedBeam()throws KernException{
		final String[] data = new String[] { "7aL" };
		for (int i = 0; i < data.length; ++i) {
			phraseTest = new SlurPhraseBeam(stub);
			phraseTest.init(data[i]);
			stub.fire(SlurPhraseBeam.class,phraseTest);
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		if(err.getBuffer().length()==0){
			fail("Should have recognized unclosed beam");
		} else if(!(new String(err.getBuffer())).matches(".*Error.*[Bb]eam.*"+System.getProperty("line.separator"))){
			fail("Should have 1 exception:" + new String(err.getBuffer()));
		}
	}
	
	public void testUnclosedMultiSlur()throws KernException{
		final String[] data = new String[] { "(7a","(6b","5d)" };
		for (int i = 0; i < data.length; ++i) {
			phraseTest = new SlurPhraseBeam(stub);
			phraseTest.init(data[i]);
			stub.fire(SlurPhraseBeam.class,phraseTest);
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		if(err.getBuffer().length()==0){
			fail("Should have recognized unclosed beam");
		} else if(!(new String(err.getBuffer())).matches(".*Error.*[Ss]lur.*"+System.getProperty("line.separator"))){
			fail("Should have 2 exceptions:" + new String(err.getBuffer()));
		}
	}
		
	public void testUnclosedMultiPhrase()throws KernException{
		final String[] data = new String[] { "{7a","{6b","5d}" };
		for (int i = 0; i < data.length; ++i) {
			phraseTest = new SlurPhraseBeam(stub);
			phraseTest.init(data[i]);
			stub.fire(SlurPhraseBeam.class,phraseTest);
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		if(err.getBuffer().length()==0){
			fail("Should have recognized unclosed beam");
		} else if(!(new String(err.getBuffer())).matches(".*Error.*[Pp]hrase.*"+System.getProperty("line.separator"))){
			fail("Should have exceptions concerning phrases:" + new String(err.getBuffer()));
		}
	}
		
	public void testUnclosedMultiBeam()throws KernException{
		final String[] data = new String[] { "7aL","6bL","5dJ" };
		for (int i = 0; i < data.length; ++i) {
			phraseTest = new SlurPhraseBeam(stub);
			phraseTest.init(data[i]);
			stub.fire(SlurPhraseBeam.class,phraseTest);
			stub.fire(EndOfToken.class,new EndOfToken());
		}
		stub.fire(SpineEOF.class, new SpineEOF(stub));
		if(err.getBuffer().length()==0){
			fail("Should have recognized unclosed beam");
		} else if(!(new String(err.getBuffer())).matches(".*Error.*[Bb]eam.*"+System.getProperty("line.separator"))){
			fail("Should have error concerning Beams:" + new String(err.getBuffer()));
		}
	}
		
	public static Test suite() {
		return new TestSuite(TestPhraseLikeCheck.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestPhraseLikeCheck.class);
	}
}
