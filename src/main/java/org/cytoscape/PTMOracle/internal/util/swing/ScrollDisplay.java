package org.cytoscape.PTMOracle.internal.util.swing;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * ScrollDisplay are JScrollPanes used for displaying information. 
 * @author aidan
 * 
 */
public class ScrollDisplay extends JScrollPane {

	private static final long serialVersionUID = 5934260151591051503L;

	public ScrollDisplay(Component component) {
		super(component);
		setCorner(JScrollPane.UPPER_RIGHT_CORNER, new JPanel());
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	}

}
