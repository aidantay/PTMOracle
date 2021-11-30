package org.cytoscape.PTMOracle.internal.examples.yeast;

import org.cytoscape.PTMOracle.internal.examples.AbstractExampleNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;

/**
 * YeastMethylproteome represents an in-house curated yeast methyltransferase-substrate interaction network.
 * Kinase-substrate interactions are also included.
 * TODO Update of table data required
 * @author aidan
 */
public class YeastMethylproteome extends AbstractExampleNetwork {

	private static final String YEAST_METHYLPROTEOME_NAME  = "Yeast Methylproteome";
	private static final String YEAST_METHYLPROTEOME_EDGES = "yeast_methylproteome_edge_table.txt";
	private static final String YEAST_METHYLPROTEOME_NODES = "yeast_methylproteome_node_table.txt";
	private static final int YEAST_METHYLPROTEOME_COLUMNS  = 13;
		
	public YeastMethylproteome() {
		super();
	}
	
	@Override
	public String getNetworkNodeFile() {
		return YEAST_METHYLPROTEOME_NODES;
	}

	@Override
	public int getNetworkNodeColumnNumber() {
		return YEAST_METHYLPROTEOME_COLUMNS;
	}

	@Override
	public String getNetworkEdgeFile() {
		return YEAST_METHYLPROTEOME_EDGES;
	}
	
	@Override
	public String getNetworkName() {
		return YEAST_METHYLPROTEOME_NAME;
	}
	
	@Override
	public void nodeCheck(String[] parts) {
		
		String uniprotAcc         = parts[0];
		String olnId              = parts[1];
		String geneName           = parts[2];
		String description        = parts[3];
		String enzymeType         = parts[4];
		boolean kinaseTarget      = Boolean.valueOf(parts[5]);
		boolean arginineTarget    = Boolean.valueOf(parts[6]);
		boolean lysineTarget      = Boolean.valueOf(parts[7]);
		boolean methylationTarget = Boolean.valueOf(parts[8]);
		String methylationType    = parts[9];
		String vizMap             = parts[10];

		if (!getNodeMap().containsKey(olnId)) {
			CyNode node = getNetwork().addNode();
			
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("name", olnId);
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("uniprotAcc", uniprotAcc);
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("geneName", geneName);
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("description", description);
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("enzymeType", enzymeType);
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("kinaseTarget", kinaseTarget);
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("arginineTarget", arginineTarget);
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("lysineTarget", lysineTarget);
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("methylationTarget", methylationTarget);
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("methylationType", methylationType);
			getNetwork().getDefaultNodeTable().getRow(node.getSUID()).set("vizMapping", vizMap);

            getNodeMap().put(olnId, node);
		}
	}
	
	@Override
	public void createColumns() {		
		CyTable nodeTable = getNetwork().getDefaultNodeTable();
		nodeTable.createColumn("uniprotAcc", String.class, false);
		nodeTable.createColumn("geneName", String.class, false);
		nodeTable.createColumn("description", String.class, false);
		nodeTable.createColumn("enzymeType", String.class, false);
		nodeTable.createColumn("kinaseTarget", Boolean.class, false);
		nodeTable.createColumn("arginineTarget", Boolean.class, false);
		nodeTable.createColumn("lysineTarget", Boolean.class, false);
		nodeTable.createColumn("methylationTarget", Boolean.class, false);
		nodeTable.createColumn("methylationType", String.class, false);
		nodeTable.createColumn("vizMapping", String.class, false);
	}
}
