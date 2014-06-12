/*
 * Created on Aug 21, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.note;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.osu.csml.humdrum.*;
import edu.osu.csml.humdrum.kern.*;

/**
 * Model represeting the pitch of a note
 * @author Daniel McEnnis
 * 
 */
public class Pitch implements Model {
	private String content;
	/**
	 * pitch class of a note
	 */
	private int value;
	/**
	 * modification of a note from base pitch
	 */
	private int shift;
	/**
	 * original token representing the pitch
	 */
	private String note;
	private Controller master;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone(Controller master) {
		Pitch tmp = new Pitch(master);
		tmp.value = value;
		tmp.shift = shift;
		tmp.note = note;
		return tmp;
	}

	public Pitch(Controller master) {
		this.master = master;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Pitch) {
			if ((((Pitch) obj).value == this.value)
				&& (((Pitch) obj).shift == this.shift)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Parse into pitch class and pitch modifier. Check for multiple and/or
	 * duplicate pitches. Check for multiple and/or duplicate accidentals.
	 * Report any syntax errors found.
	 */
	public void init(String token) throws KernException, KernWarning {
		content = token;
		final String[] dualNote =
			new String[] {
				"[[^a-g]&&[^A-G]]*(a+)[[^a-g]&&[^A-G]]*([b-g[A-G]]+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*(b+)[[^a-g]&&[^A-G]]*([a[c-g][A-G]]+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*(c+)[[^a-g]&&[^A-G]]*([ab[d-g][A-G]]+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*(d+)[[^a-g]&&[^A-G]]*([[a-c][e-g][A-G]]+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*(e+)[[^a-g]&&[^A-G]]*([[a-d]fg[A-G]]+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*(f+)[[^a-g]&&[^A-G]]*([[a-e]g[A-G]]+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*(g+)[[^a-g]&&[^A-G]]*([[a-d][A-G]]+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*(A+)[[^a-g]&&[^A-G]]*([[a-g][B-G]]+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*(B+)[[^a-g]&&[^A-G]]*([[a-g]A[C-G]]+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*(C+)[[^a-g]&&[^A-G]]*([[a-g]AB[D-G]]+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*(D+)[[^a-g]&&[^A-G]]*([[a-g][A-C][E-G]]+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*(E+)[[^a-g]&&[^A-G]]*([[a-g][A-D]FG]+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*(F+)[[^a-g]&&[^A-G]]*([[a-g][A-E]G]+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*(G+)[[^a-g]&&[^A-G]]*([[a-g][A-F]]+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*([[b-g][A-G]]+)[[^a-g]&&[^A-G]]*(a+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*([a[c-g][A-G]]+)[[^a-g]&&[^A-G]]*(b+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*([ab[d-g][A-G]]+)[[^a-g]&&[^A-G]]*(c+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*([[a-c][e-g][A-G]]+)[[^a-g]&&[^A-G]]*(d+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*([[a-d]fg[A-G]]+)[[^a-g]&&[^A-G]]*(e+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*([[a-e]g[A-G]]+)[[^a-g]&&[^A-G]]*(f+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*([[a-d][A-G]]+)[[^a-g]&&[^A-G]]*(g+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*([[a-g][B-G]]+)[[^a-g]&&[^A-G]]*(A+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*([[a-g]A[C-G]]+)[[^a-g]&&[^A-G]]*(B+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*([[a-g]AB[D-G]]+)[[^a-g]&&[^A-G]]*(C+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*([[a-g][A-CE-G]]+)[[^a-g]&&[^A-G]]*(D+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*([[a-g][A-DFG]]+)[[^a-g]&&[^A-G]]*(E+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*([[a-g][A-E]G]+)[[^a-g]&&[^A-G]]*(F+)[[^a-g]&&[^A-G]]*",
				"[[^a-g]&&[^A-G]]*([[a-g][A-F]]+)[[^a-g]&&[^A-G]]*(G+)[[^a-g]&&[^A-G]]*" };
		for (int i = 0; i < dualNote.length; ++i) {
			if (token.matches(dualNote[i])) {
				throw new KernException("Multiple pitches in one note", master);
			}
		}

		final String[] accidental =
			new String[] {
				"[^#]*#+[^#-n]*[-n]+[^-n]*",
				"[^-n]*[-n]+[^#-n]*#+[^#]*",
				"[^n]*n+[^#-n]*[-#]+[^-#]*",
				"[^-#]*[-#]+[^#-n]*n+[^n]*",
				"[^-]*-+[^#-n]*[#n]+[^#n]*",
				"[^#n]*[#n]+[^#-n]*-+[^-]*" };
		for (int i = 0; i < accidental.length; ++i) {
			if (token.matches(accidental[i])) {
				throw new KernException(
					"Conflicting accidentals in note",
					master);
			}
		}
		if (token.matches(".*nn+.*")) {
			throw new KernException("Multiple naturals in one note", master);
		}

		Pattern base = Pattern.compile("[[^a-g]&&[^A-G]]*([[a-g][A-G]]+).*");
		Pattern sharp = Pattern.compile("[^#]*(#+).*");
		Pattern flat = Pattern.compile("[^-]*(-+).*");
		Matcher b = base.matcher(token);
		Matcher s = sharp.matcher(token);
		Matcher f = flat.matcher(token);
		if (!b.matches()) {
			throw new KernException(
				"BUG: identified as note without a pitch",
				master);
		}
		if (b.groupCount() < 1) {
			throw new KernException("BUG: Incorrect parsing of pitch", master);
		}
		content = b.group(1);
		switch ((b.group(1)).charAt(0)) {
			case 'a' :
				value = -3 + 12 * (b.group(1).length() - 1);
				break;

			case 'b' :
				value = -1 + 12 * (b.group(1).length() - 1);
				break;

			case 'c' :
				value = 12 * (b.group(1).length() - 1);
				break;

			case 'd' :
				value = 2 + 12 * (b.group(1).length() - 1);
				break;

			case 'e' :
				value = 4 + 12 * (b.group(1).length() - 1);
				break;

			case 'f' :
				value = 5 + 12 * (b.group(1).length() - 1);
				break;

			case 'g' :
				value = 7 + 12 * (b.group(1).length() - 1);
				break;

			case 'A' :
				value = -3 - 12 * (b.group(1).length());
				break;

			case 'B' :
				value = -1 - 12 * (b.group(1).length());
				break;

			case 'C' :
				value = -12 * (b.group(1).length());
				break;

			case 'D' :
				value = 2 - 12 * (b.group(1).length());
				break;

			case 'E' :
				value = 4 - 12 * (b.group(1).length());
				break;

			case 'F' :
				value = 5 - 12 * (b.group(1).length());
				break;

			case 'G' :
				value = 7 - 12 * (b.group(1).length());
				break;

			default :
				throw new KernException(
					"BUG: Kern note entry parsed incorrectly",
					master);
		}

		note = b.group(1);

		if (s.matches()) {
			shift = s.group(1).length();
			value += shift;
		} else if (f.matches()) {
			shift = -1 * f.group(1).length();
			value += shift;
		} else {
			shift = 0;
		}
	}

	/**
	 * returns the raw token used to parse this note
	 * 
	 * @return
	 */
	public String getNote() {
		return note;
	}

	/**
	 * returns the pitch class of this note
	 * 
	 * @return
	 */
	public int getPitch() {
		return value;
	}

	/**
	 * returns the modification to pitch class due to accidentals
	 * 
	 * @return
	 */
	public int getAccidental() {
		return shift;
	}

	@Override
	public String output() {
		return content;
	}

}
