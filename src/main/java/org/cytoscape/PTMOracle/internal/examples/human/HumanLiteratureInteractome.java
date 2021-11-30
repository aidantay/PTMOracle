package org.cytoscape.PTMOracle.internal.examples.human;

import java.util.Arrays;
import java.util.List;

import org.cytoscape.PTMOracle.internal.examples.AbstractExampleNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;

/**
 * HumanLiteratureInteractome represents the literature-curated human PPI network from Rolland et al. (2014).
 * @author aidan
 */
public class HumanLiteratureInteractome extends AbstractExampleNetwork {
	
	private static final String HUMAN_INTERACTOME_NAME  = "Human Literature Interactome"; 
	private static final String HUMAN_INTERACTOME_EDGES = "human_literature_interactome_edge_table.txt";
	private static final String HUMAN_INTERACTOME_NODES = "human_literature_interactome_node_table.txt";
	private static final int HUMAN_INTERACTOME_COLUMNS  = 4;
	
	public HumanLiteratureInteractome() {
		super();
	}
	
	@Override
	public String getNetworkNodeFile() {
		return HUMAN_INTERACTOME_NODES;
	}

	@Override
	public int getNetworkNodeColumnNumber() {
		return HUMAN_INTERACTOME_COLUMNS;
	}

	@Override
	public String getNetworkEdgeFile() {
		return HUMAN_INTERACTOME_EDGES;
	}
	
	@Override
	public String getNetworkName() {
		return HUMAN_INTERACTOME_NAME;
	}

	@Override
	public void nodeCheck(String[] parts) {
		String entrezId          = parts[0];
		String entrezGeneSymbol  = parts[1];
		String description       = parts[2];
		List<String> uniprotAccs = Arrays.asList(parts[3].split("\\|"));
		
		if (!getNodeMap().containsKey(entrezId)) {
			CyNode node = getNetwork().addNode();
			
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("name", entrezId);
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("entrezGeneSymbol", entrezGeneSymbol);
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("descriptions", description);
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("uniprotAccs", uniprotAccs);

            getNodeMap().put(entrezId, node);
		}
	}
	
	@Override
	public void createColumns() {		
		CyTable nodeTable = getNetwork().getDefaultNodeTable();
			
		nodeTable.createColumn("entrezGeneSymbol", String.class, false);
		nodeTable.createColumn("descriptions", String.class, false);
		nodeTable.createListColumn("uniprotAccs", String.class, false);
	}
}
