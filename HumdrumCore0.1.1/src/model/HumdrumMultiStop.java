package model;

import java.util.regex.Pattern;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.Model;

public class HumdrumMultiStop implements Model {

	HumdrumToken[] content;
	
	Controller handler;
	
	public HumdrumMultiStop(Controller master){
		handler= master;
	}
	
	public void init(String token){
		String[] tokens = token.split(" ");
		content = new HumdrumToken[tokens.length];
		for(int i=0;i<content.length;++i){
			content[i] = new HumdrumToken(handler);
			content[i].init(tokens[i]);
		}
		handler.fire(HumdrumMultiStop.class, this);
	}
	
	@Override
	public Object clone(Controller master) {
		HumdrumMultiStop ret = new HumdrumMultiStop(master);
		ret.content = new HumdrumToken[this.content.length];
		for(int i=0;i<ret.content.length;++i){
			ret.content[i] = (HumdrumToken)this.content[i].clone(master);
		}
		return ret;
	}

	@Override
	public String output() {
		StringBuffer ret = new StringBuffer();
		ret.append(content[0].output());
		for(int i=1;i<content.length;++i){
			ret.append(" ");
			ret.append(content[i].output());
		}
		return ret.toString();
	}

}
