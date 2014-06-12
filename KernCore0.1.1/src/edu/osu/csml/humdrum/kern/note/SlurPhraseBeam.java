/*
 * Created on Sep 12, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.note;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.Model;
import edu.osu.csml.humdrum.kern.KernException;

/**
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 * @author Daniel McEnnis
 * 
 */
public class SlurPhraseBeam implements Model {
	String content;
	Controller master;
	HashMap type;
	boolean empty = true;
	static public final int OPEN_PHRASE = 0;
	static public final int OPEN_ELIDED_PHRASE = 1;
	static public final int OPEN_SLUR = 2;
	static public final int OPEN_ELIDED_SLUR = 3;
	static public final int OPEN_BEAM = 4;
	static public final int CLOSE_BEAM = 5;
	static public final int CLOSE_ELIDED_SLUR = 6;
	static public final int CLOSE_SLUR = 7;
	static public final int CLOSE_ELIDED_PHRASE = 8;
	static public final int CLOSE_PHRASE = 9;
	static public final int NONE = -1;
	private HashMap convert;
	static private final String base = "[^(){}&LJ]*";
	static public final int[][] typeOfEntry =
		new int[][] { { OPEN_PHRASE, OPEN_SLUR, OPEN_BEAM }, {
			CLOSE_PHRASE, CLOSE_SLUR, CLOSE_BEAM }, {
			OPEN_ELIDED_PHRASE, OPEN_ELIDED_SLUR }, {
			CLOSE_ELIDED_PHRASE, CLOSE_ELIDED_SLUR }
	};
	static public String[] name = new String[] { "Phrase", "Slur", "Beam" };

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if ((obj instanceof SlurPhraseBeam)
			&& (((SlurPhraseBeam) obj).type.equals(this.type))) {
			return true;
		}
		return false;
	}

	/**
	 * Create an object to handle Slurs, Phrases, and Beams. Also preloads the
	 * HashMaps with the default zero entries per class.
	 */
	public SlurPhraseBeam(Controller master) {
		this.master = master;
		type = new HashMap();
		convert = new HashMap();
		//Load OPEN_PHRASE
		convert.put(new Integer(OPEN_PHRASE), "[^&]?[{]");
		type.put(new Integer(OPEN_PHRASE), new Integer(0));
		// Load OPEN_ELIDED_PHRASE
		convert.put(new Integer(OPEN_ELIDED_PHRASE), "&[{]");
		type.put(new Integer(OPEN_ELIDED_PHRASE), new Integer(0));
		// Load OPEN_SLUR
		convert.put(new Integer(OPEN_SLUR), "[^&]?[(]");
		type.put(new Integer(OPEN_SLUR), new Integer(0));
		// Load OPEN_ELIDED_SLUR
		convert.put(new Integer(OPEN_ELIDED_SLUR), "&[(]");
		type.put(new Integer(OPEN_ELIDED_SLUR), new Integer(0));
		// Load OPEN_BEAM
		convert.put(new Integer(OPEN_BEAM), "L");
		type.put(new Integer(OPEN_BEAM), new Integer(0));
		// Load CLOSE_BEAM
		convert.put(new Integer(CLOSE_BEAM), "J");
		type.put(new Integer(CLOSE_BEAM), new Integer(0));
		// Load CLOSE_ELIDED_SLUR
		convert.put(new Integer(CLOSE_ELIDED_SLUR), "&[)]");
		type.put(new Integer(CLOSE_ELIDED_SLUR), new Integer(0));
		// Load CLOSE_SLUR
		convert.put(new Integer(CLOSE_SLUR), "[^&]?[)]");
		type.put(new Integer(CLOSE_SLUR), new Integer(0));
		// Load CLOSE_ELIDED_PHRASE
		convert.put(new Integer(CLOSE_ELIDED_PHRASE), "&[}]");
		type.put(new Integer(CLOSE_ELIDED_PHRASE), new Integer(0));
		// Load CLOSE_PHRASE
		convert.put(new Integer(CLOSE_PHRASE), "[^&]?[}]");
		type.put(new Integer(CLOSE_PHRASE), new Integer(0));

		convert.put(new Integer(10), ".*");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.Model#clone(edu.osu.csml.humdrum.Controller)
	 */
	public Object clone(Controller master) {
		SlurPhraseBeam tmp = new SlurPhraseBeam(master);
		tmp.type = (HashMap) this.type.clone();
		return tmp;
	}

	/**
	 * parse the token, establishing how many of each kind of slur,phrase, or
	 * beam is present. Should only be called from Note after all spaces have
	 * been removed from the token.
	 * 
	 * @param token
	 * @throws KernException
	 */
	public void init(String token) throws KernException {
		content = token;
		if (Pattern.matches("[^L]*L+[^L]+L+.*", token)) {
			throw new KernException(
				"Open Beam signifiers must be consecutive",
				master);
		} else if (Pattern.matches("[^J]*J+[^J]+J+.*", token)) {
			throw new KernException(
				"Close Beam signifiers must be consecutive",
				master);
		}
		Matcher match;
		int i = 0;
		String tmp = token;
		while (tmp.length() > 0) {
			match =
				Pattern
					.compile(
						base
							+ "("
							+ (String) convert.get(new Integer(i))
							+ ")(.*)")
					.matcher(tmp);
			if (match.matches()) {
				tmp = match.group(2);
				increment(i);
				empty = false;
				i = 0;
			} else {
				++i;
			}
			if (i > 9) {
				tmp = "";
			}
		}
		sanityCheck();
	}

	/**
	 * informs how many of slurs, phrases, or beams have been decoded.
	 * 
	 * @param type
	 *            Either slur, phrase, or beam defined via public defines
	 * @return number of entries - if a valid type - or +MAX_INT if completely
	 *         empty, or -INFINITY if not empty and an invalid type
	 */
	public int get(int type) {
		Integer tmp = new Integer(type);
		if (this.type.containsKey(tmp)) {
			return ((Integer) this.type.get(tmp)).intValue();
		} else if (empty) {
			return Integer.MAX_VALUE;
		} else {
			return Integer.MIN_VALUE;
		}
	}

	/**
	 * helper method for increasing the number of type i found in a given token
	 * 
	 * @param i
	 *            type of object found - either Slur, phrase, or beam. Silently
	 *            fails if invlaid type given
	 */
	protected void increment(int i) {
		if (this.type.containsKey(new Integer(i))) {
			type.put(
				new Integer(i),
				new Integer(
					((Integer) type.get(new Integer(i))).intValue() + 1));
		}
	}

	/**
	 * checks if the token described an invalid token given the current state
	 * of the spine.
	 * 
	 * @throws KernException
	 */
	protected void sanityCheck() throws KernException {
		for (int i = 0; i < typeOfEntry[3].length; ++i) {
			if (get(typeOfEntry[2][i]) > 1) {
				throw new KernException(
					"Elided " + name[i] + " can not be nested",
					master);
			} else if (get(typeOfEntry[3][i]) > 1) {
				throw new KernException(
					"Closing non-existant elided " + name[i],
					master);
			} else if (
				(get(typeOfEntry[2][i]) > 0)
					&& (get(typeOfEntry[3][i]) == 0)
					&& (get(typeOfEntry[1][i]) > 0)) {
				throw new KernException(
					"Can not close an elided "
						+ name[i]
						+ " using a non-eliding close",
					master);
			} else if (
				(get(typeOfEntry[2][i]) == 0)
					&& (get(typeOfEntry[3][i]) > 0)
					&& (get(typeOfEntry[0][i]) > 0)) {
				throw new KernException(
					"Can not close a normal "
						+ name[i]
						+ "with an elided close",
					master);
			}
		}

	}

	@Override
	public String output() {
		return content;
	}
}
