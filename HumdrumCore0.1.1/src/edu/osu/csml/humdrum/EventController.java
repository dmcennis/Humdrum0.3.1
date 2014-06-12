/*
 * Created on Aug 23, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 * Fills the dispatcher function in a client-dispatcher-reciever pattern (POSA1 p323). The clients
 * are the items created by the parsing algorithm. The recievers are the
 * interpretations that monitor and act upon the events passed through these
 * controllers. 
 * 
 * @author Daniel McEnnis
 * 
 */
public class EventController implements java.lang.Cloneable, Controller {
	/**
	 * each controller is placed within a hierarchy. This hierarchy utilizes
	 * the Chain of Responsibility pattern (Design Patterns p223) where each stage is linked to a
	 * different level of context - token, exclusive interpretation (ie. kern),
	 * or humdrum.
	 */
	protected Controller parent = null;
	/**
	 * variable that records the relationships between models and the views
	 * which are listening for them. Utilizes a push model.
	 */
	private HashMap mh = null;

	/**
	 * Retireve the correct stream for printing error messages
	 * 
	 * @see edu.osu.csml.humdrum.Controller#getErrStream()
	 */
	public Writer getErrStream() {
		if ((parent != null) && (parent != this)) {
			return parent.getErrStream();
		}
		return null;
	}

	/**
	 * When 2 spines are merged, the interpretations should aso be merged. This
	 * may result in an unstable/inconsistant setup. While not strictly
	 * forbidden, such ambigous mergings have no defined behavior, and are
	 * treated as errors. All non-conflicting interpretations (views) are
	 * placed in a single new controller linked to the merged spine.
	 * 
	 * @param type
	 */
	public void merge(EventController type) {
		Map.Entry left;
		Map.Entry right;
		Class rightClass;
		Vector leftData;
		Vector rightData;

		for (Iterator it = mh.entrySet().iterator(); it.hasNext();) {
			right = (Map.Entry) it.next();
			rightClass = (Class) right.getKey();
			rightData = (Vector) right.getValue();
			// If no interpretation in this objest is registered for this
			// class, register all from the other without needing to worry
			// about duplicate entries
			// We need to check for each entry if a duplicate has been
			// registered already
			leftData = (Vector) mh.get(rightClass);
			boolean equal = false;
			for (int i = 0; i < rightData.size(); ++i) {
				equal = false;
				for (int j = 0; j < leftData.size(); ++j) {
					if (((View) rightData.get(i)).equals(leftData.get(i))) {
						equal = true;
					}
				} // for every interpretation registered on this class
				if (!equal) {
					this.register(rightClass, (View) rightData.get(i));
				}
			} // for all entities registered for 'rightClass' class in the
			// right spine
		} // for every class type registered on the right spine
	}

	/**
	 * Get from the root of the hierarchy tree where in the document we are at
	 * the moment
	 * 
	 * @see edu.osu.csml.humdrum.Controller#getLineNumber()
	 */
	public int getLineNumber() {
		if (parent != null) {
			return parent.getLineNumber();
		}
		return 0;
	}

	/**
	 * Recieve a reference to the next higher element in the hierarchy. Used to
	 * send messages to higher level controllers.
	 * 
	 * @see edu.osu.csml.humdrum.Controller#getParent()
	 */
	public Controller getParent() {
		return parent;
	}

	/**
	 * Get from root of hierarchy where we are in the document right now.
	 * 
	 * @see edu.osu.csml.humdrum.Controller#getSpineNumber()
	 */
	public int getSpineNumber() {
		if (parent != null) {
			return parent.getSpineNumber();
		}
		return 0;
	}

