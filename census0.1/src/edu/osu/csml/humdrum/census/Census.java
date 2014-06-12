/**
 * 
 */
package edu.osu.csml.humdrum.census;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import edu.osu.csml.humdrum.Document;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.OptionHandler;
import edu.osu.csml.humdrum.Spine;
import edu.osu.csml.humdrum.SpineFactory;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.census.interpretation.CountDataLines;
import edu.osu.csml.humdrum.census.interpretation.CountDataTokens;
import edu.osu.csml.humdrum.census.interpretation.CountInterpretations;
import edu.osu.csml.humdrum.census.interpretation.CountMultipleStops;
import edu.osu.csml.humdrum.census.interpretation.CountNullTokens;
import edu.osu.csml.humdrum.census.interpretation.kern.CountConcurrentNotes;
import edu.osu.csml.humdrum.census.interpretation.kern.CountDoubleBarlines;
import edu.osu.csml.humdrum.census.interpretation.kern.CountNoteHeads;
import edu.osu.csml.humdrum.census.interpretation.kern.CountNotes;
import edu.osu.csml.humdrum.census.interpretation.kern.CountRests;
import edu.osu.csml.humdrum.census.interpretation.kern.CountSingleBarlines;
import edu.osu.csml.humdrum.census.interpretation.kern.HighestNote;
import edu.osu.csml.humdrum.census.interpretation.kern.LastNoteCount;
import edu.osu.csml.humdrum.census.interpretation.kern.LongestNote;
import edu.osu.csml.humdrum.census.interpretation.kern.LowestNote;
import edu.osu.csml.humdrum.census.interpretation.kern.ShortestNote;
import edu.osu.csml.humdrum.kern.KernInterpretationFactory;
import edu.osu.csml.humdrum.kern.KernNoteFactory;

/**
 * Census
 * @author Daniel McEnnis
 *
 */
public class Census {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CountDataTokens.init();
		CountNullTokens.init();
		CountMultipleStops.init();
		CountDataLines.init();
		CountInterpretations.init();

		CountNoteHeads.init();
		CountNotes.init();
		LongestNote.init();
		ShortestNote.init();
		HighestNote.init();
		LowestNote.init();
		CountRests.init();
		CountConcurrentNotes.init();
		CountSingleBarlines.init();
		CountDoubleBarlines.init();
		LastNoteCount.init();
		SpineFactory.register("\\*\\*kern", new Spine(new KernInterpretationFactory(new EventController()),new KernNoteFactory(),new EventController()));
		OptionHandler options = new OptionHandler();
		String[] parameters = options.parse(args);
		if(options.isPresent("h")){
			System.out.println("USAGE: census [-hk] input_file");
			System.out.println("OPTIONS");
			System.out.println("   -h: print this messaage");
			System.out.println("   -k: output kern specific information");
			System.exit(0);
		}
//		System.out.println(parameters[0]);
		Document document = new Document(10);
		if(parameters.length==0){
			document.parseDocument(
					new InputStreamReader(System.in),
					new OutputStreamWriter(System.out));
		}else{
			try {
				document.parseDocument(
					new java.io.FileReader(parameters[0]),
					new OutputStreamWriter(System.out));
			} catch (FileNotFoundException e) {
				System.err.println("File " + parameters[0] + " not found");
			}
		}
		View[] checks = document.getGlobalChecks();
		int lineNum = document.getLineNum();
		int i=0;
		System.out.println("HUMDRUM DATA");
		System.out.println(((CountDataTokens)checks[i++]).output());
		System.out.println(((CountNullTokens)checks[i++]).output());
		System.out.println(((CountMultipleStops)checks[i++]).output());
		System.out.println(((CountDataLines)checks[i++]).output());
		System.out.println(((CountInterpretations)checks[i++]).output());
		System.out.println("Number of records:\t\t"+lineNum);
		if(options.isPresent("k")){
			System.out.println("KERN DATA");
			System.out.println(((CountNoteHeads)checks[i++]).output());
			System.out.println(((CountNotes)checks[i++]).output());
			System.out.println(((LongestNote)checks[i++]).output());
			System.out.println(((ShortestNote)checks[i++]).output());
			System.out.println(((HighestNote)checks[i++]).output());
			System.out.println(((LowestNote)checks[i++]).output());
			System.out.println(((CountRests)checks[i++]).output());
			System.out.println(((CountConcurrentNotes)checks[i++]).output());
			System.out.println(((CountSingleBarlines)checks[i++]).output());
			System.out.println(((CountDoubleBarlines)checks[i++]).output());
			
		}
	}
}
