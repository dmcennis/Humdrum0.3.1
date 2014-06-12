/*
 * Created on Aug 20, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.note;

import java.io.IOException;
import java.io.Writer;
import java.util.Vector;
import java.util.regex.Pattern;

import edu.osu.csml.humdrum.*;
import edu.osu.csml.humdrum.kern.*;

/**
 * Responsible for handling all multistop data tokens
 * @author Daniel McEnnis
 * 
 */
public class Multi extends Note implements Controller, Cloneable {
	private MultiController master;
	private Controller parent;
	/**
	 * list of all notes present in this multistop token
	 */
	private Vector content = new Vector();
	/**
	 * all notes must have the same duration - stored in this variable
	 */
	private Duration dur = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.Controller#getErrStream()
	 */
	public Writer getErrStream() {
		return master.getErrStream();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Multi) {
			Multi tmp = (Multi) obj;
			if (tmp.dur.equals(this.dur)) {
				if (tmp.content.equals(this.content)) {

					return true;
				}
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
		Multi tmp = new Multi(master);
		tmp.master = this.master;
		tmp.parent = parent;
		tmp.content = (Vector) content.clone();
		tmp.dur = (Duration) dur.clone(master);
		for (int i = 0; i < tmp.content.size(); ++i) {
			tmp.content.set(i, ((Model) content.elementAt(i)).clone(tmp));
		}
		return tmp;
	}

	/**
	 * listen for durations for each note present, making certain they are all
	 * consistant
	 */
	public void fire(Class type, Object data) {
		if ((parent != null) && (type != Duration.class)&&(type != EndOfToken.class)) {
			parent.fire(type, data);
		}
		else if ((dur == null) && (type == Duration.class)) {
			dur = (Duration) data;
		} else if ((dur != null) && (type == Duration.class)) {
			try {
				if (dur.getDuration() != ((Duration) (data)).getDuration()) {
					throw new KernException(
						"Notes in multistop note do not agree on duration of note",
						master);
				}
			} catch (KernException e) {
				try {
					parent.getErrStream().write(e.toString());
				} catch (IOException e1) {
					;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.Controller#getLineNumber()
	 */
	public int getLineNumber() {
		if (parent != null) {
			return parent.getLineNumber();
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.Controller#getParent()
	 */
	public Controller getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.Controller#getSpineNumber()
	 */
	public int getSpineNumber() {
		if (parent != null) {
			return parent.getSpineNumber();
		} else {
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.Controller#register(java.lang.Class,
	 *      edu.osu.csml.humdrum.View)
	 */
	public void register(Class type, View rec) {
		master.register(type, rec);
	}

	public Multi(Controller handler) {
		super(handler);
		parent = handler;
		master = new MultiController(parent);
	}

	/**
	 * Parse a multistop token into notes, create notes to parse them, then
	 * validate the result
	 * 
	 * Jan 24 2011 - Added fire(Multi.class,this) to mark to listeners that it is 
	 * 				 a Multi-stop note.
	 */
	public void init(String token) throws HumdrumException, HumdrumWarning {
		super.init(token);
		Pattern split = Pattern.compile(" ");
		String[] notes = split.split(token);
		Note temp;
		for (int i = 0; i < notes.length; ++i) {
			temp = (new KernNoteFactory()).getNote(notes[i], this);
			if (temp.getClass() != Single.class) {
				throw new KernException(
					"Note " + i + " of a multistop entry is not a note",
					master);
			} else {
				content.add(temp);
				fire(EndOfNote.class, new EndOfNote());
			}
		}
		fire(Multi.class,this);
		parent.fire(Duration.class, dur);
//		parent.fire(EndOfToken.class,new EndOfToken());
	}

	/**
	 * @author Daniel McEnnis
	 * 
	 * specialize an event controller to allow Multi to catch duration events
	 * from notes and only resend one per token.
	 */
	class MultiController extends EventController {

		public MultiController(Controller parent) {
			super(parent);
		}

		/**
		 * Do some processing for syntax errors, then decide if the event
		 * should be propogated.
		 */
		public void fire(Class name, Object trigger) {
			if (name == Duration.class) {
				if (dur == null) {
					super.fire(name, trigger);
				} else {
					if (!dur.equals(trigger)) {
						try {
							throw new KernException(
								"Durations differ inside a multistop note",
								master);
						} catch (KernException e) {
							try {
								parent.getErrStream().write(e.toString());
							} catch (IOException e1) {
								;
							}
						}
					}
				}
			} else {
				super.fire(name, trigger);
			}
		}

	}

	@Override
	public String output() {
		StringBuffer ret = new StringBuffer();
		ret.append(((Model)content.get(0)).output());
		for(int i=1;i<content.size();++i){
			ret.append(" ");
			ret.append(((Model)content.get(i)).output());
		}
		return ret.toString();
	}

}
