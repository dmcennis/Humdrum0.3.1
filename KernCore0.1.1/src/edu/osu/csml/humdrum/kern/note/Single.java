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
import edu.osu.csml.humdrum.HumdrumException;
import edu.osu.csml.humdrum.HumdrumWarning;
import edu.osu.csml.humdrum.kern.KernException;

/**
 * Model representing a single note - no multistops permitted
 * @author Daniel McEnnis
 * 
 */
public class Single extends Note {
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Single) {
			if ((((Single) obj).dur.equals(this.dur))
				&& (((Single) obj).pitch.equals(this.pitch))) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone(Controller master) {
		Single tmp = new Single(master);
		tmp.dur = (Duration) dur.clone(master);
		tmp.pitch = (Pitch) pitch.clone(master);
		return tmp;
	}

	/**
	 * Create a new note, including all possible sub-objects that a note may
	 * possess
	 * 
	 * @param handler
	 */
	public Single(Controller handler) {
		super(handler);
		dur = new Duration(handler);
		pitch = new Pitch(handler);
		spb = new SlurPhraseBeam(handler);
		tie = new Tie(handler);
		sb = new StemBow(handler);
		acc = new Accent(handler);
	}

	String content;
	
	/**
	 * duration of this note
	 */
	private Duration dur;
	/**
	 * pitch of this note
	 */
	private Pitch pitch;
	/**
	 * slur, beams, or phrases associated with this note - if any
	 */
	private SlurPhraseBeam spb;
	/**
	 * Tie presents - if any
	 */
	private Tie tie;
	/**
	 * Bow or stem markings present if any
	 */
	private StemBow sb;
	/**
	 * Accents present, if any
	 */
	private Accent acc;

	/**
	 * Parse a note from a token with spaces removed. Ensure entire object is
	 * syntacally correct. Also responsible for firing the events for all
	 * sub-objects created in this stage. Only fires events if none of the
	 * sub-elements reported syntax errors.
	 */
	public void init(String token) throws HumdrumException, HumdrumWarning {
		super.init(token);
		content = token;
		boolean problem = false;

		// Parse Ties
		tie.init(token);

		// Parse Note stems and bowing markings
		sb.init(token);

		// Parse Slurs, phrases, and beams
		spb.init(token);

		// Parse all accents and ornaments
		try {
			acc.init(token);
		} catch (KernException e) {
			problem = true;
			try {
				handler.getErrStream().write(token);
			} catch (IOException e1) {
				;
			}
		}

		// Get the duration object to calculate duration of note
		try {
			if (Duration.isPresent(token)) {
				dur.init(token);
			} else {
				dur = Duration.getDefault(handler);
			}
		} catch (HumdrumException e) {
			problem = true;
			try {
				handler.getErrStream().write(e.toString());
			} catch (IOException e1) {
				;
			}
		} catch (HumdrumWarning e) {
			problem = true;
			try {
				handler.getErrStream().write(e.toString());
			} catch (IOException e1) {
				;
			}
		}

		//
		// Get pitch object to determine pitch of note
		//
		try {
			pitch = new Pitch(handler);
			pitch.init(token);
		} catch (HumdrumException e) {
			problem = true;
			try {
				handler.getErrStream().write(e.toString());
			} catch (IOException e1) {
				;
			}
		} catch (HumdrumWarning e) {
			problem = true;
			try {
				handler.getErrStream().write(e.toString());
			} catch (IOException e1) {
				;
			}
		}

		if (!problem) {
			handler.fire(SlurPhraseBeam.class, spb);
			handler.fire(Tie.class, tie);
			handler.fire(StemBow.class, sb);
			handler.fire(Pitch.class, pitch);
			handler.fire(Duration.class, dur);
			handler.fire(Accent.class, acc);
			handler.fire(Single.class,this);
		}

	} // init(token)

	@Override
	public String output() {
		return content;
	}

}
