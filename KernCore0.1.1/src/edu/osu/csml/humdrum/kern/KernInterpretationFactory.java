/*
 * Created on Aug 23, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.InterpretationFactory;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.interpretation.Generic;

/**
 * Factory for interpreting interpretation tokens in kern spines.
 * 
 * TODO: This class should read its matching tokens from an XML config file,
 * but is hard coded for now.
 * @author Daniel McEnnis
 * 
 */
public class KernInterpretationFactory implements InterpretationFactory {

	private static HashSet<Interpretation> global = null;
	private static HashMap<String,Interpretation> specific = null;
	
	
	
	public static void registerGlobal(Interpretation spineWide){
		if(global == null){
			global = new HashSet<Interpretation>();
			specific = new HashMap<String,Interpretation>();
		}
		global.add(spineWide);
	}
	
	public static void registerSpecific(String regexp,Interpretation local ){
		if(global == null){
			global = new HashSet<Interpretation>();
			specific = new HashMap<String,Interpretation>();
		}
		specific.put(regexp,local);
	}
	/**
	 * Create a new factory object. Typically called by SpineFactory
	 * @param master
	 */
	public KernInterpretationFactory(Controller master) {
		if(global == null){
			global = new HashSet<Interpretation>();
			specific = new HashMap<String,Interpretation>();
		}
		for(Iterator<Interpretation> it = global.iterator();it.hasNext();){
			it.next().prototype(master);
		}			
	}

	/**
	 * returns the correct (kern specific) interpretation given the string provided
	 * @throws HumdrumWarning 
	 * @throws HumdrumException 
	 */
	public Interpretation parseInterpretation(String token, Controller master)
		throws HumdrumException, HumdrumWarning {
		if(global == null){
			global = new HashSet<Interpretation>();
			specific = new HashMap<String,Interpretation>();
		}
		for(Iterator<String> it = specific.keySet().iterator();it.hasNext();){
			String regexp = it.next();
			if(token.matches(regexp)){
				View ret = specific.get(regexp).prototype(token,master);
				master.fire(Interpretation.class, ret);
				return (Interpretation) ret;
			}
		}
		View ret = new Generic(token);
		master.fire(Interpretation.class,ret);
		return (Interpretation)ret;
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.InterpretationFactory#prototype(edu.osu.csml.humdrum.EventController)
	 */
	@Override
	public InterpretationFactory prototype(EventController spineMaster) {
		return new KernInterpretationFactory(spineMaster);
	}

}
