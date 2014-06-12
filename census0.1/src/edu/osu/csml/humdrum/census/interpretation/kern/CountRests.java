package edu.osu.csml.humdrum.census.interpretation.kern;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.GlobalCheckFactory;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.note.Rest;

public class CountRests implements Interpretation {

	static boolean initialized = false;
	
	public static void init(){
		if(!initialized){
			GlobalCheckFactory.register(new CountRests(new EventController()));
			initialized = true;
		}
	}
	
	public CountRests(Controller master){
		this.master = master;
		master.register(Rest.class, this);
	}
	
	Controller master;
	
	int count=0;
	
	@Override
	public void fire(Class type, Object trigger) throws HumdrumException,
			HumdrumWarning {
		count++;

	}

	@Override
	public View clone(Controller master) {
		CountRests ret = new CountRests(master);
		ret.count = this.count;
		return ret;
	}

	@Override
	public View prototype(Controller master) {
		return new CountRests(master);
	}

	@Override
	public View prototype(String token, Controller master)
			throws HumdrumException, HumdrumWarning {
		return new CountRests(master);
	}

	@Override
	public boolean isCompatible(Interpretation k) {
		return true;
	}
	
	public String output(){
		return "Number of rests:\t\t"+count;
	}

}
