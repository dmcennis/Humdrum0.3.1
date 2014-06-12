package edu.osu.csml.humdrum.census.interpretation.kern;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.GlobalCheckFactory;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.Model;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.note.Pitch;
import edu.osu.csml.humdrum.kern.note.Rest;

public class HighestNote implements Interpretation {

	static boolean initialized = false;
	
	public static void init(){
		if(!initialized){
			GlobalCheckFactory.register(new HighestNote(new EventController()));
			initialized = true;
		}
	}
	
	public HighestNote(Controller master){
		this.master = master;
		master.register(Pitch.class, this);
	}
	
	Controller master;
	
	Pitch count=null;
	
	@Override
	public void fire(Class type, Object trigger) throws HumdrumException,
			HumdrumWarning {
		Pitch p = (Pitch)trigger;
		if((count==null)||(p.getPitch() > count.getPitch())){
			count = p;
		}
	}

	@Override
	public View clone(Controller master) {
		HighestNote ret = new HighestNote(master);
		ret.count=this.count;
		return ret;
	}

	@Override
	public View prototype(Controller master) {
		return new HighestNote(master);
	}

	@Override
	public View prototype(String token, Controller master)
			throws HumdrumException, HumdrumWarning {
		return new HighestNote(master);
	}

	@Override
	public boolean isCompatible(Interpretation k) {
		return true;
	}
	
	public String output(){
		if(count==null){
			return "Highest note:\t\t\tnone";			
		}else{
			return "Highest note:\t\t\t"+count.output();
		}
	}

}
