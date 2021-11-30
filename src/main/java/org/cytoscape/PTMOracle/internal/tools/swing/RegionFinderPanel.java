package org.cytoscape.PTMOracle.internal.tools.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cytoscape.PTMOracle.internal.schema.PropertyTable;
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
public class RegionFinderPanel extends JPanel {

	private static final long serialVersionUID = -8454610429405691132L;

	private static final String DESCRIPTION = "Identifies nodes with PTM sites that lie\n"
											  + "within a specified sequence annotation.\n"
											  + "Input optional target residues as a regular expression (e.g. [KR])";
	
	private JPanel descriptionPanel;
	private TextDisplay descriptionPane;
	
	private JPanel parameterPanel;
	private JLabel regionTypeInstructions;
	private ComboBoxDisplay regionTypeComboBoxDisplay;
	private JLabel regionInstructions;
	private ListDisplay regionListDisplay;
	private JLabel ptmInstructions;
	private ListDisplay ptmListDisplay;
	
	private JPanel optionsPanel;
	private JLabel targetResiduesInstructions;
	private JTextField targetResiduesField;
	
	public RegionFinderPanel() {
		super(new GridBagLayout());
		
		createDescriptionPanel();
		createParameterPanel();
		createOptionsPanel();
		
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
		
		c.gridx = 0;
		c.gridy = 2;
		add(optionsPanel, c);
	}
	
	public String getRegionTypeField() {
		return regionTypeComboBoxDisplay.getSelectedItem();
	}

	public String getRegionField() {
		return regionListDisplay.getSelectedValue();
	}
	
	public String getPtmField() {
		return ptmListDisplay.getSelectedValue();
	}
	
	public String getTargetResiduesField() {
		return targetResiduesField.getText();
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

		ptmInstructions = new JLabel("PTM");
		ptmListDisplay  = new KeywordListDisplay(Arrays.asList(PropertyTable.PTM));

		regionInstructions = new JLabel("Region");
		regionListDisplay  = new KeywordListDisplay();
		
		regionTypeInstructions    = new JLabel("Property");
		regionTypeComboBoxDisplay = new PropertyComboBoxDisplay(Arrays.asList(true, true));
		regionTypeComboBoxDisplay.addActionListener(new PropertyListListener());
		regionTypeComboBoxDisplay.setSelectedIndex(0);		

		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1;
        parameterPanel.add(regionTypeInstructions, c);
        
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 1;
        parameterPanel.add(regionTypeComboBoxDisplay, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0;
        parameterPanel.add(regionInstructions, c);

        c.gridx = 1;
        c.gridy = 1;
        c.weighty = 1;
        parameterPanel.add(new ScrollDisplay(regionListDisplay), c);

        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0;
        parameterPanel.add(ptmInstructions, c);

        c.gridx = 1;
        c.gridy = 2;
        c.weighty = 1;
        parameterPanel.add(new ScrollDisplay(ptmListDisplay), c);
	}
	
	private void createOptionsPanel() {
		optionsPanel = new JPanel(new GridBagLayout());
		optionsPanel.setBorder(BorderFactory.createTitledBorder("Options"));
		
		targetResiduesInstructions = new JLabel("Target residue/s");
		targetResiduesField        = new JTextField("");
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1;
        optionsPanel.add(targetResiduesInstructions, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 1;
        optionsPanel.add(targetResiduesField, c);
	}
	
	private class PropertyListListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			ComboBoxDisplay comboBoxDisplay = (ComboBoxDisplay) e.getSource();
			String value = (String) comboBoxDisplay.getSelectedItem();
			regionListDisplay.setConditions(Arrays.asList(value));
			
		}
	}

}
