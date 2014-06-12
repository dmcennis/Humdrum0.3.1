/*
 * Created on Aug 23, 2003
 * 
 * Distributed under the GPL license
 */
package edu.osu.csml.humdrum;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.CommentLine;

/**
 * Master class for parsing a humdrum document. Responsible for accepting
 * character input, creating the appropriate interpretations, and passing data
 * to these interpretation objects as it is encountered. This class is also
 * capable of diagnosing some humdrum errors (those that were trivial during
 * parsing of humdrum files). This error checking feature should have a flag to
 * disable it, but currently doesn't.
 * 
 * @author Daniel McEnnis
 *  
 */
public class Document {
	/**
	 * Recieves all events fired during the procesing of the document.
	 */
	MasterController master = new MasterController();
	/**
	 * Holds refrences to all spines at the current line
	 */
	private Vector spines = new Vector();
	/**
	 * current line in document
	 */
	private int lineNum = 0;
	/**
	 * current spine in document
	 */
	private int spineNum = 0;
	/**
	 * source of the humdrum data -read exclsuively by line.
	 */
	private LineNumberReader source;
	/**
	 * defines the spine deliminator
	 */
	private Pattern split = Pattern.compile("\\t");
	/**
	 * defines test for existance of end-of-spine interpretation
	 */
	private Pattern end = Pattern.compile("(.*\\t)*\\*-(\\t.*)*");
	/**
	 * defines test for existance of a merging interpretation
	 */
	private Pattern merge = Pattern.compile("(.*\\t)*\\*v(\\t.*)*");
	/**
	 * defines test for existance of an exchange interpretation
	 */
	private Pattern exch = Pattern.compile("(.*\\t)*\\*x(\\t.*)*");
	/**
	 * defines test for existance of interpretation requesting the insert of a
	 * new, uncloned spine
	 */
	private Pattern add = Pattern.compile("(.*\\t)*\\*\\+(\\t.*)*");
	/**
	 * defines test of existance of interpretation requesting a cloned spine
	 */
	private Pattern separate = Pattern.compile("(.*\\t)*\\*^(\\t.*)*");
	/**
	 * reference for where the error data should be sent
	 */
	private Writer err;
	/**
	 * reference to the most recently read line
	 */
	private String line;

	/**
	 * Any global checks that require inter-spine contexts should be inserted
	 * here. They are loaded by initGlobalChecks called in the primary loop of
	 * parseDocument
	 */
	private View[] globalChecks;

	/**
	 * Create a document at the default warning level
	 */
	public Document() {
		DupLineCheck.init();
		HumdrumWarning.setWarningLevel(0);
	}

	/**
	 * Create a document at the given warning level
	 * 
	 * @param val
	 *            warning level - 0 is base, bigger than 0 implies ignore
	 *            warnings
	 */
	public Document(int val) {
		DupLineCheck.init();
		HumdrumWarning.setWarningLevel(val);
	}

	/**
	 * Parses a humdrum document. There may be more than one document in this
	 * file, but only parses through the final spine termination of the first
	 * set of exclusive interpretations.
	 * 
	 * @param source
	 * @param err
	 */
	public void parseDocument(Reader source, Writer err) {
		this.source = new LineNumberReader(source);
		this.err = err;
		initGlobalChecks();
		try {
			line = this.source.readLine();
			while (line != null) {
				parseHeader();
				parseBody();
			}
		} catch (HumdrumException e) {
			try {
				err.write(e.toString());
			} catch (IOException e1) {
			}

		} catch (HumdrumWarning e) {
			try {
				err.write(e.toString());
			} catch (IOException e1) {
			}
		} catch (IOException e) {
			try {
				err.write("Input Failure: " + e.toString());
			} catch (IOException e1) {
			}

		}
		try {
			err.flush();
		} catch (IOException e1) {
		}
	}

