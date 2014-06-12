package edu.osu.csml.humdrum.census.interpretation.kern;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.GlobalCheckFactory;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.Model;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.note.Duration;
import edu.osu.csml.humdrum.kern.note.Rest;

public class LongestNote implements Interpretation {

	static boolean initialized = false;
	
	public static void init(){
		if(!initialized){
			GlobalCheckFactory.register(new LongestNote(new EventController()));
			initialized = true;
		}
	}
	
	public LongestNote(Controller master){
		this.master = master;
		master.register(Duration.class, this);
	}
	
	Controller master;
	
	Duration duration = null;
	
	@Override
	public void fire(Class type, Object trigger) throws HumdrumException,
			HumdrumWarning {
		Duration d = (Duration)trigger;
		if((duration==null)||(d.getDuration() > duration.getDuration())){
			duration = d;
		}
	}

	@Override
	public View clone(Controller master) {
		LongestNote ret = new LongestNote(master);
		ret.duration = this.duration;
		return ret;
	}

	@Override
	public View prototype(Controller master) {
		return new LongestNote(master);
	}

	@Override
	public View prototype(String token, Controller master)
			throws HumdrumException, HumdrumWarning {
		return new LongestNote(master);
	}

	@Override
	public boolean isCompatible(Interpretation k) {
		return true;
	}
	
	public String output(){
		if(duration == null){
			return "Longest note:\t\t\tnone";
		}else{
			return "Longest note:\t\t\t"+duration.output();
		}
	}

}
