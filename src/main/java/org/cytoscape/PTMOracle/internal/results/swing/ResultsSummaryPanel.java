package org.cytoscape.PTMOracle.internal.results.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import org.cytoscape.PTMOracle.internal.schema.swing.EdgePropertyTableDisplay;
import org.cytoscape.PTMOracle.internal.schema.swing.NodePropertyTableDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.ScrollDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;
import org.cytoscape.PTMOracle.internal.util.tasks.UpdateTextDisplayTask;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.work.TaskIterator;

import static org.cytoscape.PTMOracle.internal.util.CytoscapeServices.getTaskManager;

/**
 * Panel for showing the results of all properties mapped onto a network 
 * component, such as NODES and EDGES. Format of results varies depending 
 * on the network component queried.
 * @author aidan
 */
public class ResultsSummaryPanel extends JPanel {

	private static final long serialVersionUID = 8388762113272140501L;
	
	private JPanel infoPanel;
	private JLabel networkInstructions;
	private JTextField networkLabel;
	private JLabel componentInstructions;	
	private JTextField componentLabel;

	private TableDisplay propertyTableDisplay; 
	private ProteinSequenceTextDisplay sequenceTextDisplay;
	
	private JSplitPane splitPane;
	
	private CyRootNetwork rootNetwork;
	private CyIdentifiable component;
	
	public ResultsSummaryPanel(CyRootNetwork rootNetwork, CyIdentifiable component) {
		super(new GridBagLayout());

		this.rootNetwork = rootNetwork;
		this.component   = component;

		createResults();
		paint();
	}
	
	public CyRootNetwork getRootNetwork() {
		return rootNetwork;
	}
	
	public CyIdentifiable getComponent() {
		return component;
	}

	public boolean isNodeResults() {
		if (component instanceof CyNode) {
			return true;
		}
		return false;
	}
	
	public void createResults() {
		if (isNodeResults()) {
			createNodeInfoPanel();			
		} else {
			createEdgeInfoPanel();
		}
	}
		
	public void paint() {
	    GridBagConstraints c = new GridBagConstraints();
		
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.gridx = 0;
	    c.gridy = 0;
	    c.weightx = 1;
	    c.insets = new Insets(5, 5, 5, 5);
	    add(infoPanel, c);
		        
	    c.fill = GridBagConstraints.BOTH;
	    c.gridx = 0;
	    c.gridy = 1;
	    c.weightx = 1;
	    c.weighty = 0.8;
		add(splitPane, c);
	}

	private void createNodeInfoPanel() {
		String sharedNodeName = (String) rootNetwork.getSharedNodeTable().getRow(component.getSUID()).getRaw(CyRootNetwork.SHARED_NAME);

		createInfoPanel(sharedNodeName);
		
		propertyTableDisplay = new NodePropertyTableDisplay(sharedNodeName, rootNetwork.toString());
		sequenceTextDisplay = new ProteinSequenceTextDisplay(sharedNodeName, rootNetwork.toString());
		getTaskManager().execute(new TaskIterator(new UpdateTextDisplayTask(sequenceTextDisplay)));
		
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new ScrollDisplay(propertyTableDisplay), new ScrollDisplay(sequenceTextDisplay));
		splitPane.setBorder(BorderFactory.createEtchedBorder());
		splitPane.setDividerLocation(0.7);
		splitPane.setResizeWeight(0.6);			
	}

	private void createEdgeInfoPanel() {
		String sharedEdgeName = (String) rootNetwork.getSharedEdgeTable().getRow(component.getSUID()).getRaw(CyRootNetwork.SHARED_NAME);
		
		createInfoPanel(sharedEdgeName);
		
		propertyTableDisplay = new EdgePropertyTableDisplay(sharedEdgeName, rootNetwork.toString());
		
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new ScrollDisplay(propertyTableDisplay), new JPanel());
		splitPane.setEnabled(false);
		splitPane.setBorder(BorderFactory.createEtchedBorder());
		splitPane.setDividerLocation(1.0);
		splitPane.setResizeWeight(1.0);			
	}

	private void createInfoPanel(String componentName) {
		infoPanel = new JPanel(new GridBagLayout());
		infoPanel.setBorder(BorderFactory.createTitledBorder("Background information"));

		networkInstructions = new JLabel("Network Collection Name:");		
		networkLabel = new JTextField(rootNetwork.toString());
		networkLabel.setEditable(false);
		
		componentInstructions = new JLabel("Component Name:");
		componentLabel = new JTextField(componentName);
		componentLabel.setEditable(false);

		GridBagConstraints c = new GridBagConstraints();
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.insets = new Insets(5, 5, 5, 5);
	    c.gridx = 0;
	    c.gridy = 0;
	    infoPanel.add(networkInstructions, c);

	    c.gridx = 1;
        c.gridy = 0;
	    c.weightx = 1;
	    infoPanel.add(networkLabel, c);

		c.gridx = 0;
		c.gridy = 1;
	    c.weightx = 0;
	    infoPanel.add(componentInstructions, c);
		
		c.gridx = 1;
		c.gridy = 1;
	    c.weightx = 1;
	    infoPanel.add(componentLabel, c);
	}

}

