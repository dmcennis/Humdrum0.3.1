/*
 * Created on Aug 28, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

import java.util.HashSet;

import model.HumdrumMultiStop;
import model.HumdrumToken;
import model.NullNote;
import edu.osu.csml.humdrum.interpretation.Generic;

/**
 * Default interpretation used when the exclusive interpretation present does
 * not have a registered parser
 * @author Daniel McEnnis
 * 
 */
public class GenericSpine extends Spine {

	/**
	 * Create a new spine. All are identical, but must conform to the syntax of
	 * the other spines
	 * 
	 */
	public Spine clone(Controller parent) {
		return new GenericSpine();
	}

	/**
	 * Default used - irrelevant to function
	 */
	public GenericSpine() {
		super();
	}

	/**
	 * Default used - parents are nice, but already handled by super class.
	 * 
	 * @param parent
	 */
	public GenericSpine(Controller parent) {
		super(parent);
	}

	/**
	 * No interpretations are parsed, so nothing to pass back. Keep others
	 * happy and pass back a blank HashMap instead.
	 * 
	 * @see edu.osu.csml.humdrum.Spine#getInterpretations()
	 */
	protected HashSet getInterpretations() {
		return new HashSet();
	}

	/**
	 * No-op.  Don't care whats present, no interpretation is being done anyways.
	 * 
	 * @see edu.osu.csml.humdrum.Spine#addData(java.lang.String)
	 */
	public void addData(String elem) throws HumdrumWarning, HumdrumException {
		if(elem.matches(".* .*")){
			HumdrumMultiStop h = new HumdrumMultiStop(master);
			h.init(elem);
		}else if(elem.matches("\\.")){
			NullNote n = new NullNote(master);
			n.init(elem);
		}else{
			HumdrumToken h = new HumdrumToken(master);
			h.init(elem);
		}
	}

	/**
	 * Fire back the interpretations for those that care.
	 * 
	 * @see edu.osu.csml.humdrum.Spine#addInterpretation(java.lang.String)
	 */
	public void addInterpretation(String elem) {
		super.master.fire(Interpretation.class, new Generic(elem));
	}
	
	/**
	 * Parse comments.  Throw an error if they don't match Humdrum spec (begin with a '!').
	 */
	public void addComment(String elem) throws HumdrumWarning, HumdrumException{
		super.addComment(elem);
	}

	/**
	 * call default op
	 * 
	 * @see edu.osu.csml.humdrum.Controller#getParent()
	 */
	public Controller getParent() {
		return parent;
	}

	/**
	 * return a smart default if we're left on our own, otherwise defer to parents
	 * 
	 * @see edu.osu.csml.humdrum.Controller#getLineNumber()
	 */
	public int getLineNumber() {
		if (parent == null) {
			return 0;
		}
		return parent.getLineNumber();
	}

	/**
	 * Defer to parent if possible, otherwise return a smart default
	 * 
	 * @see edu.osu.csml.humdrum.Controller#getSpineNumber()
	 */
	public int getSpineNumber() {
		if (parent == null) {
			return 0;
		}
		return parent.getSpineNumber();
	}

}
