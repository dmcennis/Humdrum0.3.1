/*
 * Created on Aug 20, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.note;

import java.io.IOException;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.Note;
import edu.osu.csml.humdrum.kern.KernException;
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
/**
 * Model describing a note with duration, but not pitch.
 * @author Daniel McEnnis
 * 
 */
public class Rest extends Note {
	// Controller handler <from note>
	private Duration dur;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if ((obj instanceof Rest) && (((Rest) obj).dur.equals(this.dur))) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone(Controller master) {
		Rest tmp = new Rest(master);
		tmp.dur = (Duration) dur.clone(master);
		return tmp;
	}

	public Rest(Controller handler) {
		super(handler);
	}

	/**
	 * parses note to ensure that rest is syntactically correct, then calls on
	 * duration object to finish processing. It is assumed that this object can
	 * not have stem, bow, accents, or any other forms of modifications beyond
	 * duration.
	 */
	public void init(String token) throws HumdrumException, HumdrumWarning {
		super.init(token);
		if (!token.matches(".*r.*")) {
			throw new KernException("BUG: Illegal parsing of rest", handler);
		} else if (token.matches(".*r.*r.*")) {
			throw new KernException(
				"'r' may only occur once per rest",
				handler);
		} else {
			try {
				dur = new Duration(handler);
				dur.init(token);
				handler.fire(Duration.class, dur);
				handler.fire(Rest.class, this);
			} catch (KernException e) {
				try {
					handler.getErrStream().write(e.toString());
				} catch (IOException e1) {
					;
				}
			}
		}
	}

	@Override
	public String output() {
		return dur.output()+"r";
	}
}
