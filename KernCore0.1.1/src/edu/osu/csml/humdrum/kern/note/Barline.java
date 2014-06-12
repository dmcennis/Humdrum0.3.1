/*
 * Created on Aug 20, 2003
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
 * Handle parsing barlines in a token
 * @author Daniel McEnnis
 * 
 */
public class Barline extends Note {
	
	private String content;
	
	/**
	 * Measure number present  
	 * -1 is none given
	 */
	private int number = -1;
	/**
	 * Measure letter present 
	 * empty string implies none present
	 */
	private String a = "";
	/**
	 * Is this token a double or single barline
	 */
	private boolean doubleBarline = false;
	/**
	 * does this barline contain a grand pause marking
	 */
	private boolean pause = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if ((obj instanceof Barline)
			&& (((Barline) obj).number == this.number)
			&& (((Barline) obj).a).equals(this.a)) {
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
		Barline tmp = new Barline(master);
		tmp.number = this.number;
		tmp.a = this.a;
		tmp.pause = this.pause;
		tmp.doubleBarline = this.doubleBarline;
		return tmp;
	}

	/**
	 * Check if this object is a double barline or not
	 * 
	 * @return am I a double barline or not
	 */
	public boolean isDouble() {
		return doubleBarline;
	}

	/**
	 * Create a new Barline object. Check if syntax is corect and load the
	 * various aspects of the barline into the object.
	 */
	public void init(String token) throws HumdrumException, HumdrumWarning {
		super.init(token);
		content = token;
		if (token.matches("==.*")) {
			doubleBarline = true;
		} else {
			Pattern num =
				Pattern.compile("=[^\\d]*(\\d+)[^\\da-z]*([a-z]?).*(;?).*");
			Matcher n = num.matcher(token);
			Pattern notNumber = Pattern.compile("=[^\\d]*(;?).*");
			Matcher nN = notNumber.matcher(token);

			if (n.matches()) {
				number = Integer.parseInt(n.group(1));
				if (n.group(2).length() > 0) {
					a = n.group(2);
				} else {
					a = "\0";
				}
				if (n.group(3).length() > 0) {
					pause = true;
				}
			} else if (nN.matches()) {
				if (nN.group(1).length() > 0) {
					pause = true;
				}
			} else {
				throw new KernException("Syntax error in barline ", handler);
			}
		}
		handler.fire(Barline.class, this);
	}

	/**
	 * is the current barline of a higher measure number than the Barline
	 * provided.
	 * 
	 * @param previous
	 *            earlier barline to be compared against.
	 * @return is 'this' greater than 'previous'
	 */
	public boolean isGreater(Barline previous) {
		if (previous.number < 0) {
			return true;
		}
		if (this.number > previous.number) {
			return true;
		} else if (
			(this.number == previous.number)
				&& (a.charAt(0) > previous.a.charAt(0))) {
			return true;
		} else if ((this.number == -1) && (previous.number == -1)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public Barline(Controller handler) {
		super(handler);
	}

	@Override
	public String output() {
		return content;
	}
}
