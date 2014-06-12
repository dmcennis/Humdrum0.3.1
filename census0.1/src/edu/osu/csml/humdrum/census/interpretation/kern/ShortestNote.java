package edu.osu.csml.humdrum.census.interpretation.kern;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.GlobalCheckFactory;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.note.Duration;

public class ShortestNote implements Interpretation {

	static boolean initialized = false;
	
	public static void init(){
		if(!initialized){
			GlobalCheckFactory.register(new ShortestNote(new EventController()));
			initialized = true;
		}
	}
	
	public ShortestNote(Controller master){
		this.master = master;
		master.register(Duration.class, this);
	}
	
	Controller master;
	
	Duration duration=null;
	
	@Override
	public void fire(Class type, Object trigger) throws HumdrumException,
			HumdrumWarning {
		Duration d = (Duration)trigger;
		if((duration==null)||(d.getDuration() < duration.getDuration())){
			duration = d;
		}
	}

	@Override
	public View clone(Controller master) {
		ShortestNote ret = new ShortestNote(master);
		ret.duration=this.duration;
		return ret;
	}

	@Override
	public View prototype(Controller master) {
		return new ShortestNote(master);
	}

	@Override
	public View prototype(String token, Controller master)
			throws HumdrumException, HumdrumWarning {
		return new ShortestNote(master);
	}

	@Override
	public boolean isCompatible(Interpretation k) {
		return true;
	}

	
	public String output(){
		if(duration == null){
			return "Shortest note:\t\t\tnone";
		}else{
			return "Shortest note:\t\t\t"+duration.output();
		}
	}
}
