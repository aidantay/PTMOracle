package org.cytoscape.PTMOracle.internal.tools.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cytoscape.PTMOracle.internal.schema.PropertyTable;
import org.cytoscape.PTMOracle.internal.util.swing.ListDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.ScrollDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.TextDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.impl.DescriptionTextDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.impl.JTextFieldLimit;
import org.cytoscape.PTMOracle.internal.util.swing.impl.KeywordListDisplay;

/**
 * Primary panel for PairFinder OracleTool. 
 * @author aidan
 */
public class PairFinderPanel extends JPanel {

	private static final long serialVersionUID = -8454610429405691132L;
	
	private static final String DESCRIPTION = "Identifies nodes with pairs of PTMs that are separated\n"
										      + "by a specified number of amino acids";
	
	private JPanel descriptionPanel;
	private TextDisplay descriptionPane;

	private JPanel parameterPanel;
	private JLabel ptm1Instructions;
	private ListDisplay ptm1ListDisplay;
	private JLabel ptm2Instructions;
	private ListDisplay ptm2ListDisplay;
	private JLabel distanceInstructions;
	private JTextField distanceField;
	
	private JPanel optionsPanel;
	private JLabel cumulativeInstructions;
	private JCheckBox cumulativeFlag;
	
	public PairFinderPanel() {
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
	
	public String getPtm1Field() {
		return ptm1ListDisplay.getSelectedValue();
	}

	public String getPtm2Field() {
		return ptm2ListDisplay.getSelectedValue();
	}

	public int getDistanceField() {
		return Integer.valueOf(distanceField.getText());
	}
	
	public boolean getCumulativeField() {
		return cumulativeFlag.isSelected();
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

		ptm1Instructions = new JLabel("PTM 1");
		ptm1ListDisplay  = new KeywordListDisplay(Arrays.asList(PropertyTable.PTM));

		ptm2Instructions = new JLabel("PTM 2");
		ptm2ListDisplay  = new KeywordListDisplay(Arrays.asList(PropertyTable.PTM));

		distanceInstructions = new JLabel("Separation distance");
		distanceField        = new JTextField();
		distanceField.setDocument(new JTextFieldLimit(3));
		distanceField.setText("0");

		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1;
        parameterPanel.add(ptm1Instructions, c);
        
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 1;
        parameterPanel.add(new ScrollDisplay(ptm1ListDisplay), c);

        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0;
        parameterPanel.add(ptm2Instructions, c);

        c.gridx = 1;
        c.gridy = 1;
        c.weighty = 1;
        parameterPanel.add(new ScrollDisplay(ptm2ListDisplay), c);

        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0;
        parameterPanel.add(distanceInstructions, c);

        c.gridx = 1;
        c.gridy = 2;
        c.weighty = 1;
        parameterPanel.add(distanceField, c);
	}
	
	private void createOptionsPanel() {
		optionsPanel = new JPanel(new GridBagLayout());
		optionsPanel.setBorder(BorderFactory.createTitledBorder("Options"));
		
		cumulativeInstructions = new JLabel("Cumulative count");
		cumulativeFlag         = new JCheckBox("", false);
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1;
        optionsPanel.add(cumulativeInstructions, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 1;
        optionsPanel.add(cumulativeFlag, c);
	}


}