	/**
	 * This must be edited to add or remove interspine interpretations/check
	 */
	private void initGlobalChecks() {
		globalChecks = GlobalCheckFactory.getChecks(master);
//		globalChecks = new View[4];
//		globalChecks[0] = new DupLineCheck(master);
//		globalChecks[1] = new DupMeasureCheck(master);
//		globalChecks[2] = new GlobalDurationCheck(master);
//		globalChecks[3] = new MixedBarlineNoteCheck(master);
	}

	/**
	 * responsible for parsing a Humdrum document up to and including the
	 * exclusive interpretations
	 * 
	 * @throws IOException
	 * @throws HumdrumWarning
	 * @throws HumdrumException
	 */
	public void parseHeader()
		throws IOException, HumdrumWarning, HumdrumException {
		while ((spines.size() <= 0) && (line != null)) {
			if (line.matches("!.*")) {
				;
				// intentioanlly do nothing - not connected to any spine
			} else if (line.matches("\\*[^*].*")) {
				;
				// intentionally do nothing - not connected to any spine
			} else if (line.matches("\\*\\*.*")) {

				String[] tokens = split.split(line);
				for (spineNum = 0; spineNum < tokens.length; ++spineNum) {
					spines.add(
						spineNum,
						SpineFactory.parseSpine(tokens[spineNum], master));

				}
			} else if (line.matches("")) {
				throw new HumdrumException(
					"Blank lines are not permitted in a humdrum file",
					master);
			} else {
				throw new HumdrumException(
					"Must declare a spine (ie. **kern) before entering data",
					master);
			}
			master.fire(Line.class, new Line(line, master));
			++lineNum;
			line = source.readLine();
		}
	}
	/**
	 * Responsible for the document after exclusive interpretations have been
	 * declared through the termination of the last spine
	 * 
	 * @throws IOException
	 * @throws HumdrumException
	 * @throws HumdrumWarning
	 */
	public void parseBody()
		throws IOException, HumdrumException, HumdrumWarning {

		while (spines.size() > 0) {
			if (line.matches("!.*")) {
				parseComment(line);

			} else if (line.matches("\\*.*")) {
				if (!parseMeta(line)) {
					parseInterpretation(line);
				}
			} else {
				parseData(line);
			}
			master.fire(Line.class, new Line(line, master));
			++lineNum;
			line = source.readLine();
		}
	}

	/**
	 * Processes all comments - Any 'special' comments that need to be checked
	 * should be fired from here.
	 */
	protected void parseComment(String line)
		throws HumdrumException, HumdrumWarning {
		if(line.matches("!!.*")){
			CommentLine comment= new CommentLine(line,master);
			master.fire(CommentLine.class, comment);
		}else{
			
		}
	}
	/**
	 * Processes all humdrum data tokens. Code that reference specific types of
	 * spines should *not* be here.
	 * 
	 * @param line
	 * @throws HumdrumException
	 * @throws HumdrumWarning
	 */
	protected void parseData(String line)
		throws HumdrumException, HumdrumWarning {
		String[] tokenStream = split.split(line);
		if (tokenStream.length != spines.size()) {
			throw new HumdrumException(
				"Number of entries ("
					+ tokenStream.length
					+ " ) does not match the number of spines ("
					+ spines.size()
					+ ")",
				master);
		}
		for (spineNum = 0; spineNum < spines.size(); ++spineNum) {
			((Spine) spines.get(spineNum)).addData(tokenStream[spineNum]);
		}
		spineNum = 0;
	}

	// Processes all non-humdrum interpretations (not related to changes in
	// spine structure) Code that references specific types of spines should
	// *not* be here.
	protected void parseInterpretation(String line)
		throws HumdrumException, HumdrumWarning {
		String[] tokens;
		tokens = split.split(line);
		if (spines.size() != tokens.length) {
			throw new HumdrumException(
				"Number of entries ("
					+ tokens.length
					+ " ) does not match the number of spines ("
					+ spines.size()
					+ ")",
				master);

		}
		for (spineNum = 0; spineNum < spines.size(); ++spineNum) {
			((Spine) spines.get(spineNum)).addInterpretation(tokens[spineNum]);
		}
		spineNum = 0;
	}

