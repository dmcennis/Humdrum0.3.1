package edu.osu.csml.humdrum.census.interpretation;

import model.HumdrumToken;
import model.NullNote;
import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.GlobalCheckFactory;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.note.Barline;
import edu.osu.csml.humdrum.kern.note.Rest;
import edu.osu.csml.humdrum.kern.note.Single;

public class CountDataTokens implements Interpretation {

	static boolean initialized = false;
	
	public static void init(){
		if(!initialized){
			GlobalCheckFactory.register(new CountDataTokens(new EventController()));
			initialized = true;
		}
	}
	
	public CountDataTokens(Controller master){
		this.master = master;
		master.register(Single.class, this);
		master.register(HumdrumToken.class, this);
		master.register(Rest.class, this);
		master.register(Barline.class,this);
		master.register(NullNote.class, this);
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
		CountDataTokens ret = new CountDataTokens(master);
		ret.count = this.count;
		return ret;
	}

	@Override
	public View prototype(Controller master) {
		return new CountDataTokens(master);
	}

	@Override
	public View prototype(String token, Controller master)
			throws HumdrumException, HumdrumWarning {
		return new CountDataTokens(master);
	}

	@Override
	public boolean isCompatible(Interpretation k) {
		return true;
	}

	
	public String output(){
		return "Number of data tokens:\t\t"+count;
	}
}
