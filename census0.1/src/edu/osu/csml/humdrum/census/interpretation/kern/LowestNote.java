package edu.osu.csml.humdrum.census.interpretation.kern;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.GlobalCheckFactory;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.note.Pitch;

public class LowestNote implements Interpretation {

	static boolean initialized = false;
	
	public static void init(){
		if(!initialized){
			GlobalCheckFactory.register(new LowestNote(new EventController()));
			initialized = true;
		}
	}
	
	public LowestNote(Controller master){
		this.master = master;
		master.register(Pitch.class, this);
	}
	
	Controller master;
	
	Pitch count=null;
	
	@Override
	public void fire(Class type, Object trigger) throws HumdrumException,
			HumdrumWarning {
		Pitch p = (Pitch)trigger;
		if((count==null)||(p.getPitch() < count.getPitch())){
			count = p;
		}

	}

	@Override
	public View clone(Controller master) {
		LowestNote ret = new LowestNote(master);
		ret.count=this.count;
		return ret;
	}

	@Override
	public View prototype(Controller master) {
		return new LowestNote(master);
	}

	@Override
	public View prototype(String token, Controller master)
			throws HumdrumException, HumdrumWarning {
		return new LowestNote(master);
	}

	@Override
	public boolean isCompatible(Interpretation k) {
		return true;
	}
	
	public String output(){
		if(count==null){
			return "Lowest note:\t\t\tnone";			
		}else{
			return "Lowest note:\t\t\t"+count.output();
		}
	}
}
