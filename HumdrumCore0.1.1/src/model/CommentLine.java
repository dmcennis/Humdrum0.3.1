package model;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.Model;

public class CommentLine implements Model {

	String comment;
	@Override
	public Object clone(Controller master) {
		CommentLine ret = new CommentLine(this.comment,master);
		return ret;
	}
	
	public CommentLine(){}
	
	public CommentLine(Controller master){}
	
	public CommentLine(String token,Controller master){
		comment = token;
	}

	@Override
	public String output() {
		return comment;
	}

}
