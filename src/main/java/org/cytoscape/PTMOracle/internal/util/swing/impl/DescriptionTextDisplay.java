package org.cytoscape.PTMOracle.internal.util.swing.impl;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.cytoscape.PTMOracle.internal.util.swing.TextDisplay;

/**
 * TextDisplay for displaying descriptions for different GUIs 
 * @author aidan
 */
public class DescriptionTextDisplay extends TextDisplay {

	private static final long serialVersionUID = 4681680195787628795L;

	private String description;
	
	public DescriptionTextDisplay(String description) {
		super();
		
		this.description = description;
		updateTextDisplay();
	}
	
	public void updateTextDisplay() {
		DefaultStyledDocument doc = new DefaultStyledDocument();
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attrs, "SanSerif");
		StyleConstants.setFontSize(attrs, 12);

		try {
			doc.insertString(doc.getLength(), description, attrs);
			setDocument(doc);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

}