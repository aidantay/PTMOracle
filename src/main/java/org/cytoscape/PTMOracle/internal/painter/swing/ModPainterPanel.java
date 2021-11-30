package org.cytoscape.PTMOracle.internal.painter.swing;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;
import static org.cytoscape.PTMOracle.internal.schema.PropertyTable.PTM; 

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.cytoscape.PTMOracle.internal.model.ColorScheme;
import org.cytoscape.PTMOracle.internal.model.painter.ColorSchemeImpl;
import org.cytoscape.PTMOracle.internal.util.swing.impl.ColorButton;

/**
 * Panel for assigning colours to PTMs for a pie-chart representation.
 * ModPainter is a PTM specific painter.
 * @author aidan
 */
public class ModPainterPanel extends JPanel {
	
	private static final long serialVersionUID = -8478269412554278787L;

	private JPanel emptyPanel;

	private JPanel selectionPanel;
	private Map<JLabel, ColorButton> colorPaletteMap;
	
	public ModPainterPanel() {
		super(new CardLayout());

		createPanels();
		
		colorPaletteMap = new HashMap<JLabel, ColorButton>();

		paint();
	}
	
	public void paint() {
		add(emptyPanel);
		add(selectionPanel);
	}
	
	public void showEmptyPanel() {
		CardLayout layout = (CardLayout) getLayout();
		layout.first(this);
	}
	
	public void showSelectionPanel() {
		CardLayout layout = (CardLayout) getLayout();
		layout.last(this);
	}
	
	public void createColorPalette() {
		for (Object primaryKey : getOracle().getKeywordTable().getPrimaryKeys()) {
			Integer id = (Integer) primaryKey;
			List<?> compositeKey = getOracle().getKeywordTable().getCompositeKey(id);
			String keyword       = (String) compositeKey.get(0);
			String type          = (String) compositeKey.get(1);

			if (type.equals(PTM)) {
				Color color      = (Color) getOracle().getKeywordTable().getColor(compositeKey);
				JLabel label = new JLabel(keyword);
				ColorButton colorButton = new ColorButton(color);
				colorPaletteMap.put(label, colorButton);
			}
		}
		
		GridBagConstraints c = new GridBagConstraints();
	    c.fill = GridBagConstraints.BOTH;
	    c.gridx = 0;
	    c.gridy = 0;
	    c.weightx = 0.5;
	    c.weighty = 0.1;
	    c.insets = new Insets(5, 5, 5, 5);		
		
		List<JLabel> labels = new ArrayList<JLabel>(colorPaletteMap.keySet());
	    Collections.sort(labels, new JLabelTextComparator());
	    for (JLabel label : labels) {
	    	selectionPanel.add(label, c);
		    	
	    	c.gridx++;
	    	selectionPanel.add(colorPaletteMap.get(label), c);
				
			c.gridx = 0;
	    	c.gridy++;
	    }		
	    revalidate();
		repaint();
	}
	
	public void clearColorPalette() {
		selectionPanel.removeAll();
		selectionPanel.revalidate();
		selectionPanel.repaint();
		colorPaletteMap.clear();
	}
	
	private void createPanels() {
		emptyPanel = new JPanel();
		emptyPanel.setBackground(Color.WHITE);

		selectionPanel = new JPanel(new GridBagLayout());
		selectionPanel.setBackground(Color.WHITE);
	}
	
	public ColorScheme getColorScheme() {
		ColorScheme colorScheme = new ColorSchemeImpl();
		for (JLabel label : colorPaletteMap.keySet()) {
			Color color = colorPaletteMap.get(label).getColor();
			Pair<String, String> values = new ImmutablePair<String, String>("", label.getText());
			colorScheme.addColor(values, color);
		}
		return colorScheme;
	}
	
	public void importColor(String value, Color color) {
		for (JLabel label : colorPaletteMap.keySet()) {
			if (label.getText().equals(value)) {
				ColorButton button = colorPaletteMap.get(label);
				button.setColor(color);
			}
		}
	}

	private class JLabelTextComparator implements Comparator<JLabel> {

		@Override
		public int compare(JLabel o1, JLabel o2) {
			return String.CASE_INSENSITIVE_ORDER.compare(o1.getText(), o2.getText());
		}
			
	}
	
}
