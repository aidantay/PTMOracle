package org.cytoscape.PTMOracle.internal.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

/**
 * Abstract implementation of an ExampleNetwork.
 * ExampleNetwork represents a network that can be visualised in Cytoscape.
 * Networks are created by parsing node and edge columns from file.
 * This is similar to importing directly into Cytoscape (except its from code).
 * @author aidan
 */
public abstract class AbstractExampleNetwork implements ExampleNetwork {

	private CyNetwork network;
	private Map<String, CyNode> nodeMap;
		
	public AbstractExampleNetwork() {
		this.nodeMap = new HashMap<String, CyNode>();
	}
	
	@Override
	public CyNetwork getNetwork() {
		return network;
	}
	
	@Override
	public void setNetwork(CyNetwork network) {
		this.network = network;
	}
	
	@Override
	public Map<String, CyNode> getNodeMap() {
		return nodeMap;
	}
	
	abstract public String getNetworkNodeFile();
	abstract public int getNetworkNodeColumnNumber();
	abstract public String getNetworkEdgeFile();

	public void parseNetwork() {
		// Parse the nodes file
		List<String> nodeList = parseNodeFile(getNetworkNodeFile());
		for (String s : nodeList) {
		    String[] parts = s.split("\\t", getNetworkNodeColumnNumber());
			nodeCheck(parts);
		}
		
		// Parse the edges file
		List<String> edgeList = parseEdgeFile(getNetworkEdgeFile());
		for (String s : edgeList) {
		    String[] parts = s.split("\\t");
		    edgeCheck(parts);
		}
	}

	private List<String> parseNodeFile(String nodeFile) {
		List<String> nodeList = new ArrayList<String>();
		try {
			
			// Read node file
			InputStream input = getClass().getResourceAsStream("/" + nodeFile);
			InputStreamReader inputReader = new InputStreamReader(input);
					
			BufferedReader reader = new BufferedReader(inputReader);
			String line;
				
			while ((line = reader.readLine()) != null) {
				nodeList.add(line);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nodeList;
	}

	// Only the nodeCheck is 'abstract public' because the edgeCheck function is the same for each example
	// If different implementations of edgeCheck are needed, then we can change it to 'abstract public'  
	abstract public void nodeCheck(String[] parts);
	
	private List<String> parseEdgeFile(String edgeFile) {
		List<String> edgeList = new ArrayList<String>();
		try {

			// Read edge file
			InputStream input = getClass().getResourceAsStream("/" + edgeFile);
			InputStreamReader inputReader = new InputStreamReader(input);
					
			BufferedReader reader = new BufferedReader(inputReader);
			String line;
					
			while ((line = reader.readLine()) != null) {
				edgeList.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return edgeList;
	}

	private void edgeCheck(String[] parts) {
		String interaction = parts[1];
			
		CyNode proteinA = getNodeMap().get(parts[0]);
		CyNode proteinB = getNodeMap().get(parts[2]);
			
		CyEdge edge = getNetwork().addEdge(proteinA, proteinB, false);
		String olnIdA = parts[0];
		String olnIdB = parts[2];
			
		String name = olnIdA + " (" + interaction + ") " + olnIdB;
			
		getNetwork().getDefaultEdgeTable().getRow(edge.getSUID()).set(CyRootNetwork.SHARED_NAME, name);
		getNetwork().getDefaultEdgeTable().getRow(edge.getSUID()).set(CyRootNetwork.SHARED_INTERACTION, interaction);
		getNetwork().getDefaultEdgeTable().getRow(edge.getSUID()).set("name", name);
		getNetwork().getDefaultEdgeTable().getRow(edge.getSUID()).set("interaction", interaction);
	}

	// We are just using arbitrary numbers here so we can get a general grid layout
	public void applyLayout(CyNetworkView networkView) {
		double nodeSeparator = 100;
		double networkSize = networkView.getNodeViews().size();
		double currX = 0;
		double currY = 0;
		
		double i;
		
		for (i = Math.floor(Math.sqrt(networkSize)); i > 2; i--) {
			if (networkSize % i == 0) {
				break;
			}
		}
		
		double maxX = ((networkSize / i) * nodeSeparator) - nodeSeparator;
		
		if (maxX > 7000) {
			maxX = 7000;
		}
		
		for (View<CyNode> nodeView : networkView.getNodeViews()) {
			nodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, currX);
			nodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, currY);
			if (currX >= maxX) {
				currY = currY + nodeSeparator;
				currX = 0;
			} else {
				currX = currX + nodeSeparator;
			}
		}
	}
}
