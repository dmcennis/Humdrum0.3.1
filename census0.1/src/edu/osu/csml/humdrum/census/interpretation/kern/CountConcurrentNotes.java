package edu.osu.csml.humdrum.census.interpretation.kern;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.GlobalCheckFactory;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.Line;
import edu.osu.csml.humdrum.Model;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.note.Barline;

public class CountConcurrentNotes implements Interpretation {

	static boolean initialized = false;
	
	public static void init(){
		if(!initialized){
			GlobalCheckFactory.register(new CountConcurrentNotes(new EventController()));
			initialized = true;
		}
	}
	
	public CountConcurrentNotes(Controller master){
		this.master = master;
		master.register(Integer.class, this);
		master.register(Line.class, this);
	}
	
	Controller master;
	
	int count=0;
	
	int max = Integer.MIN_VALUE;
	
	@Override
	public void fire(Class type, Object trigger) throws HumdrumException,
			HumdrumWarning {
		if(type.equals(Integer.class)){
			count+= (Integer)trigger;
		}else if(type.equals(Line.class)){
			if(count > max){
				max = count;
			}
			count =0;
		}
	}

	@Override
	public View clone(Controller master) {
		CountConcurrentNotes ret = new CountConcurrentNotes(master);
		ret.count = this.count;
		ret.max = this.max;
		return ret;
	}

	@Override
	public View prototype(Controller master) {
		return new CountConcurrentNotes(master);
	}

	@Override
	public View prototype(String token, Controller master)
			throws HumdrumException, HumdrumWarning {
		return new CountConcurrentNotes(master);
	}

	@Override
	public boolean isCompatible(Interpretation k) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String output(){
		return "Maximum concurrent notes:\t"+count;
	}

}
