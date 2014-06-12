/*
 * Created on Aug 23, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Define all exclusive interpretations here. Really should be pulled from an
 * XML file, but for now its all hard coded
 * 
 * TODO: change to load from XML file
 * @author Daniel McEnnis
 * 
 */
public class SpineFactory {
	
	private static SpineFactory instance = null;
	
	public static void init(){
		if(instance == null){
			instance = new SpineFactory();
		}
	}
	
	HashMap<String,Spine> spineSet = new HashMap<String,Spine>();
	
	/**
	 * function that interprets an exclusive interpretation token and creates the
	 * appropriate factories for the spine.
	 * 
	 * @param token string containing the exclusive interpretatio to be interpreted
	 * @param master parent controller
	 * @return new spine with appropriate factory routines
	 */
	public static Spine parseSpine(String token, Controller master) {
		if(instance == null){
			instance = new SpineFactory();
		}
		EventController spineMaster = new EventController(master);
		for(Iterator<String> it = instance.spineSet.keySet().iterator();it.hasNext();){
			String match = it.next();
			if(token.matches(match)){
				return instance.spineSet.get(match).prototype(spineMaster);
			}
		}
		return new GenericSpine(master);
	}
	
	public static void register(String regexp,Spine spine){
		if(instance == null){
			instance = new SpineFactory();
		}
		instance.spineSet.put(regexp, spine);
	}
}
