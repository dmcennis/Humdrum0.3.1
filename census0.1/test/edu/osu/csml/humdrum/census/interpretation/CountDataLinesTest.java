/**
 * 
 */
package edu.osu.csml.humdrum.census.interpretation;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

import edu.osu.csml.humdrum.Document;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.Spine;
import edu.osu.csml.humdrum.SpineFactory;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.KernInterpretationFactory;
import edu.osu.csml.humdrum.kern.KernNoteFactory;

/**
 * @author user
 *
 */
public class CountDataLinesTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		CountDataLines.init();
		SpineFactory.register("\\*\\*kern", new Spine(new KernInterpretationFactory(new EventController()),new KernNoteFactory(),new EventController()));
		StringWriter err = new StringWriter();
	}

	/**
	 * Test method for {@link edu.osu.csml.humdrum.census.interpretation.CountDataLines#output()}.
	 */
	@Test
	public void testEmpty() throws FileNotFoundException{
		Document document = new Document(10);
		document.parseDocument(
					new java.io.FileReader("test"+File.separator+"empty.hum"),
					new OutputStreamWriter(System.out));
		View[] checks = document.getGlobalChecks();
		for(int i=0;i<checks.length;++i){
			if(checks[i] instanceof CountDataLines){
				String outs = ((CountDataLines)checks[i]).output();
				if(outs.matches("[^0-9]*0")){
					
				}else{
					fail(outs);
				}
			}
		}
	}

	@Test
	public void testGeneric1Spine() throws FileNotFoundException{
		Document document = new Document(10);
		document.parseDocument(
					new java.io.FileReader("test"+File.separator+"generic1spine.hum"),
					new OutputStreamWriter(System.out));
		View[] checks = document.getGlobalChecks();
		for(int i=0;i<checks.length;++i){
			if(checks[i] instanceof CountDataLines){
				String outs = ((CountDataLines)checks[i]).output();
				if(outs.matches("[^0-9]*4"));
			}
		}
	}

	@Test
	public void testGeneric2Spine() throws FileNotFoundException{
		Document document = new Document(10);
		document.parseDocument(
					new java.io.FileReader("test"+File.separator+"generic2spine.hum"),
					new OutputStreamWriter(System.out));
		View[] checks = document.getGlobalChecks();
		for(int i=0;i<checks.length;++i){
			if(checks[i] instanceof CountDataLines){
				String outs = ((CountDataLines)checks[i]).output();
				if(outs.matches("[^0-9]*3"));
			}
		}
	}

	@Test
	public void testInterpretation() throws FileNotFoundException{
		Document document = new Document(10);
		document.parseDocument(
					new java.io.FileReader("test"+File.separator+"interpretation.krn"),
					new OutputStreamWriter(System.out));
		View[] checks = document.getGlobalChecks();
		for(int i=0;i<checks.length;++i){
			if(checks[i] instanceof CountDataLines){
				String outs = ((CountDataLines)checks[i]).output();
				if(outs.matches("[^0-9]*6")){
					
				}else{
					fail(outs);
				}
			}
		}
	}

	@Test
	public void testMixedWithBarlines() throws FileNotFoundException{
		Document document = new Document(10);
		document.parseDocument(
					new java.io.FileReader("test"+File.separator+"mixedwithbarlines.krn"),
					new OutputStreamWriter(System.out));
		View[] checks = document.getGlobalChecks();
		for(int i=0;i<checks.length;++i){
			if(checks[i] instanceof CountDataLines){
				String outs = ((CountDataLines)checks[i]).output();
				if(outs.matches("[^0-9]*7")){
					
				}else{
					fail(outs);
				}
			}
		}
	}

	@Test
	public void testOverlap() throws FileNotFoundException{
		Document document = new Document(10);
		document.parseDocument(
					new java.io.FileReader("test"+File.separator+"overlap.krn"),
					new OutputStreamWriter(System.out));
		View[] checks = document.getGlobalChecks();
		for(int i=0;i<checks.length;++i){
			if(checks[i] instanceof CountDataLines){
				String outs = ((CountDataLines)checks[i]).output();
				if(outs.matches("[^0-9]*3")){
					
				}else{
					fail(outs);
				}
			}
		}
	}

	@Test
	public void testAllTypes() throws FileNotFoundException{
		Document document = new Document(10);
		document.parseDocument(
					new java.io.FileReader("test"+File.separator+"alltypes.krn"),
					new OutputStreamWriter(System.out));
		View[] checks = document.getGlobalChecks();
		for(int i=0;i<checks.length;++i){
			if(checks[i] instanceof CountDataLines){
				String outs = ((CountDataLines)checks[i]).output();
				if(outs.matches("[^0-9]*13")){
					
				}else{
					fail(outs);
				}
			}
		}
	}

}
