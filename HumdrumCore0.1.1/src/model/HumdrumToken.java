/**
 * 
 */
package model;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.EventController;
import edu.osu.csml.humdrum.Model;

/**
 * @author Daniel McEnnis
 *
 */
public class HumdrumToken implements Model {

	String content;
	
	Controller handler;
	
	
	public HumdrumToken(Controller master){
		handler = master;
	}
	
	public void init(String token){
		content = token;
		handler.fire(HumdrumToken.class,this);
	}
	
	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.Model#clone(edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public Object clone(Controller master) {
		HumdrumToken ret = new HumdrumToken(master);
		ret.content = this.content;
		return ret;
	}

	@Override
	public String output() {
		return content;
	}

}
