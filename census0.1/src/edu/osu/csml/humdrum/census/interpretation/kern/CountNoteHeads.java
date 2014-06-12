package edu.osu.csml.humdrum.census.interpretation.kern;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.GlobalCheckFactory;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.note.Single;

public class CountNoteHeads implements Interpretation {


	static boolean initialized = false;
	
	public static void init(){
		if(!initialized){
			GlobalCheckFactory.register(new CountNoteHeads(new EventController()));
			initialized = true;
		}
	}
	
	public CountNoteHeads(Controller master){
		this.master = master;
		master.register(Single.class, this);
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
		CountNoteHeads ret = new CountNoteHeads(master);
		ret.count = this.count;
		return ret;
	}

	@Override
	public View prototype(Controller master) {
		return new CountNoteHeads(master);
	}

	@Override
	public View prototype(String token, Controller master)
			throws HumdrumException, HumdrumWarning {
		return new CountNoteHeads(master);
	}

	@Override
	public boolean isCompatible(Interpretation k) {
		return true;
	}
	
	public String output(){
		return "Number of note-heads:\t\t"+count;
	}

}
