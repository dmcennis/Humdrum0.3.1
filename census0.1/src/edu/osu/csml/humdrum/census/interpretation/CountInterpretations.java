package edu.osu.csml.humdrum.census.interpretation;

import model.HumdrumToken;
import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.GlobalCheckFactory;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.Model;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.note.Single;

public class CountInterpretations implements Interpretation {

	static boolean initialized = false;
	
	public static void init(){
		if(!initialized){
			GlobalCheckFactory.register(new CountInterpretations(new EventController()));
			initialized = true;
		}
	}
	
	public CountInterpretations(Controller master){
		this.master = master;
		master.register(Interpretation.class, this);
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
		CountInterpretations ret = new CountInterpretations(master);
		ret.count = this.count;
		return ret;
	}

	@Override
	public View prototype(Controller master) {
		return new CountInterpretations(master);
	}

	@Override
	public View prototype(String token, Controller master)
			throws HumdrumException, HumdrumWarning {
		return new CountInterpretations(master);
	}

	@Override
	public boolean isCompatible(Interpretation k) {
		return true;
	}
	
	public String output(){
		return "Number of Interpretations:\t"+count;
	}

}