	/**
	 * Create a deep copy of this object. Useful for implementing a proper
	 * split operation.
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone(Controller master) {
		// TODO handle dups properly?
		HashMap temp = new HashMap(mh);
		HashMap dups = new HashMap();
		for (Iterator i = temp.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			if (!dups.containsKey(entry.getKey())) {
				dups.put(
					entry.getValue(),
					((View) entry.getValue()).clone(master));
			}
			entry.setValue(((View) dups.get(entry.getValue())));
		}
		return temp;
	}

	/**
	 * self explanatory.
	 * 
	 * Implementation note: order is irellevant and non-traditional definition
	 * of 'equals' in interpretations can result in unexpected results. If you
	 * are planning on using equals, make sure you understand its meaning for
	 * each interpretation.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof EventController)) {
			return false;
		}
		HashMap tmp = ((EventController) obj).mh;
		Map.Entry left;
		Class leftClass;
		Vector leftData;
		Map.Entry right;
		Vector rightData;
		boolean match;

		for (Iterator entry = mh.entrySet().iterator(); entry.hasNext();) {
			left = (Map.Entry) entry.next();
			leftClass = (Class) left.getKey();
			if (!tmp.containsKey(leftClass)) {
				return false;
			}
			leftData = (Vector) (mh.get(leftClass));
			rightData = (Vector) (tmp.get(leftClass));
			for (int i = 0; i < leftData.size(); ++i) {
				match = false;
				for (int j = 0; j < rightData.size(); ++j) {
					if (leftData.get(i).equals(rightData.get(j))) {
						match = true;
					}
				}
				if (match == false) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Identifies whether or not two controllers contain conflicting (usually
	 * same type, non-equal) interpretations or not. A true result indicates
	 * that the two controllers can be safely merged. A false result implies
	 * that a merge will fail.
	 * 
	 * @param rhs
	 *            other controller to be compared against
	 * @return whether the 2 controllers can be safely merged
	 */
	public boolean isCompatible(EventController rhs) {
		HashMap tmp = rhs.mh;
		Vector left;
		Vector right;
		for (Iterator i = mh.entrySet().iterator(); i.hasNext();) {
			for (Iterator j = tmp.entrySet().iterator(); j.hasNext();) {
				left = ((Vector) ((Map.Entry) i.next()).getValue());
				right = ((Vector) ((Map.Entry) j.next()).getValue());

				for (int k = 0; k < left.size(); k++) {
					for (int l = 0; l < right.size(); l++) {
						if (!((Interpretation) left.elementAt(k))
							.isCompatible(
								(Interpretation) right.elementAt(l))) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Creates a new controller that is a child of the controller provided
	 * 
	 * @param master
	 *            parent of newly created controller
	 */
	public EventController(Controller master) {
		mh = new HashMap();
		parent = master;
	}

	/**
	 * generate a controller without a parent. Use with caution.
	 *  
	 */
	public EventController() {
		mh = new HashMap();
	}

	/**
	 * models call this function to trigger processing by any views listening
	 * for this object.
	 */
	public void fire(Class name, Object trigger) {

		//Extremely useful to activate when debugging
		//System.out.println(name.getName() +" fired at " +this.getClass().getName());
		if (mh.containsKey(name)) {
			Vector temp;
			temp = ((Vector) mh.get(name));
			View rec;
			for (Iterator i = temp.iterator(); i.hasNext();) {
				rec = ((View) i.next());
				try {
					rec.fire(name, trigger);
				} catch (HumdrumException e) {
					try {
						getErrStream().write(e.toString());
					} catch (IOException e1) {
						;
					}
				} catch (HumdrumWarning e) {
					try {
						getErrStream().write(e.toString());
					} catch (IOException e1) {
						;
					}
				}
			}
		}
		FireParent(name,trigger);
	}

	/**
	 * Create a stub so Document.MasterController can overide it
	 * @param name
	 * @param trigger
	 */
	protected void FireParent(Class name, Object trigger) {
		parent.fire(name,trigger);
	}

	/**
	 * Mechanism by which views register an interest in events created by
	 * particular models. 
	 * 
	 * @param trigger name of model type to be registered for
	 * @param rec reference of the view to be called.
	 */
	public void register(Class trigger, View rec) {
		if (mh.containsKey(trigger)) {
			((Vector) mh.get(trigger)).add(rec);
		} else {
			Vector temp = new Vector();
			temp.add(rec);
			mh.put(trigger, temp);
		}
	}

}
