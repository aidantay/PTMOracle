package org.cytoscape.PTMOracle.internal.util.swing.impl;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
/**
 * Button for displaying and choosing colours
 * @author aidan
 */
public class ColorButton extends JButton implements ActionListener {

	private static final long serialVersionUID = 690670297546747482L;
	private Color color;
	
	public ColorButton(Color color) {
		super();

		setColor(color);
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));	// Improvised for 'no show' issue on Windows
		addActionListener(this);
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
		setBackground(color);
		setContentAreaFilled(false);	// Fixes 'no show' issue on Windows
		setOpaque(true);				// Fixes 'no show' issue on Windows
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Color newColor = JColorChooser.showDialog(this, "Color Chooser", getColor());
		if (newColor != null) {
			setColor(newColor);
		}
	}
	
}