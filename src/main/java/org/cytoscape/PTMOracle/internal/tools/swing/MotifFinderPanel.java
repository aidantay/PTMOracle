package org.cytoscape.PTMOracle.internal.tools.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cytoscape.PTMOracle.internal.util.swing.TextDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.impl.DescriptionTextDisplay;

/**
 * Primary panel for MotifFinder OracleTool. 
 * @author aidan
 */
public class MotifFinderPanel extends JPanel {

	private static final long serialVersionUID = -8454610429405691132L;
	
	private static final String DESCRIPTION = "Identifies nodes whose sequence contains\n"
											  + "a specified sequence motif.\n"
											  + "Input sequence motifs as a Regular Expression";
	
	private JPanel descriptionPanel;
	private TextDisplay descriptionPane;

	private JPanel parameterPanel;
	private JLabel motifInstructions;
	private JTextField motifField;
	
	public MotifFinderPanel() {
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
	
	public String getMotifField() {
		return motifField.getText();
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

		motifInstructions = new JLabel("Motif pattern");
		motifField        = new JTextField(10);
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1;
        parameterPanel.add(motifInstructions, c);
        
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 1;
        parameterPanel.add(motifField, c);
	}

}