	//Processes all humdrum interpretations - references specific spine
	// subclasses only to create new spines.
	protected boolean parseMeta(String line)
		throws HumdrumException, HumdrumWarning, IOException {
		if (end.matcher(line).matches()) {
			endSpine(line);
			return true;
		} else if (merge.matcher(line).matches()) {
			mergeSpine(line);
			return true;
		} else if (separate.matcher(line).matches()) {
			splitSpine(line);
			return true;
		} else if (add.matcher(line).matches()) {
			newSpine(line);
			return true;
		} else if (exch.matcher(line).matches()) {
			exchangeSpine(line);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * terminates one or more spines spine
	 * 
	 * @param line
	 *            current line of document
	 * @throws HumdrumException
	 * @throws HumdrumWarning
	 */
	protected void endSpine(String line)
		throws HumdrumException, HumdrumWarning {
		Pattern end = Pattern.compile("\\*-");
		Matcher e;
		String[] tokens = split.split(line);
		if (spines.size() != tokens.length) {
			throw new HumdrumException(
				"Number of entries ("
					+ tokens.length
					+ " ) does not match the number of spines ("
					+ spines.size()
					+ ")",
				master);

		}
		for (int spineNum = (tokens.length - 1); spineNum >= 0; --spineNum) {
			e = end.matcher(tokens[spineNum]);
			if (e.matches()) {
				((Spine) spines.get(spineNum)).terminate();
				spines.remove(spineNum);
			}
		}
	}

	/**
	 * Merge two spines together. Merging spines must be compatible and
	 * adjacent
	 * 
	 * @param line
	 *            current line of document
	 * @throws HumdrumException
	 * @throws HumdrumWarning
	 */
	protected void mergeSpine(String line)
		throws HumdrumException, HumdrumWarning {
		String[] tokens = split.split(line);
		Pattern merge = Pattern.compile("\\*v");
		if (spines.size() != tokens.length) {
			throw new HumdrumException(
				"Number of entries ("
					+ tokens.length
					+ " ) does not match the number of spines ("
					+ spines.size()
					+ ")",
				master);
		}
		int i = 0;
		while ((i < tokens.length) && (!merge.matcher(tokens[i]).matches())) {
			++i;
		}
		if ((i + 1 >= spines.size())
			|| (!merge.matcher(tokens[i + 1]).matches())) {
			throw new HumdrumException(
				"Only adjacent spines may be joined",
				master);
		}
		while ((i + 1 < spines.size())
			&& (merge.matcher(tokens[i + 1]).matches())) {
			if (!((Spine) spines.get(i))
				.isCompatible((Spine) spines.get(i + 1))) {
				throw new HumdrumWarning(
					"Joining spines with incompatible interpretations",
					master);
			} else {
				((Spine) spines.get(i)).merge((Spine) spines.get(i + 1));
				spines.remove(i + 1);
			}
		}
	}

	/**
	 * Creates a new spine with identical state to the original
	 * 
	 * @param line
	 *            current line in document as a string
	 * @throws HumdrumException
	 * @throws HumdrumWarning
	 */
	protected void splitSpine(String line)
		throws HumdrumException, HumdrumWarning {
		Pattern sp = Pattern.compile("*^");
		String[] tokens = split.split(line);
		if (spines.size() != tokens.length) {
			throw new HumdrumException(
				"Number of entries ("
					+ tokens.length
					+ " ) does not match the number of spines ("
					+ spines.size()
					+ ")",
				master);
		}
		int i = 0;
		while ((i < tokens.length) && (sp.matcher(tokens[i]).matches())) {
			++i;
		}
		if (!((Spine) spines.get(i)).isCompatible((Spine) spines.get(i + 1))) {
			throw new HumdrumWarning(
				"Spines " + i + " and " + (i + 1) + " are incompatible",
				master);
		}
		((Spine) spines.get(i)).merge((Spine) spines.get(i + 1));
	}
	/**
	 * Create a new spine , then pulls the next line and processes the
	 * exclusive interpretation that belongs with the line. Only time two lines
	 * are processed at once.
	 * 
	 * @param line
	 *            string of current line in document
	 * @throws HumdrumException
	 * @throws HumdrumWarning
	 * @throws IOException
	 */
	protected void newSpine(String line)
		throws HumdrumException, HumdrumWarning, IOException {
		String[] tokens = split.split(line);
		if (spines.size() != tokens.length) {
			throw new HumdrumException(
				"Number of entries ("
					+ tokens.length
					+ " ) does not match the number of spines ("
					+ spines.size()
					+ ")",
				master);
		}
		int i = 0;
		Pattern nw = Pattern.compile("\\*+");
		while ((i < tokens.length) && (nw.matcher(tokens[i]).matches())) {
			++i;
		}

		line = source.readLine();
		tokens = split.split(line);
		spines.add(i + 1, SpineFactory.parseSpine(tokens[i], master));
	}

	/**
	 * Switches the position of two spines
	 * 
	 * @param line
	 *            string of current line in document
	 * @throws HumdrumException
	 *             too many or too few spines in line
	 * @throws HumdrumWarning
	 */
	protected void exchangeSpine(String line)
		throws HumdrumException, HumdrumWarning {
		String[] tokens = split.split(line);
		Pattern exchange = Pattern.compile("\\*x");
		if (spines.size() != tokens.length) {
			throw new HumdrumException(
				"Number of entries ("
					+ tokens.length
					+ " ) does not match the number of spines ("
					+ spines.size()
					+ ")",
				master);
		}
		int i = 0;
		while ((i < tokens.length)
			&& (!exchange.matcher(tokens[i]).matches())) {
			++i;
		}
		Object tmp = spines.get(i);
		spines.set(i, spines.get(i + 1));
		spines.set(i + 1, tmp);
	}

	/**
	 * Gives global interpretations a way to see not only which spine they are
	 * currently looking at, but how many spines are expected in this line.
	 * 
	 * @return total number of spines expected in this line
	 */
	public int getNumberOfSpines() {
		return spines.size();
	}

	/**
	 * @author Daniel McEnnis
	 * 
	 * Extends EventController to provide support for root node operations
	 */
	class MasterController extends EventController {
		public MasterController() {
			super();
		}
		/**
		 * Sets the StdErr as primary error stream
		 * 
		 * @see edu.osu.csml.humdrum.Controller#getErrStream()
		 */
		public Writer getErrStream() {
			return err;
		}

		/**
		 * utilizes line numbers in Document to identify location
		 */
		public int getLineNumber() {
			return source.getLineNumber();
		}

		/**
		 * utilizes spine numbers in Document to identify the correct spine
		 */
		public int getSpineNumber() {
			return spineNum + 1;
		}

		/**
		 * Can't make the parent variable self referential in the constructor,
		 * so call it here. parent is only accessed via a View being fired.
		 * SInce Views must register first and generally don't hapen very
		 * frequently, it should be safe to set the value here.
		 *  
		 */
		public void register(Class trigger, View rec) {
			super.register(trigger, rec);
		}

		/**
		 * Intentionally do nothing. There is no parent to call.
		 * 
		 * @param trigger
		 *            model that started this cascade of events
		 * @param name
		 *            class type of the trigger
		 */
		protected void FireParent(Class name, Object trigger) {
			//Deliberatlly no op
			;
		}

		/* (non-Javadoc)
		 * @see edu.osu.csml.humdrum.Controller#getParent()
		 */
		public Controller getParent() {
			return this;
		}

	}

	public int getLineNum() {
		return lineNum;
	}

	public View[] getGlobalChecks() {
		return globalChecks;
	}

}
