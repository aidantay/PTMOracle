package org.cytoscape.PTMOracle.internal.schema.swing;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.cytoscape.PTMOracle.internal.util.CytoscapeServices;
import org.cytoscape.PTMOracle.internal.util.swing.ScrollDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.impl.WrapEditorKit;
import org.cytoscape.model.CyNetwork;

/**
 * Primary panel for exporting properties from the oracle.
 * @author aidanNode
 */
public class ExportPropertiesPanel extends JPanel {

	private static final long serialVersionUID = -7710622807529835242L;
	
	private static final String NODE = "NodeProperties";
	private static final String EDGE = "EdgeProperties";

	private JPanel networkPanel;
	private JLabel currentNetworkInstructions;
	private JLabel currentNetwork;
	
	private JPanel filePanel;
	private JLabel fileInstructions;
	private JTextPane filePath;
	
	private JPanel propertyListPanel;
	private JList<String> propertyList;
	private DefaultListModel<String> propertyListModel;
	
	private JPanel viewPanel;
	private TableDisplay nodePropertyTableDisplay;
	private TableDisplay edgePropertyTableDisplay;

	public ExportPropertiesPanel(CyNetwork network, File file) {
		super(new GridBagLayout());
		
		createCurrentNetworkPanel(network);
		createFilePanel(file);
		createViewPanel();
		createPropertyListPanel();

		paint();
	}
		
	public void paint() {
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.weightx = 1;
		c.insets = new Insets(5, 5, 5, 5);
		add(networkPanel, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		add(filePanel, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.weighty = 1;
		c.weightx = 0;
		add(propertyListPanel, c);

		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 1;
		add(viewPanel, c);
	}
	
	public TableDisplay getTableDisplay() {
		if (propertyList.getSelectedValue().equals(NODE)) {
			return nodePropertyTableDisplay;
		}
		return edgePropertyTableDisplay;
	}
	
	private void createCurrentNetworkPanel(CyNetwork network) {
		networkPanel = new JPanel(new GridBagLayout());
		networkPanel.setBorder(BorderFactory.createTitledBorder("Target network data"));
		
		currentNetworkInstructions = new JLabel("Current Network: ");
		currentNetwork = new JLabel(CytoscapeServices.getRootNetwork(network).toString());
		
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
	
	private void createViewPanel() {
		nodePropertyTableDisplay = new NodePropertyTableDisplay();
		edgePropertyTableDisplay = new EdgePropertyTableDisplay();

		viewPanel = new JPanel(new CardLayout());	

		viewPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		viewPanel.add(new ScrollDisplay(nodePropertyTableDisplay), NODE);
		viewPanel.add(new ScrollDisplay(edgePropertyTableDisplay), EDGE);
	}
	
	private void createPropertyListPanel() {
		propertyListPanel = new JPanel(new GridBagLayout());		
		
		propertyListModel = new DefaultListModel<String>();
		propertyListModel.addElement(NODE);
//		propertyListModel.addElement(EDGE);
		
		propertyList = new JList<String>(propertyListModel);
		propertyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		propertyList.addListSelectionListener(new PropertyListSelectionListener());
		propertyList.setSelectedIndex(0);
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1;
        c.weighty = 1;
        propertyListPanel.add(new ScrollDisplay(propertyList), c);		
	}

	private class PropertyListSelectionListener implements ListSelectionListener {

		@Override
		@SuppressWarnings("rawtypes")
		public void valueChanged(ListSelectionEvent e) {
			JList list = (JList) e.getSource();
			String value = (String) list.getSelectedValue();
			
			CardLayout layout = (CardLayout) viewPanel.getLayout();
			layout.show(viewPanel, value);
		}
	}

}
