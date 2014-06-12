/**
 * 
 */
package edu.osu.csml.humdrum.census.interpretation.kern;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EndOfToken;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.KernInterpretationFactory;
import edu.osu.csml.humdrum.kern.note.Rest;
import edu.osu.csml.humdrum.kern.note.Single;

/**
 * @author user
 *
 */
public class LastNoteCount implements Interpretation {

	static boolean started = false;
	
	static int done = init();
	
	static synchronized public int init(){
		if(!started){
			KernInterpretationFactory.registerGlobal(new LastNoteCount(new EventController()));
			started=true;
		}
		return 1;
	}
	
	Controller master;
	
	int count = 0;
	
	volatile int sum = 0;
	boolean rest = false;
	
	public LastNoteCount(Controller controller){
		master = controller;
		master.register(Single.class, this);
		master.register(EndOfToken.class, this);
		master.register(Rest.class, this);
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#fire(java.lang.Class, java.lang.Object)
	 */
	@Override
	public void fire(Class type, Object trigger) throws HumdrumException,
			HumdrumWarning {
		if(type.equals(Single.class)){
			sum++;
		}else if(type.equals(EndOfToken.class)){
			if(sum>0){
				count = sum;
				sum=0;
			}
			if(rest){
				sum=0;
				count=0;
				rest = false;
			}
			master.fire(Integer.class,new Integer(count));
		}else if(type.equals(Rest.class)){
			rest = true;
		}
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#clone(edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View clone(Controller master) {
		LastNoteCount ret = new LastNoteCount(master);
		ret.count = this.count;
		return ret;
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(Controller master) {
		return new LastNoteCount(master);
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(java.lang.String, edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(String token, Controller master)
			throws HumdrumException, HumdrumWarning {
		return new LastNoteCount(master);
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.Interpretation#isCompatible(edu.osu.csml.humdrum.Interpretation)
	 */
	@Override
	public boolean isCompatible(Interpretation k) {
		return true;
	}
	
	public String output(){
		return "";
	}

}
