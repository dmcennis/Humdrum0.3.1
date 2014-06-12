/*
 * Created on Aug 20, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern;

/**
 * Kern specific factory for parsing data objects. Typically created by
 * SpineFactory.
 * 
 * TODO: The contents ought to be loaded from an XML file, but are hard coded.
 * @author Daniel McEnnis
 * 
 */

import model.NullNote;
import edu.osu.csml.humdrum.*;
import edu.osu.csml.humdrum.kern.note.Barline;
import edu.osu.csml.humdrum.kern.note.Multi;
import edu.osu.csml.humdrum.kern.note.Rest;
import edu.osu.csml.humdrum.kern.note.Single;

public class KernNoteFactory implements NoteFactory {

	/**
	 * Generic constructor, essentially a library class
	 */
	public KernNoteFactory() {
	}

	/**
	 * returns the correct Kern data object given the data token provided. This
	 * creation process causes the notes generated to fire off events to its
	 * controller. This is the heart of kern data processing.
	 */
	public Note getNote(String token, Controller handler)
		throws HumdrumException, HumdrumWarning {
		Note temp;
		if (token.matches(".*[a-gA-G].*") && !token.matches(".* .*")) {
			temp = new Single(handler);
		} else if (token.matches(".* .*")) {
			temp = new Multi(handler);
		} else if (token.matches(".*r.*")) {
			temp = new Rest(handler);
		} else if (token.matches(".*=.*")) {
			temp = new Barline(handler);
		} else if (token.matches(".")) {
			temp = new NullNote(handler);
		} else {
			throw new KernException("Badly formed kern note found", handler);
		}
		temp.init(token);
//		handler.fire(EndOfToken.class, new EndOfToken());
		return temp;
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.NoteFactory#prototype(edu.osu.csml.humdrum.EventController)
	 */
	@Override
	public NoteFactory prototype(EventController spineMaster) {
		return new KernNoteFactory();
	}

}
