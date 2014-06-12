/*
 * Created on Aug 20, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

import model.CommentToken;



/**
 * Object representing a humdrum spine
 * 
 * @author Daniel McEnnis
 * 
 *  
 */
public class Spine {
	/**
	 * controller for this spine
	 */
	protected EventController master;
	/**
	 * parent of this controller
	 */
	protected Controller parent;
	/**
	 * factory used to decide which interpretations to create
	 */
	protected InterpretationFactory iF;
	/**
	 * factory used to decide which note objects to create
	 */
	protected NoteFactory nF;

	/**
	 * TODO: This constructor really shouldn't be here
	 *  
	 */
	public Spine() {
		this.master = new EventController();
		parent = this.master;
	}

	/**
	 * TODO: THis contstructor really shouldn't be here
	 * 
	 * @param parent
	 */
	public Spine(Controller parent) {
		this.master = new EventController(parent);
		this.parent = parent;
	}

	/**
	 * Create a new spine loaded with the right factories and controllers
	 * 
	 * @param iF
	 * @param nF
	 * @param parent
	 */
	public Spine(
		InterpretationFactory iF,
		NoteFactory nF,
		EventController master) {
		this.master = master;
		this.parent = master.getParent();
		this.iF = iF;
		this.nF = nF;
	}

	/**
	 * @return current context
	 */
	protected Controller getController() {
		return master;
	}

	/**
	 * interpret the given token as data.
	 * 
	 * @param elem
	 *            token to be interpreted
	 * @throws HumdrumWarning
	 * @throws HumdrumException
	 */
	public void addData(String elem) throws HumdrumWarning, HumdrumException {
		nF.getNote(elem, master);
		this.master.fire(EndOfToken.class, new EndOfToken());
	}

	/**
	 * interpret the given token as interpretation
	 * 
	 * @param elem
	 *            token to be interpreted
	 * @throws HumdrumWarning
	 * @throws HumdrumException
	 */
	public void addInterpretation(String elem)
		throws HumdrumWarning, HumdrumException {
		iF.parseInterpretation(elem, master);
		this.master.fire(EndOfToken.class, new EndOfToken());
	}
	
	public void addComment(String elem)throws HumdrumWarning, HumdrumException {
		if(elem.matches("!.*")){
			CommentToken token = new CommentToken(elem,master);
			master.fire(CommentToken.class, token);
			this.master.fire(EndOfToken.class, new EndOfToken());
		}else{
			throw new HumdrumException("Each comment in a comment line must begin with '!'.",master);
		}
	}

	/**
	 * Create a deep copy of this object. Useful for splitting spines
	 * 
	 * @param parent
	 *            new copy can have a different parent from the original
	 * @return deep copy in the context 'parent'
	 */
	public Spine clone(Controller parent) {
		Spine tmp = new Spine(iF, nF, master);
		tmp.master =
			(EventController) this.master.clone(master);
		tmp.parent = parent;
		return tmp;
	}

	/**
	 * can these two spines be merged?
	 * 
	 * @param rhs
	 * @return can these two spines be merged?
	 */
	public boolean isCompatible(Spine rhs) {
		return master.isCompatible(rhs.master);
	}

	/**
	 * Make this spine a deep copy of the argument
	 * 
	 * @param cloned
	 *            spine to be duplicated
	 */
	protected void copy(Spine cloned) {
		cloned.master = (EventController) master.clone(cloned.master);
		cloned.parent = parent;
	}

	/**
	 * take the other spine and collapse them together into one.
	 * 
	 * @param rhs
	 */
	public void merge(Spine rhs) {
		this.master.merge(rhs.master);
	}

	/**
	 * This spine is being shutdown (by end of spine interpretation) let the
	 * children know about it too so they can do whatever cleanup is
	 * neccessary.
	 *  
	 */
	public void terminate() {
		this.master.fire(SpineEOF.class, new SpineEOF(this.master));
	}

	/**
	 * @param spineMaster
	 * @return
	 */
	public Spine prototype(EventController spineMaster) {
		return new Spine(iF.prototype(spineMaster),nF.prototype(spineMaster),spineMaster);
	}
}
