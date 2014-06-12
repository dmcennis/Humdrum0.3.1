/*
 * Created on Aug 23, 2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.osu.csml.humdrum.kern.interpretation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.osu.csml.humdrum.*;
import edu.osu.csml.humdrum.kern.*;
import edu.osu.csml.humdrum.kern.note.*;

/**
 * Ensure keysignatures conform to syntax and that likely key signature errors
 * are detected.
 * @author Daniel McEnnis
 * 
 */
public class KeySignature implements Interpretation {

	static boolean started = false;
	
	static synchronized public void init(){
		if(!started){
			KernInterpretationFactory.registerSpecific("\\*k\\[.*\\]",new KeySignature(new EventController()));
			started=true;
		}
	}
	/**
	 * relates letter classes with their modifications. Assume a default of
	 * zero
	 */
	private HashMap keys = new HashMap();
	private Controller master;

	/*
	 * @see java.lang.Object#clone()
	 */
	public Interpretation clone(Controller master) {
		KeySignature tmp = new KeySignature(master);
		tmp.keys = (HashMap) keys.clone();
		for (Iterator i = tmp.keys.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			entry.setValue(((Pitch) entry.getValue()).clone(master));
		}
		return tmp;

	}

	/**
	 * For subclasses only. Not meant for general consumption
	 * 
	 * @param master
	 */
	protected KeySignature(Controller master) {
		this.master = master;
	}

	/**
	 * parse a key signature from its interpretation and register for pitch
	 * events
	 * 
	 * @param token
	 * @param master
	 * @throws KernException
	 * @throws KernWarning
	 */
	public KeySignature(String token, Controller master)
		throws KernException, KernWarning {
		Pattern keySig = Pattern.compile("\\*k\\[([-#n[a-g][A-G]]*)\\]");
		Matcher k = keySig.matcher(token);
		if (!k.matches()) {
			throw new KernException(
				"BUG: improperly labeled as key signature",
				master);
		}
		String elem = k.group(1);
		Pattern notes = Pattern.compile("([a-gA-G][-#n]+)(.*)");
		Matcher n = notes.matcher(elem);
		Pitch p;

		while (n.matches()) {
			p = new Pitch(master);
			p.init(n.group(1));
			keys.put(new Integer(p.getPitch() - p.getAccidental()), p);
			elem = n.group(2);
			n = notes.matcher(elem);
		}
		if (elem.length() > 0) {
			throw new KernException("Malformed key signature entry", master);
		}
		master.register(Pitch.class, this);
		this.master = master;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {

		if (obj instanceof KeySignature) {
			KeySignature right = (KeySignature)obj;
			for(Iterator it = this.keys.keySet().iterator();it.hasNext();){
				if(!right.keys.containsKey(it.next())){
					return false;
				}
			}
			for(Iterator it = right.keys.keySet().iterator();it.hasNext();){
				if(!this.keys.containsKey(it.next())){
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.osu.csml.humdrum.Interpretation#isCompatible(edu.osu.csml.humdrum.Interpretation)
	 */
	public boolean isCompatible(Interpretation k) {
		if (k.getClass().isInstance(this)) {
			return k.equals(this);
		}
		return true;
	}

	/**
	 * check if pitch explicitly declares an altered pitch or might be a
	 * 'forgotten accidental'
	 * 
	 * @see edu.osu.csml.humdrum.Consumer#fire(edu.osu.csml.humdrum.Producer)
	 */
	public void fire(Class type, Object trigger)
		throws KernException, KernWarning {
		if (type != Pitch.class) {
			throw new KernException(
				"BUG: only pitches used by KeySignature",
				master);
		}
		Pitch tmp = (Pitch) trigger;
		Integer note = new Integer(tmp.getPitch() - tmp.getAccidental());
		if ((keys.containsKey(note))
			&& (tmp.getAccidental() == 0)
			&& (tmp != ((Pitch) keys.get(note)))) {
			throw new KernWarning(
				"The key signature implies a different accidental than is present",
				master);
		}
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(Controller master) {
		return new KeySignature(master);
	}

	/* (non-Javadoc)
	 * @see edu.osu.csml.humdrum.View#prototype(java.lang.String, edu.osu.csml.humdrum.Controller)
	 */
	@Override
	public View prototype(String token, Controller master) throws HumdrumException, HumdrumWarning{
		return new KeySignature(token,master);
	}
}
