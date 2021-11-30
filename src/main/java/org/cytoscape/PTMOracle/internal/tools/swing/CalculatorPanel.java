package org.cytoscape.PTMOracle.internal.tools.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.cytoscape.PTMOracle.internal.util.swing.ComboBoxDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.ListDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.ScrollDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.TextDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.impl.DescriptionTextDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.impl.KeywordListDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.impl.PropertyComboBoxDisplay;

/**
 * Primary panel for RegionFinder OracleTool. 
 * @author aidan
 */
public class CalculatorPanel extends JPanel {

	private static final long serialVersionUID = -8454610429405691132L;
	
	private static final String DESCRIPTION = "Counts the number of PTM sites or\n"
											  + "sequence annotations on protein nodes";
	
	private JPanel descriptionPanel;
	private TextDisplay descriptionPane;

	private JPanel parameterPanel;
	private JLabel propertyTypeInstructions;
	private ComboBoxDisplay propertyTypeComboBoxDisplay;
	private JLabel keywordInstructions;
	private ListDisplay keywordListDisplay;
	
	public CalculatorPanel() {
		super(new GridBagLayout());
		
		createDescriptionPanel();
		createParameterPanel();
		
		paint();
	}
	
	public void paint() {
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 1;
		c.weighty = 1;		
		add(descriptionPanel, c);

		c.gridx = 0;
		c.gridy = 1;
		add(parameterPanel, c);
	}
	
	public String getPropertyTypeField() {
		return propertyTypeComboBoxDisplay.getSelectedItem();
	}

	public List<String> getKeywordListField() {
		return keywordListDisplay.getSelectedValuesList();
	}
	
	private void createDescriptionPanel() {
		descriptionPanel = new JPanel(new GridBagLayout());
		descriptionPanel.setBorder(BorderFactory.createTitledBorder("Description"));

		descriptionPane = new DescriptionTextDisplay(DESCRIPTION);

		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1;
        descriptionPanel.add(descriptionPane, c);
	}

	private void createParameterPanel() {
		parameterPanel = new JPanel(new GridBagLayout());
		parameterPanel.setBorder(BorderFactory.createTitledBorder("Parameters"));

		keywordInstructions = new JLabel("Description");
		keywordListDisplay  = new KeywordListDisplay();
		keywordListDisplay.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		propertyTypeInstructions    = new JLabel("Property");
		propertyTypeComboBoxDisplay = new PropertyComboBoxDisplay();
		propertyTypeComboBoxDisplay.addActionListener(new PropertyListListener());
		propertyTypeComboBoxDisplay.setSelectedIndex(0);		

		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1;
        parameterPanel.add(propertyTypeInstructions, c);
        
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 1;
        parameterPanel.add(propertyTypeComboBoxDisplay, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0;
        parameterPanel.add(keywordInstructions, c);

        c.gridx = 1;
        c.gridy = 1;
        c.weighty = 1;
        parameterPanel.add(new ScrollDisplay(keywordListDisplay), c);
	}
	
	private class PropertyListListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			ComboBoxDisplay comboBoxDisplay = (ComboBoxDisplay) e.getSource();
			String value = (String) comboBoxDisplay.getSelectedItem();
			keywordListDisplay.setConditions(Arrays.asList(value));
			
		}
	}

}
