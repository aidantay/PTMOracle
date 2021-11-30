package org.cytoscape.PTMOracle.internal.painter.swing;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.cytoscape.PTMOracle.internal.model.ColorScheme;
import org.cytoscape.PTMOracle.internal.model.painter.ColorSchemeImpl;
import org.cytoscape.PTMOracle.internal.util.swing.ComboBoxDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.impl.AttributeComboBoxDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.impl.ColorButton;
import org.cytoscape.PTMOracle.internal.util.swing.impl.ValueComboBoxDisplay;

import static org.cytoscape.PTMOracle.internal.util.CytoscapeServices.getCurrentNetwork;

/**
 * Panel for assigning colours to ANY attribute for a pie-chart representation  
 * @author aidan
 */
public class MultiPainterPanel extends JPanel {

	private static final long serialVersionUID = -8478269412554278787L;	

	private JPanel emptyPanel;

	private JSplitPane splitPane;
	private JPanel selectionPanel;
	private JPanel addPanel;
	private JButton addButton;

	private Map<JButton, MultiPainterUnit> colorPaletteMap;

	public MultiPainterPanel() {
		super(new CardLayout());

		createPanels();
		
		colorPaletteMap = new HashMap<JButton, MultiPainterUnit>();
		paint();
	}
	
	public void paint() {
		add(emptyPanel);
		add(splitPane);
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
		addButton = new JButton("+");
		addButton.addActionListener(new AddListener());
		
	    GridBagConstraints c = new GridBagConstraints();
	    
	    c.anchor = GridBagConstraints.NORTHWEST;
	    c.gridx = 0;
	    c.gridy = 0;
	    c.weightx = 1;
	    c.weighty = 1;
	    c.insets = new Insets(5, 5, 5, 5);
	    addPanel.add(addButton, c);
		
		splitPane.setTopComponent(selectionPanel);
		splitPane.setBottomComponent(addPanel);
	}
	
	public void clearColorPalette() {
		selectionPanel.removeAll();
		selectionPanel.revalidate();
		selectionPanel.repaint();		
		addPanel.removeAll();
		addPanel.revalidate();
		addPanel.repaint();
		colorPaletteMap.clear();
	}
	
