/**
 * 
 */
package edu.osu.csml.humdrum;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * 
 * 
 * @author Daniel McEnnis
 *
 */
public class OptionHandler {

	HashSet<String> options = new HashSet<String>();;
	
	public String[] parse(String[] args){
		boolean done = false;
		int count = 0;
		LinkedList<String> remainder = new LinkedList<String>();
		
		while(!done && (count < args.length)){
			if(args[count].matches("--")){
				done = true;
				count++;
			}else if(args[count].matches("-.*")){
				String[] opt = args[count].split("");
				for(int i=0;i<opt.length;++i){
					options.add(opt[i]);
				}
				count++;
			}else{
				done = true;
				remainder.add(args[count++]);
			}
//			System.out.print(".");
		}
		for(;count<args.length;++count){
			remainder.add(args[count]);
		}
		return remainder.toArray(new String[]{});
	}
	
	public String[] getOptions(){
		return options.toArray(new String[]{});
	}
	
	public boolean isPresent(String option){
		return options.contains(option);
	}
}
