/*
 * Created on Feb 27, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

import edu.osu.csml.humdrum.Document.MasterController;


/**
 * Global check designed to catch sequential duplicated lines.
 * 
 * @author Daniel McEnnis
 */
public class DupLineCheck implements View {
	
	static boolean started = false;
	
	static synchronized public void init(){
		if(!started){
			GlobalCheckFactory.register(new DupLineCheck(new EventController()));
			started=true;
		}
	}
	/**
	 * link to master controller (usually for a document object)
	 */
	private Controller master;
	/**
	 * reference to the last line proccessed
	 */
	private Line lastLine;

	/**
	 * Create the DupLineCheck object and register it to recieve Line events
	 * 
	 * @param master
	 */
	public DupLineCheck(Controller master) {
		this.master = master;
		master.register(Line.class, this);
	}

	/**
	 * Keep up to a full line of context.
	 */
	public void fire(Class type, Object trigger)
		throws HumdrumException, HumdrumWarning {
		if (type == Line.class) {
			if (((Line) trigger).equals(lastLine)) {
				throw new HumdrumWarning(
					"Possible unintentional duplication",
					master);
			}
			lastLine = (Line) trigger;
		} else {
			throw new HumdrumException(
				"BUG: DupLineCheck should not be registered to recieve events of type "
					+ type.getName(),
				master);
		}

	}

	/**
	 * Only one DupLineCheck should exist at any given time, but give the
	 * opportunity for trouble.
	 * 
	 * @return reference to deep copy
	 */
	public View clone(Controller master) {
		DupLineCheck ret = new DupLineCheck(master);
		ret.lastLine = this.lastLine;
		return ret;
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(Controller master) {
		return new DupLineCheck(master);
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(java.lang.String, edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(String token, Controller master) {
		return new DupLineCheck(master);
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#isCompatible(edu.osu.csml.humdrum.Interpretation)
	 */
	@Override
	public boolean isCompatible(Interpretation k) {
		// TODO Auto-generated method stub
		return true;
	}

}
