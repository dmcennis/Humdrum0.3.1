/*
 * Created on Dec 11, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.proofCommand;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import edu.osu.csml.humdrum.Document;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.Spine;
import edu.osu.csml.humdrum.SpineFactory;
import edu.osu.csml.humdrum.kern.KernInterpretationFactory;
import edu.osu.csml.humdrum.kern.KernNoteFactory;
import edu.osu.csml.humdrum.kern.interpretation.BarlineCheck;
import edu.osu.csml.humdrum.kern.interpretation.CheckAppoggiatura;
import edu.osu.csml.humdrum.kern.interpretation.DupMeasureCheck;
import edu.osu.csml.humdrum.kern.interpretation.DurationSum;
import edu.osu.csml.humdrum.kern.interpretation.GlobalDurationCheck;
import edu.osu.csml.humdrum.kern.interpretation.KeySignature;
import edu.osu.csml.humdrum.kern.interpretation.Meter;
import edu.osu.csml.humdrum.kern.interpretation.MixedBarlineNoteCheck;
import edu.osu.csml.humdrum.kern.interpretation.PhraseLikeCheck;
import edu.osu.csml.humdrum.kern.interpretation.TieCheck;

/**
 * class wrapper for executing the proof program
 * @author Daniel McEnnis
 * 
 */
public class proof {

	/**
	 * default constructor
	 */
	public proof() {
		super();
		BarlineCheck.init();
		CheckAppoggiatura.init();
		DupMeasureCheck.init();
		DurationSum.init();
		GlobalDurationCheck.init();
		KeySignature.init();
		Meter.init();
		MixedBarlineNoteCheck.init();
		PhraseLikeCheck.init();
		TieCheck.init();
		SpineFactory.register("\\*\\*kern", new Spine(new KernInterpretationFactory(new EventController()),new KernNoteFactory(),new EventController()));
	}

	/**
	 * if there are arguments, treat them as file names, open and process them
	 * one by one. If not, assume input is from stdin.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		BarlineCheck.init();
		CheckAppoggiatura.init();
		DupMeasureCheck.init();
		DurationSum.init();
		GlobalDurationCheck.init();
		KeySignature.init();
		Meter.init();
		MixedBarlineNoteCheck.init();
		PhraseLikeCheck.init();
		TieCheck.init();
		SpineFactory.register("\\*\\*kern", new Spine(new KernInterpretationFactory(new EventController()),new KernNoteFactory(),new EventController()));
		int i=0;
		Document tmp;
		// Set warning level
		if((args.length>0) && (args[0].equals("-h"))){
			System.out.println("USAGE: proof [-hw] input_file");
			System.out.println("OPTIONS");
			System.out.println("   -h: print this messaage");
			System.out.println("   -w: disables warning messages");
			System.exit(0);
		}
		if((args.length>0) && (args[0].equals("-w"))){
			tmp = new Document(10);
			++i;
		}else{
			tmp = new Document();
		}
		// parse argument line
		if (args.length <= i) {
			tmp.parseDocument(
				new InputStreamReader(System.in),
				new OutputStreamWriter(System.err));
		} else {
			for (; i < args.length; ++i) {
				try {
					tmp.parseDocument(
						new java.io.FileReader(args[i]),
						new OutputStreamWriter(System.out));
				} catch (FileNotFoundException e) {
					System.err.println("File " + args[i] + " not found");
				}
			}
		}
	}
}
