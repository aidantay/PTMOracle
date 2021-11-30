package org.cytoscape.PTMOracle.internal.schema.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.apache.commons.lang3.tuple.Pair;
import org.cytoscape.PTMOracle.internal.util.CytoscapeServices;
import org.cytoscape.PTMOracle.internal.util.swing.ComboBoxDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.ScrollDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.impl.AttributeComboBoxDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.impl.WrapEditorKit;
import org.cytoscape.model.CyNetwork;

/**
 * Primary panel for importing properties from an oracle file.
 * @author aidan
 */
public class ImportPropertiesPanel extends JPanel {

	private static final long serialVersionUID = -7710622807529835242L;

	private JPanel networkPanel;
	private JLabel currentNetworkInstructions;
	private JLabel currentNetwork;
	private JLabel attributeInstructions;	
	private ComboBoxDisplay attributeComboBoxDisplay;

	private JPanel fileInfoPanel;
	private JLabel fileInstructions;
	private JTextPane filePath;
	private JLabel sourceTypeInstructions;
	private JTextField sourceType;
		
	private JPanel unresolvedPropertiesPanel;
	private TableDisplay unresolvedPropertiesTableDisplay;
	
	public ImportPropertiesPanel(CyNetwork network, File file, Map<Pair<String, String>, List<String>> possibleKeywordMap) {
		super(new GridBagLayout());
		
		createCurrentNetworkPanel(network);
		createFileInfoPanel(file);
		createFileStatusPanel(possibleKeywordMap);

		paint();
	}
		
	public void paint() {
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.insets = new Insets(5, 5, 5, 5);
		add(networkPanel, c);

		c.gridx = 0;
		c.gridy = 1;
		add(fileInfoPanel, c);

		if (unresolvedPropertiesPanel != null) {
			c.gridx = 0;
			c.gridy = 2;
			c.weighty = 1;
			add(unresolvedPropertiesPanel, c);
		}
	}
	
	public String getSourceString() {
		return sourceType.getText();
	}
	
	public String getAttribute() {
		return attributeComboBoxDisplay.getSelectedItem();
	}
	
	public TableDisplay getTableDisplay() {
		return unresolvedPropertiesTableDisplay;
	}
	
	private void createCurrentNetworkPanel(CyNetwork network) {
		networkPanel = new JPanel(new GridBagLayout());
		networkPanel.setBorder(BorderFactory.createTitledBorder("Target network data"));
		
		currentNetworkInstructions = new JLabel("Current Network: ");
		currentNetwork = new JLabel(CytoscapeServices.getRootNetwork(network).toString());
		
		attributeInstructions = new JLabel("Mapping column:");
		attributeComboBoxDisplay = new AttributeComboBoxDisplay();

		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0.25;
        c.insets = new Insets(5, 5, 5, 5);
        networkPanel.add(currentNetworkInstructions, c);
        
        c.gridx = 1;
        c.gridy = 0;
        networkPanel.add(currentNetwork, c);

        c.gridx = 0;
        c.gridy = 1;
        networkPanel.add(attributeInstructions, c);
        
        c.gridx = 1;
        c.gridy = 1;
        networkPanel.add(attributeComboBoxDisplay, c);
	}

	private void createFileInfoPanel(File file) {
		fileInfoPanel = new JPanel(new GridBagLayout());
		fileInfoPanel.setBorder(BorderFactory.createTitledBorder("Input file data"));
		
		fileInstructions = new JLabel("Selected file:");
		
		filePath = new JTextPane();
		filePath.setEditable(false);
		filePath.setEditorKit(new WrapEditorKit());
		filePath.setText(file.getAbsolutePath());

		sourceTypeInstructions = new JLabel("Source:");
		sourceType             = new JTextField("");
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        fileInfoPanel.add(fileInstructions, c);
        
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        fileInfoPanel.add(filePath, c);
        
        c.gridx = 0;
        c.gridy = 1;
        fileInfoPanel.add(sourceTypeInstructions, c);
        
        c.gridx = 1;
        c.gridy = 1;
        fileInfoPanel.add(sourceType, c);	
	}

	private void createFileStatusPanel(Map<Pair<String, String>, List<String>> possibleKeywordMap) {
		if (possibleKeywordMap.size() != 0) {
			unresolvedPropertiesPanel = new JPanel(new GridBagLayout());
			unresolvedPropertiesPanel.setBorder(BorderFactory.createTitledBorder("Unresolved Properties"));
	
			unresolvedPropertiesTableDisplay = new PropertyCheckTableDisplay(possibleKeywordMap);
			ScrollDisplay scrollDisplay = new ScrollDisplay(unresolvedPropertiesTableDisplay);
			
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			unresolvedPropertiesPanel.add(scrollDisplay, c);
		}
	}
	
}

