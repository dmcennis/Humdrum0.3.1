/*
 * Created on Sep 12, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.note;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.osu.csml.humdrum.Controller;
import edu.osu.csml.humdrum.Model;
import edu.osu.csml.humdrum.kern.KernException;
import java.io.IOException;

/**
 * Model representing the tie in a tied note
 * @author Daniel McEnnis
 * 
 */
public class Tie implements Model {
	String content;
	private Controller master;
	/**
	 * initially, no tie has been found
	 */
	private int type = NONE;

	/**
	 * collection of define's for different tie states
	 */
	public static final int OPEN = '[';
	public static final int CONTINUE = '_';
	public static final int END = ']';
	public static final int NONE = '\0';
	public static final String[] convert = new String[] { "\\[", "_", "\\]" };

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if ((obj instanceof Tie) && (((Tie) obj).type == this.type)) {
			return true;
		}
		return false;
	}

	/**
	 * info for properly printing a tie object
	 */
	public String toString() {
		if (type == OPEN) {
			return "\\[";
		} else if (type == CONTINUE) {
			return "_";
		} else if (type == END) {
			return "\\]";
		} else if (type == NONE) {
			return "";
		} else {
			return "BUG: Unknown type in Tie.toString()";
		}
	}

	public Tie(Controller master) {
		this.master = master;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.Model#clone(edu.osu.csml.humdrum.Controller)
	 */
	public Object clone(Controller master) {
		Tie tmp = new Tie(master);
		tmp.type = this.type;
		return tmp;
	}

	/**
	 * for each type of tie symbol, attempt to lacate within the object. This
	 * is only called from Note, so all spaces and tabs have been removed from
	 * the token before it gets here.
	 * 
	 * @param token
	 *            single note - no spaces in token
	 * @throws KernException
	 */
	public void init(String token) throws KernException {
		content=token;
		String str = "[_]";
		int placeholder = -1;
		for (int i = 0; i < str.length(); ++i) {
			Matcher m =
				Pattern.compile(
					"[^\\[\\]_]*([" + convert[i] + "]+).*").matcher(
					token);
			Matcher extra =
				Pattern
					.compile(
						"[^\\[\\]_]*["
							+ convert[i]
							+ "]+[^\\[\\]_]*["
							+ convert[i]
							+ "]+.*")
					.matcher(token);
			if (m.matches()) {
				checkDuplicate(m);
				if (placeholder != -1) {
					throw new KernException(
						"Mutually exclusive tokens '"
							+ convert[placeholder]
							+ " and "
							+ convert[i]
							+ "present",
						master);
				} else if (extra.matches()) {
					try {
						throw new KernException(
							"Ties can only appear once per duration",
							master);
					} catch (KernException e) {
						try {
							master.getErrStream().write(e.toString());
						} catch (IOException e1) {
							;
						}
					}
				}
				placeholder = i;
				type = str.charAt(i);
			}
		}
	}

	/**
	 * Verify that there are no more than 1 match of the expression described
	 * by match.
	 * 
	 * @param match
	 *            applied regular expression describing results from one of the
	 *            regular expressions defined in init()
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
	 * checks the state of the argument to verify that the Tie that is passed
	 * in can legally precede this tie. Used by TieCheck interpretation to
	 * preserve data hiding.
	 * 
	 * @param rhs
	 * @return is the given state valid.
	 */
	public boolean canFollow(Tie rhs) {
		if ((rhs == null) || (rhs.type == NONE) || (rhs.type == END)) {
			if ((this.type == OPEN) || (this.type == NONE)) {
				return true;
			} else {
				return false;
			}
		} else if ((rhs.type == OPEN) || (rhs.type == CONTINUE)) {
			if ((type == CONTINUE) || (type == END)) {
				return true;
			} else {
				return false;
			}
		}
		// BUG: Should never get here
		return false;
	}

	/**
	 * Check if this tie has the state 'type'
	 * @param type expected state
	 * @return whether the state of this tie matches the expected state
	 */
	public boolean isPresent(int type) {
		if (this.type == type) {
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
