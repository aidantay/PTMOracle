package org.cytoscape.PTMOracle.internal.util.swing.impl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.cytoscape.PTMOracle.internal.util.swing.TextDisplay;

/**
 * Primary panel for converting properties to Oracle format
 * @author aidan
 */
public class ConvertPropertyPanel extends JPanel {

	private static final long serialVersionUID = -8454610429405691132L;

	private static final String DESCRIPTION = "Converts TAB-format file into XML-format file for PTMOracle\n" +
	                                          "Follow the steps below to convert files:\n" +
	                                          " * Set the column index to the corresponding column in the TAB-format\n" +
	                                          "   file. The residue column can be set to -1 (if there is no residue)\n" +
	                                          " * Input a source description (eg. where the data is from)\n";
	                                          
	private JPanel descriptionPanel;
	private TextDisplay descriptionPane;
	
	private JPanel filePanel;
	private JLabel fileInstructions;
	private JTextPane filePath;

	private JPanel indexPanel;
	private JLabel idIndexLabel;
	private JTextField idIndexField;
	private JLabel typeIndexLabel;
	private JTextField typeIndexField;
	private JLabel descriptionIndexLabel;
	private JTextField descriptionIndexField;
	private JLabel startPositionIndexLabel;
	private JTextField startPositionIndexField;
	private JLabel endPositionIndexLabel;
	private JTextField endPositionIndexField;
	private JLabel residueIndexLabel;
	private JTextField residueIndexField;	

	public ConvertPropertyPanel(File file) {
		super(new GridBagLayout());

		createDescriptionPanel();
		createFilePanel(file);
		createIndexPanel();

		paint();
	}
	
	public void paint() {
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(5, 5, 5, 5);
		add(descriptionPanel, c);
		
		c.gridx = 0;
		c.gridy = 1;
		add(filePanel, c);
		
		c.gridx = 0;
		c.gridy = 2;
		add(indexPanel, c);
	}
	
	public List<String> getIndexList() {
		List<String> indexList = new ArrayList<String>();
		indexList.add(idIndexField.getText());
		indexList.add(typeIndexField.getText());
		indexList.add(descriptionIndexField.getText());
		indexList.add(startPositionIndexField.getText());
		indexList.add(endPositionIndexField.getText());
		indexList.add(residueIndexField.getText());
		return indexList;
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
	
	private void createFilePanel(File file) {
		filePanel = new JPanel(new GridBagLayout());
		filePanel.setBorder(BorderFactory.createTitledBorder("Output file data"));

		fileInstructions = new JLabel("Selected file:");
		
		filePath = new JTextPane();
		filePath.setEditable(false);
		filePath.setEditorKit(new WrapEditorKit());
		filePath.setText(file.getAbsolutePath());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        filePanel.add(fileInstructions, c);
        
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        filePanel.add(filePath, c);
	}
	
	private void createIndexPanel() {
		indexPanel = new JPanel(new GridBagLayout());
		indexPanel.setBorder(BorderFactory.createTitledBorder("File parameters:"));

		idIndexLabel = new JLabel("ID Column Index:");
		idIndexField = new JTextField(3);
		idIndexField.setDocument(new JTextFieldLimit(2));
		idIndexField.setText("0");

		typeIndexLabel = new JLabel("Type Column Index:");
		typeIndexField = new JTextField(3);
		typeIndexField.setDocument(new JTextFieldLimit(2));
		typeIndexField.setText("1");

		descriptionIndexLabel = new JLabel("Description Column Index:");
		descriptionIndexField = new JTextField(3);
		descriptionIndexField.setDocument(new JTextFieldLimit(2));
		descriptionIndexField.setText("2");
		
		startPositionIndexLabel = new JLabel("Start Position Column Index:");
		startPositionIndexField = new JTextField(3);
		startPositionIndexField.setDocument(new JTextFieldLimit(2));
		startPositionIndexField.setText("3");

		endPositionIndexLabel = new JLabel("End Position Column Index:");
		endPositionIndexField = new JTextField(3);
		endPositionIndexField.setDocument(new JTextFieldLimit(2));
		endPositionIndexField.setText("4");

		residueIndexLabel = new JLabel("Residue Column Index:");
		residueIndexField = new JTextField(3);
		residueIndexField.setDocument(new JTextFieldLimit(2));
		residueIndexField.setText("5");
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.insets = new Insets(5, 5, 5, 5);
        indexPanel.add(idIndexLabel, c);
        
        c.gridx = 1;
        c.gridy = 0;
        indexPanel.add(idIndexField, c);

        c.gridx = 0;
        c.gridy = 1;
        indexPanel.add(typeIndexLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        indexPanel.add(typeIndexField, c);

        c.gridx = 0;
        c.gridy = 2;
        indexPanel.add(descriptionIndexLabel, c);

        c.gridx = 1;
        c.gridy = 2;
        indexPanel.add(descriptionIndexField, c);

        c.gridx = 0;
        c.gridy = 3;
        indexPanel.add(startPositionIndexLabel, c);

        c.gridx = 1;
        c.gridy = 3;
        indexPanel.add(startPositionIndexField, c);

        c.gridx = 0;
        c.gridy = 4;
        indexPanel.add(endPositionIndexLabel, c);

        c.gridx = 1;
        c.gridy = 4;
        indexPanel.add(endPositionIndexField, c);

        c.gridx = 0;
        c.gridy = 5;
        indexPanel.add(residueIndexLabel, c);

        c.gridx = 1;
        c.gridy = 5;
        indexPanel.add(residueIndexField, c);
	}
	
}
