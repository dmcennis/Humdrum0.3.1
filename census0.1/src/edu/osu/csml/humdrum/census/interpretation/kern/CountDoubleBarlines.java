package edu.osu.csml.humdrum.census.interpretation.kern;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.GlobalCheckFactory;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.note.Barline;

public class CountDoubleBarlines implements Interpretation {

	static boolean initialized = false;
	
	public static void init(){
		if(!initialized){
			GlobalCheckFactory.register(new CountDoubleBarlines(new EventController()));
			initialized = true;
		}
	}
	
	public CountDoubleBarlines(Controller master){
		this.master = master;
		master.register(Barline.class, this);
	}
	
	Controller master;
	
	int count=0;
	
	@Override
	public void fire(Class type, Object trigger) throws HumdrumException,
			HumdrumWarning {
		Barline b = (Barline)trigger;
		if(b.isDouble()){
			count++;
		}

	}

	@Override
	public View clone(Controller master) {
		CountDoubleBarlines ret = new CountDoubleBarlines(master);
		ret.count = this.count;
		return ret;
	}

	@Override
	public View prototype(Controller master) {
		return new CountDoubleBarlines(master);
	}

	@Override
	public View prototype(String token, Controller master)
			throws HumdrumException, HumdrumWarning {
		return new CountDoubleBarlines(master);
	}

	@Override
	public boolean isCompatible(Interpretation k) {
		return true;
	}
	
	public String output(){
		return "Number of double barlines:\t"+count;
	}

}
