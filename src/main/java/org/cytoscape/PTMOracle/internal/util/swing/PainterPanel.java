package org.cytoscape.PTMOracle.internal.util.swing;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import org.cytoscape.PTMOracle.internal.model.ColorScheme;

/**
 * Panel for assigning colours to PTMs for a pie-chart representation.
 * ModPainter is a PTM specific painter.
 * @author aidan
 */
public abstract class PainterPanel extends JPanel {
	
	private static final long serialVersionUID = -8478269412554278787L;
	private static final String EMPTY     = "Empty";
	private static final String SELECTION = "Selection";

	private JPanel emptyPanel;
	private JPanel selectionPanel;
	
	public PainterPanel() {
		super(new CardLayout());

		emptyPanel = new JPanel(new GridBagLayout());
		emptyPanel.setBackground(Color.WHITE);

		selectionPanel = new JPanel(new GridBagLayout());
		selectionPanel.setBackground(Color.WHITE);

		createSelectionPanel();
		
		paint();
	}
	
	public JPanel getEmptyPanel() {
		return emptyPanel;
	}
	
	public JPanel getSelectionPanel() {
		return selectionPanel;
	}
	
	private void paint() {
		add(emptyPanel, EMPTY);
		add(selectionPanel, SELECTION);
	}
	
	public void showEmptyPanel() {
		CardLayout layout = (CardLayout) getLayout();
		layout.first(this);
	}
	
	public void showSelectionPanel() {
		CardLayout layout = (CardLayout) getLayout();
		layout.last(this);		
	}
	
	public abstract void createSelectionPanel();
	public abstract ColorScheme getColorScheme();
	
}
