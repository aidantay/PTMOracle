package org.cytoscape.PTMOracle.internal.util.swing.impl;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Class for limiting text field input 
 * Code taken from here: https://stackoverflow.com/questions/3519151/hot-to-limit-the-number-of-characters-in-jtextfield
 * @author aidan
 */
public class JTextFieldLimit extends PlainDocument {

	private static final long serialVersionUID = -9155791940200954552L;
	private int limit;
	
	public JTextFieldLimit(int limit) {
		super();

		this.limit = limit;
	}
	
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		
		if (str == null) {
			return;
		}
		
		if ((getLength() + str.length()) <= limit) {
			super.insertString(offset, str, attr);
		}
	}
}



