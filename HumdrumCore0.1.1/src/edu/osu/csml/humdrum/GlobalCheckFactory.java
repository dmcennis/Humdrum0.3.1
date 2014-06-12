/**
 * GlobalCheckFactory
 *
 * Created Jan 16, 2011-12:44:24 PM by Daniel McEnnis
 * Distributed under the latest LGPL license.  See www.fsf.org for license details.
 */
package edu.osu.csml.humdrum;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * GlobalCheckFactory
 *
 * @author Daniel McEnnis
 */
public class GlobalCheckFactory {

	private static GlobalCheckFactory instance = null;
	
	public static void init(){
		if(instance == null){
			instance = new GlobalCheckFactory();
		}
	}
	
	LinkedList<View> checks = new LinkedList<View>();
	
	public static void register(View object){
		if(instance == null){
			instance = new GlobalCheckFactory();
		}
		instance.checks.add(object);
	}
	
	public static View[] getChecks(Controller master){
		if(instance == null){
			instance = new GlobalCheckFactory();
		}
		View[] ret = new View[instance.checks.size()];
		int i=0;
		for(Iterator<View> it = instance.checks.iterator();it.hasNext();){
			ret[i++] = it.next().prototype(master);
			
		}
		return ret;
	}
}
