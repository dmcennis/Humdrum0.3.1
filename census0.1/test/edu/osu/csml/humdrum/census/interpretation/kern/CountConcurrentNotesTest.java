package edu.osu.csml.humdrum.census.interpretation.kern;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;

import org.junit.Before;
import org.junit.Test;

import edu.osu.csml.humdrum.Document;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.Spine;
import edu.osu.csml.humdrum.SpineFactory;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.census.interpretation.kern.CountConcurrentNotes;
import edu.osu.csml.humdrum.kern.KernInterpretationFactory;
import edu.osu.csml.humdrum.kern.KernNoteFactory;

public class CountConcurrentNotesTest {

	@Before
	public void setUp() throws Exception {
		CountConcurrentNotes.init();
		SpineFactory.register("\\*\\*kern", new Spine(new KernInterpretationFactory(new EventController()),new KernNoteFactory(),new EventController()));
	}

	@Test
	public void testEmpty() throws FileNotFoundException{
		Document document = new Document(10);
		document.parseDocument(
					new java.io.FileReader("test"+File.separator+"empty.hum"),
					new OutputStreamWriter(System.out));
		View[] checks = document.getGlobalChecks();
		for(int i=0;i<checks.length;++i){
			if(checks[i] instanceof CountConcurrentNotes){
				String outs = ((CountConcurrentNotes)checks[i]).output();
				if(outs.matches(".*0")){
					
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
			if(checks[i] instanceof CountConcurrentNotes){
				String outs = ((CountConcurrentNotes)checks[i]).output();
				if(outs.matches(".*0")){
					
				}else{
					fail(outs);
				}
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
			if(checks[i] instanceof CountConcurrentNotes){
				String outs = ((CountConcurrentNotes)checks[i]).output();
				if(outs.matches(".*0")){
					
				}else{
					fail(outs);
				}
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
			if(checks[i] instanceof CountConcurrentNotes){
				String outs = ((CountConcurrentNotes)checks[i]).output();
				if(outs.matches(".*1")){
					
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
			if(checks[i] instanceof CountConcurrentNotes){
				String outs = ((CountConcurrentNotes)checks[i]).output();
				if(outs.matches(".*2")){
					
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
			if(checks[i] instanceof CountConcurrentNotes){
				String outs = ((CountConcurrentNotes)checks[i]).output();
				if(outs.matches(".*3")){
					
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
			if(checks[i] instanceof CountConcurrentNotes){
				String outs = ((CountConcurrentNotes)checks[i]).output();
				if(outs.matches(".*2")){
					
				}else{
					fail(outs);
				}
			}
		}
	}

}
