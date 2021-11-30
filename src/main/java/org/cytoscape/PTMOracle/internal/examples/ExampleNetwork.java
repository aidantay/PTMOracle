package org.cytoscape.PTMOracle.internal.examples;

import java.util.Map;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;

/**
 * ExampleNetwork represents a network that can be visualised in Cytoscape.
 * Networks are created by parsing node and edge columns from file.
 * This is similar to importing directly into Cytoscape (except its from code).
 * @author aidan
 */
public interface ExampleNetwork {

	public CyNetwork getNetwork();
	public Map<String, CyNode> getNodeMap();

	public void setNetwork(CyNetwork network);
	
	public void parseNetwork();
	public void applyLayout(CyNetworkView networkView);

	public void createColumns();
	public String getNetworkName();	
}
