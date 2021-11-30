package org.cytoscape.PTMOracle.internal.util.swing;

import javax.swing.JTextPane;

import org.cytoscape.PTMOracle.internal.util.swing.impl.WrapEditorKit;

/**
 * TextDisplays are JTextPanes used for displaying text. 
 * @author aidan
 */
public class TextDisplay extends JTextPane {

	private static final long serialVersionUID = -4706763671939937001L;

	public TextDisplay() {
		super();
		
		setEditable(false);
		setEditorKit(new WrapEditorKit());
	}
	
	public void updateTextDisplay() {
		
	}
}