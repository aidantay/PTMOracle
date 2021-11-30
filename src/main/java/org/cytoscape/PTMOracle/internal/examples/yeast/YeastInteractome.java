package org.cytoscape.PTMOracle.internal.examples.yeast;

import org.cytoscape.PTMOracle.internal.examples.AbstractExampleNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;

/**
 * YeastInteractome represents the yeast PPI network from Pang et al. (2012).
 * TODO Update of table data required
 * @author aidan
 */
public class YeastInteractome extends AbstractExampleNetwork {
	
	private static final String YEAST_INTERACTOME_NAME     = "Yeast Interactome"; 
	private static final String YEAST_INTERACTOME_EDGES    = "yeast_interactome_edge_table.txt";
	private static final String YEAST_INTERACTOME_NODES    = "yeast_interactome_node_table.txt";
	private static final int YEAST_INTERACTOME_COLUMNS     = 4;	
	
	public YeastInteractome() {
		super();
	}

	@Override
	public String getNetworkNodeFile() {
		return YEAST_INTERACTOME_NODES;
	}

	@Override
	public int getNetworkNodeColumnNumber() {
		return YEAST_INTERACTOME_COLUMNS;
	}

	@Override
	public String getNetworkEdgeFile() {
		return YEAST_INTERACTOME_EDGES;
	}
	
	@Override
	public String getNetworkName() {
		return YEAST_INTERACTOME_NAME;
	}

	@Override
	public void nodeCheck(String[] parts) {
		
		String uniprotAcc  = parts[0];
		String olnId       = parts[1];
		String geneName    = parts[2];
		String description = parts[3];

		if (!getNodeMap().containsKey(olnId)) {
			CyNode node = getNetwork().addNode();
			
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("name", olnId);
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("uniprotAcc", uniprotAcc);
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("geneName", geneName);
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("description", description);

            getNodeMap().put(olnId, node);
		}
	}
	
	@Override
	public void createColumns() {
		CyTable nodeTable = getNetwork().getDefaultNodeTable();
			
		nodeTable.createColumn("uniprotAcc", String.class, false);
		nodeTable.createColumn("geneName", String.class, false);
		nodeTable.createColumn("description", String.class, false);
	}

}
