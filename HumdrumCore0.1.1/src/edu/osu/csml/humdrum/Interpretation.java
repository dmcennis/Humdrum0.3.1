/*
 * Created on Aug 20, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum;

/**
 * interface for evaluating non-exclusive interpretations the only required
 * info is that compatibility checks can be made. It is assumed that any
 * interpreting will involve interactions with controllers. However,
 * interpretations are not required to do anything, so the prtototypes are kept
 * deliberatly simple.
 * @author Daniel McEnnis
 * 
 */
public interface Interpretation extends View {
	/**
	 * can this interpretation coexist with this other interpretation in the
	 * same spine without introducing inconsistancies
	 * 
	 * @param k
	 *            interpretation to be compared against
	 * @return whether or not the two interpretations can coexist in the same
	 *         spine
	 */
	public abstract boolean isCompatible(Interpretation k);
}
