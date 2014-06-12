/*
 * Created on Sep 12, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.note;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.Model;
import edu.osu.csml.humdrum.kern.KernException;
import java.io.IOException;

/**
 * Model responsible for represeting bowing markings in a kern note
 * @author Daniel McEnnis
 * 
 */
public class StemBow implements Model {
	String content;
	private Controller master;

	/**
	 * Set of all bow markings attached to a given note
	 */
	private HashSet type;

	/**
	 * Set of define's for different bow markings
	 */
	public static final int UP_STEM = '/';
	public static final int DOWN_STEM = '\\';
	public static final int UP_BOW = 'v';
	public static final int DOWN_BOW = 'u';
	public static final int BREATH = ';';
	public static final int NONE = '\0';
	public static final String[] convert =
		new String[] { "u", "v", "/", "\\\\", ";" };

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if ((obj instanceof StemBow)
			&& (((StemBow) obj).type.equals(this.type))) {
			return true;
		}
		return super.equals(obj);
	}

	public StemBow(Controller master) {
		this.master = master;
		type = new HashSet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.Model#clone(edu.osu.csml.humdrum.Controller)
	 */
	public Object clone(Controller master) {
		StemBow temp = new StemBow(master);
		temp.type = (HashSet) this.type.clone();
		return temp;
	}
	/**
	 * Create a new stem marking object. Verify the markings are internally
	 * correct.
	 * 
	 * @param token
	 *            single note (no spaces) called exclusively by note.
	 */
	public void init(String token) {
		content = token;
		String src = "uv/\\;";
		Matcher match;
		for (int i = 0; i < src.length(); ++i) {
			match =
				Pattern.compile("[^uv/\\\\]*(" + convert[i] + "+).*").matcher(
					token);
			if (match.matches()) {
				checkDuplicate(match);
				type.add(new Integer(src.charAt(i)));
			}
		}
	}

	/**
	 * helper function to report errors for duplicate bowing entries in a
	 * single note.
	 * 
	 * @param match
	 */
	protected void checkDuplicate(Matcher match) {
		try {
			if (match.group(1).length() > 1) {
				throw new KernException(
					"Duplicate entries of " + match.group(1).charAt(0),
					master);
			}
		} catch (KernException e) {
			try {
				master.getErrStream().write(e.toString());
			} catch (IOException e1) {
				;
			}
		}
	}

	/**
	 * Checks to see if there exists a marking of the type 'type' in this
	 * object
	 * 
	 * @param type
	 * @return
	 */
	public boolean isPresent(int type) {
		if (type == NONE) {
			return this.type.isEmpty();
		} else if (this.type.contains(new Integer(type))) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String output() {
		return content;
	}

}
