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
 * Model to record the Duration of a note object.
 * @author Daniel McEnnis
 * 
 */

public class Duration implements Model {
	private String content;
	/**
	 * duration of the current note defined as fraction of a beat. (half is
	 * 0.5, quarter 0.25, etc)
	 */
	private double duration;
	private Controller master;
	/**
	 * existance of modifers that impact the interpretation of a duration
	 */
	private boolean[] specialty = new boolean[] { false, false, false, false };

	public Duration(Controller master) {
		this.master = master;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if ((obj instanceof Duration)
			&& (((Duration) obj).getDuration() == this.getDuration())) {
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
		Duration tmp = new Duration(master);
		tmp.duration = duration;
		return tmp;
	}

	/**
	 * parse a duration, including all modifers that impact how a duration is
	 * registered (ie appoggiaturas, etc)
	 * 
	 * @param token
	 * @throws KernException
	 * @throws KernWarning
	 */
	public void init(String token) throws KernException, KernWarning {
		String[] name =
			new String[] {
				"Appoggiatura",
				"Appoggiatura",
				"Acciaccaturas",
				"Groupetto" };
		String[] check = new String[] { "P", "p", "q", "Q" };
		for (int i = 0; i < name.length; ++i) {
			if (Pattern
				.matches(
					"[^" + check[i] + "]*" + check[i] + "[^" + check[i] + "]*",
					token)) {
				specialty[i] = true;
				for (int j = i + 1; j < name.length; ++j) {
					if (Pattern
						.matches(
							"[^"
								+ check[j]
								+ "]*"
								+ check[j]
								+ "[^"
								+ check[j]
								+ "]*",
							token)) {
						throw new KernException(
							name[i]
								+ " ("
								+ check[i]
								+ ") and "
								+ name[j]
								+ " ("
								+ check[j]
								+ ") are mutually exclusive duration markings",
							master);
					}
				}
			}
		}
		if (specialty[2]) {
			duration = 0.0;
		} else {
			if (token.matches("[^\\d]*(\\d+)[^\\d]+(\\d+).*")) {
				throw new KernException(
					"Multiple durations specified in note",
					master);
			} else if (token.matches(".*[^\\d.]\\..*")) {
				throw new KernException(
					"'.' only permitted after digits describing durations",
					master);
			} else {
				Matcher d =
					Pattern.compile("[^\\d]*(\\d+)(\\.*)[^\\d]*").matcher(
						token);

				if (d.groupCount() < 1) {
					throw new KernException(
						"BUG: Unable to parse duration",
						master);
				} else if (!d.matches()) {
					throw new KernException(
						"BUG: Unable to parse duration",
						master);
				}
				content = d.group(1);
				content += d.group(2);
				int val = Integer.decode(d.group(1)).intValue();

				if (val == 0.0) {
					duration = 1.0;
				} else {
					duration = 1.0 / ((double) val);
				}
				if (d.groupCount() > 1) {
					double addition = 0.0;
					for (int i = 1; i <= d.group(2).length(); ++i) {
						addition += duration * Math.pow(.5, i);
					}
					duration += addition;
				}
			}
		}
	}

	/**
	 * The duration of this object, ignoring modifiers
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * Does this token contain a duration. Preparsed by note to remove spaces.
	 * 
	 * @param token
	 *            string containing current token.
	 * @return is a duration presdent or not
	 */
	public static boolean isPresent(String token) {
		if (token.matches(".*\\d.*") || token.matches(".*\\..*")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Default duration is (double) NaN.
	 * 
	 * @param master
	 * @return NaN
	 */
	public static Duration getDefault(Controller master) {
		Duration d = new Duration(master);
		d.duration = Double.NaN;
		return d;
	}

	/**
	 * is an Acciaccaturas present
	 */
	public boolean isAcciaccaturas() {
		return specialty[2];
	}
	/**
	 * Is a Groupetto present
	 */
	public boolean isGroupetto() {
		return specialty[3];
	}
	/**
	 * Is this a 'non-duration' appoggiatura
	 */
	public boolean isAppoggiatura() {
		return specialty[0];
	}
	/**
	 * Is this note shortened in value because of a 'non duration' appoggiatura
	 */
	public boolean isModifiedByAppoggiatura() {
		return specialty[1];
	}

	@Override
	public String output() {
		return content;
	}
}
