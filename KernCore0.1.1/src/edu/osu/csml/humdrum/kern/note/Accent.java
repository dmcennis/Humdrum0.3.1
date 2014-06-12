/*
 * Created on Aug 21, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.note;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.osu.csml.humdrum.*;
import edu.osu.csml.humdrum.kern.*;
/**
 * Describes all possible accents on a note
 * @author Daniel McEnnis
 * 
 */
public class Accent implements Model {

	private String accent;
	
	private Controller master;
	/**
	 * Holds all accents present on this note
	 */
	private HashSet type = new HashSet();

	/**
	 * defines mapping all ornaments to an integer
	 */
	public static final int GENERIC_ORNAMENT = 'O';
	public static final int GENERIC_ARTICULATION = 'I';
	public static final int MORDENT_WHOLE = 'M';
	public static final int MORDENT_SEMI = 'm';
	public static final int TURN = 'S';
	public static final int WAGNERIAN_TURN = '$';
	public static final int TRILL_WHOLE = 'T';
	public static final int TRILL_SEMI = 't';
	public static final int INVERTED_MORDENT_WHOLE = 'W';
	public static final int INVERTED_MORDENT_SEMI = 'w';
	public static final int ORNAMENTAL_TURN = 'R';
	public static final int SFOZANDO = 'z';
	public static final int STACCATO = '\'';
	public static final int PIZZICATO = '\"';
	public static final int ATACCA = '`';
	public static final int TENUTO = '~';
	public static final int ACCENT = '^';
	public static final int ARPEGGIATION = ':';
	public static final int NONE = '\0';
	protected static HashMap convert = new HashMap();

	/**
	 * create accent object, establish HashMap with proper default values
	 * 
	 * @param master
	 */
	public Accent(Controller master) {
		this.master = master;
		convert.put("R", new Integer(ORNAMENTAL_TURN));
		convert.put("w", new Integer(INVERTED_MORDENT_SEMI));
		convert.put("W", new Integer(INVERTED_MORDENT_WHOLE));
		convert.put("t", new Integer(TRILL_SEMI));
		convert.put("T", new Integer(TRILL_WHOLE));
		convert.put("\\$", new Integer(WAGNERIAN_TURN));
		convert.put("S", new Integer(TURN));
		convert.put("m", new Integer(MORDENT_SEMI));
		convert.put("M", new Integer(MORDENT_WHOLE));
		convert.put("z", new Integer(SFOZANDO));
		convert.put(":", new Integer(ARPEGGIATION));
		convert.put("\\^", new Integer(ACCENT));
		convert.put("~", new Integer(TENUTO));
		convert.put("`", new Integer(ATACCA));
		convert.put("\"", new Integer(PIZZICATO));
		convert.put("\'", new Integer(STACCATO));
		convert.put("I", new Integer(GENERIC_ARTICULATION));
		convert.put("O", new Integer(GENERIC_ORNAMENT));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone(Controller master) {
		Accent nw = new Accent(master);
		nw.type = (HashSet) this.type.clone();
		return nw;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if ((obj instanceof Accent)
			&& (((Accent) obj).type.equals(this.type))) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 
	 * parse token- assuming no spaces in input - and load all valid accents
	 * into type HashSet. Notify user of all errors
	 * 
	 * @param token
	 * @throws KernWarning
	 * @throws KernException
	 */
	public void init(String token) throws KernWarning, KernException {

		// store the input for later output
		accent = token;
	
		// For accents and ornaments - check for valid ornaments in the correct
		// order. (For I and O, verify that they are the only one of that type
		// present)
		String src[][] =
			new String[][] {
				{ "R", "w", "W", "t", "T", "\\$", "S", "m", "M" },
				{
				"z", ":", "\\^", "~", "`", "\"", "\'" }
		};
		String generic[] = new String[] { "O", "I" };
		Matcher match;

		for (int set = 0; set < src.length; ++set) {
			boolean genericPresent = false;
			match =
				Pattern.compile(".*(" + generic[set] + "+).*").matcher(token);
			if (match.matches()) {
				type.add(convert.get(generic[set]));
				checkDuplicate(match);
				genericPresent = true;
			}
			for (int i = 0; i < src[set].length; ++i) {
				match =
					Pattern.compile(".*(" + src[set][i] + "+).*").matcher(
						token);
				if (match.matches()) {
					checkDuplicate(match);
					try {
						if (genericPresent) {
							throw new KernException(
								"Cannot have both generic ("
									+ generic[set]
									+ ") and specific ("
									+ src[set][i]
									+ ") in the same token",
								master);
						}
						for (int j = 0; j < i; ++j) {
							if (Pattern
								.compile(".*" + src[set][j] + ".*")
								.matcher(token)
								.matches()) {
								throw new KernException(
									src[set][j]
										+ "must be after "
										+ src[set][i],
									master);
							}
						} // for each character before src[type].charAt(i)
						if (type.contains(convert.get(src[set][i]))) {
							throw new KernException(
								"Ornaments/Articulation "
									+ src[set][i]
									+ " may occur only once per note",
								master);
						} else {
							type.add(convert.get(src[set][i]));
						}
					} catch (KernException e) {
						try {
							master.getErrStream().write(e.toString());
						} catch (IOException e1) {
							;
						} // catch ignored IOException
					} // catch clause for KernException
				} // if articulation src[type].charAt(i) is present
			} // for each character in src[type]
		} // for each string in src
	} // init

	/**
	 * verify that one and only one of each type exists.
	 * @param match regular expression describing one kind of accent
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
	 * Is a particular accent present on this note
	 * @param type kind of accent interested in
	 * @return does a particular accent exist on this note
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
		return accent;
	}
}
