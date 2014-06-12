package edu.osu.csml.humdrum.census.interpretation.kern;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.GlobalCheckFactory;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.note.Single;
import edu.osu.csml.humdrum.kern.note.Tie;

public class CountNotes implements Interpretation {

	static boolean initialized = false;
	
	public static void init(){
		if(!initialized){
			GlobalCheckFactory.register(new CountNotes(new EventController()));
			initialized = true;
		}
	}
	
	public CountNotes(Controller master){
		this.master = master;
		master.register(Single.class, this);
		master.register(Tie.class, this);
	}
	
	Controller master;
	
	int count=0;
	
	@Override
	public void fire(Class type, Object trigger) throws HumdrumException,
			HumdrumWarning {
		if(type.equals(Single.class)){
			count++;
		}else if(type.equals(Tie.class)){
			Tie t = (Tie)trigger;
			if(t.isPresent(Tie.CONTINUE)||t.isPresent(Tie.END)){
				count--;
			}
		}
	}

	@Override
	public View clone(Controller master) {
		CountNotes ret = new CountNotes(master);
		ret.count=this.count;
		return ret;
	}

	@Override
	public View prototype(Controller master) {
		return new CountNotes(master);
	}

	@Override
	public View prototype(String token, Controller master)
			throws HumdrumException, HumdrumWarning {
		return new CountNotes(master);
	}

	@Override
	public boolean isCompatible(Interpretation k) {
		return true;
	}
	
	public String output(){
		return "Number of notes:\t\t"+count;
	}

}