	private void createPanels() {
		emptyPanel = new JPanel();
		emptyPanel.setBackground(Color.WHITE);
		
		selectionPanel = new JPanel(new GridBagLayout());
		selectionPanel.setBackground(Color.WHITE);
		
		addPanel = new JPanel(new GridBagLayout());
		addPanel.setBackground(Color.WHITE);
				
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, selectionPanel, addPanel);
		splitPane.setEnabled(false);
		splitPane.setDividerSize(0);		
	}
			
	public ColorScheme getColorScheme() {
		ColorScheme colorScheme = new ColorSchemeImpl();
		
		for (MultiPainterUnit c : colorPaletteMap.values()) {
			String attribute = c.getAttribute();
			String value     = c.getValue();
			Color color      = c.getColor();
			Pair<String, String> values = new ImmutablePair<String, String>(attribute, value);
			colorScheme.addColor(values, color);
		}
		
		return colorScheme;
	}
	
	public void importColor(String attribute, String value, Color color) {
		MultiPainterUnit newUnit = new MultiPainterUnit(attribute, value, color);
		addComponent(newUnit);		
	}

	public MultiPainterUnit createComponent() {
		return new MultiPainterUnit();
	}
	
	public void addComponent(MultiPainterUnit component) {
	    GridBagConstraints c = new GridBagConstraints();

	    c.fill = GridBagConstraints.BOTH;
        c.gridy = colorPaletteMap.size();
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(5, 5, 5, 5);
		selectionPanel.add(component, c);
		selectionPanel.revalidate();
		selectionPanel.repaint();
		colorPaletteMap.put(component.getRemoveButton(), component);
		
		int location = splitPane.getDividerLocation() + ((Double) component.getPreferredSize().getHeight()).intValue();
		splitPane.setDividerLocation(location);
		revalidate();
		repaint();
	}
	
	public void removeComponent(JButton button) {
		MultiPainterUnit c = colorPaletteMap.get(button);
		colorPaletteMap.remove(button);
		selectionPanel.remove(c);
		selectionPanel.repaint();
		selectionPanel.revalidate();

		int location = splitPane.getDividerLocation() - ((Double) c.getPreferredSize().getHeight()).intValue();
		splitPane.setDividerLocation(location);
		revalidate();
		repaint();
	}
	
	public void removeAllComponents() {
		Set<JButton> buttonSet = new HashSet<JButton>(colorPaletteMap.keySet());
		for (JButton b : buttonSet) {
			removeComponent(b);
		}
	}
			
	private class AddListener implements ActionListener {
	
		@Override
		public void actionPerformed(ActionEvent e) {
			if (getCurrentNetwork() != null) {
				MultiPainterUnit component = createComponent();
				addComponent(component);
			}
		}
	
	}

	private class RemoveComponentListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			JButton removeButton = (JButton) e.getSource();
			removeComponent(removeButton);
		}
	}
	
	/**
	 * MultiPainterUnit is an individual attribute-colour mapping for MultiPainter
	 */
	private class MultiPainterUnit extends JPanel {
				
		private static final long serialVersionUID = -5680619653512395291L;

		private ComboBoxDisplay attributeComboBoxDisplay;
		private ComboBoxDisplay valueComboBoxDisplay;
		private ColorButton colorButton;

		private JButton removeButton;
				
		public MultiPainterUnit() {
			super(new GridBagLayout());
			setBackground(Color.WHITE);
				
			attributeComboBoxDisplay = new AttributeComboBoxDisplay();
			attributeComboBoxDisplay.addItemListener(new AttributeListener());

			valueComboBoxDisplay = new ValueComboBoxDisplay(Arrays.asList(attributeComboBoxDisplay.getSelectedItem()));
				
			colorButton = new ColorButton(Color.WHITE);
			
			removeButton = new JButton("X");
			removeButton.addActionListener(new RemoveComponentListener());
				
			paint();
		}
				
		public MultiPainterUnit(String attribute, Object value, Color color) {
			this();
			attributeComboBoxDisplay.setSelectedItem(attribute);

			valueComboBoxDisplay.setSelectedItem(value);
					
			colorButton.setBackground(color);
			colorButton.setOpaque(true);
			colorButton.setBorderPainted(false);
		}
				
		public void paint() {
	        GridBagConstraints c = new GridBagConstraints();

	        c.fill = GridBagConstraints.BOTH;
	        c.gridx = 0;
	        c.gridy = 1;
	        c.ipadx = 10;
	        c.ipady = 10;
	        c.insets = new Insets(5, 5, 5, 5);
	        add(removeButton, c);
			        
	        c.fill = GridBagConstraints.BOTH;
	        c.gridx = 1;
	        c.gridy = 0;
	        c.gridwidth = 2;
	        c.weightx = 1;
	        c.ipadx = 10;
	        c.ipady = 10;
	        add(attributeComboBoxDisplay, c);

	        c.gridx = 1;
	        c.gridy = 1;
	        c.gridwidth = 1;
	        c.ipadx = 0;
	        c.ipady = 0;
	        c.weightx = 0;
	        add(new JLabel("is "), c);
		        
	        c.gridx = 2;
	        c.gridy = 1;
	        c.ipadx = 10;
	        c.ipady = 10;
	        add(valueComboBoxDisplay, c);
	        
	        c.gridx = 1;
	        c.gridy = 2;
	        add(new JLabel("as "), c);
			        
	        c.gridx = 2;
	        c.gridy = 2;
	        c.ipadx = 10;
	        c.ipady = 10;
	        add(colorButton, c);
	        
	        c.gridx = 0;
	        c.gridy = 3;
	        c.ipadx = 0;
	        c.ipady = 0;
	        c.gridwidth = 3;
	        add(new JSeparator(), c);
		}
		
		public Color getColor() {
			return colorButton.getColor();
		}
			
		public JButton getRemoveButton() {
			return removeButton;
		}
				
		public String getAttribute() {
			return attributeComboBoxDisplay.getSelectedItem();
		}
				
		public String getValue() {
			return valueComboBoxDisplay.getSelectedItem();
		}
				
		private class AttributeListener implements ItemListener {
				
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					valueComboBoxDisplay.setConditions(Arrays.asList(attributeComboBoxDisplay.getSelectedItem()));
				}
			}
		}
		
	}
	
}