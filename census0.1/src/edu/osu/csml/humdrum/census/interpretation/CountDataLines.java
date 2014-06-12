package edu.osu.csml.humdrum.census.interpretation;

import model.HumdrumToken;
import model.NullNote;
import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.GlobalCheckFactory;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.Interpretation;
import edu.osu.csml.humdrum.Line;
import edu.osu.csml.humdrum.View;
import edu.osu.csml.humdrum.kern.note.Barline;
import edu.osu.csml.humdrum.kern.note.Rest;
import edu.osu.csml.humdrum.kern.note.Single;

public class CountDataLines implements Interpretation {

	public static boolean initialized = false;
	
	public static void init(){
		if(!initialized){
			GlobalCheckFactory.register(new CountDataLines(new EventController()));
			initialized = true;
		}
	}
	
	Controller master;
	
	int count=0;
	boolean seenData = false;
	
	public CountDataLines(Controller master){
		this.master = master;
		master.register(Line.class, this);
		master.register(HumdrumToken.class, this);
		master.register(Single.class, this);
		master.register(Rest.class, this);
		master.register(Barline.class,this);
		master.register(NullNote.class, this);
	}
	
	@Override
	public void fire(Class type, Object trigger) throws HumdrumException,
			HumdrumWarning {
		if(type.equals(HumdrumToken.class)){
			seenData = true;
		}else if(type.equals(Single.class)){
			seenData = true;
		}else if(type.equals(NullNote.class)){
			seenData = true;
		}else if(type.equals(Rest.class)){
			seenData = true;
		}else if(type.equals(Barline.class)){
			seenData = true;
		}else if(type.equals(Line.class)){
			if(seenData){
				seenData = false;
				count++;
			}
		}
	}

	@Override
	public View clone(Controller master) {
		CountDataLines ret = new CountDataLines(master);
		ret.count = this.count;
		ret.seenData = this.seenData;
		return ret;
	}

	@Override
	public View prototype(Controller master) {
		return new CountDataLines(master);
	}

	@Override
	public View prototype(String token, Controller master)
			throws HumdrumException, HumdrumWarning {
		return new CountDataLines(master);
	}

	@Override
	public boolean isCompatible(Interpretation k) {
		return true;
	}
	
	public String output(){
		return "Number of data records:\t\t"+count;
	}

}
