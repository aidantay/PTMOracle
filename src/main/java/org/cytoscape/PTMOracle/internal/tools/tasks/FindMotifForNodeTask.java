package org.cytoscape.PTMOracle.internal.tools.tasks;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cytoscape.PTMOracle.internal.model.NodeProperty;
import org.cytoscape.PTMOracle.internal.model.PropertyCollection;
import org.cytoscape.PTMOracle.internal.model.PropertyMap;
import org.cytoscape.PTMOracle.internal.model.property.NodePropertyImpl;
import org.cytoscape.PTMOracle.internal.model.property.PropertyCollectionImpl;
import org.cytoscape.PTMOracle.internal.schema.PropertyTable;
import org.cytoscape.PTMOracle.internal.schema.tasks.UpdateCytoscapeTask;
import org.cytoscape.PTMOracle.internal.schema.tasks.UpdateOracleTask;
import org.cytoscape.PTMOracle.internal.util.tasks.AbstractRootNetworkTask;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;
import static org.cytoscape.PTMOracle.internal.schema.KeywordTable.NOT_APPLICABLE;

/**
 * Task for finding motif sequences
 * @author aidan
 */
public class FindMotifForNodeTask extends AbstractRootNetworkTask {

	private String motifPattern;
	
	private static int motifFinderIndex = 0;
	private PropertyCollection motifsToAdd;
	
	public FindMotifForNodeTask(CyNetwork network, String motifPattern) {
		super(network);

		this.motifPattern     = motifPattern;
		this.motifsToAdd      = new PropertyCollectionImpl();
	}
	
	public String getMotifPattern() {
		return motifPattern;
	}

	public PropertyCollection getMotifsToAdd() {
		return motifsToAdd;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		// Set the MotifFinderIndex. This is for keeping track of what we have and haven't imported into the Oracle
		setMotifFinderIndex();		
			
		// Find all occurrences of the motif in each node 
		findMotif();
		
		// Dealing with motifs that want to be added to Oracle.
		importMotifToOracle();
	}
	
	private void setMotifFinderIndex() {
		Pattern p = Pattern.compile("MotifFinder([0-9]+)");
		for (String source : getOracle().getNetworkMappingTable().getSources(getRootNetwork().toString())) {
		   	Matcher matcher = p.matcher(source);
		   	while (matcher.find()) {
		   		if (Integer.valueOf(matcher.group(1)) > FindMotifForNodeTask.motifFinderIndex) {
		   			FindMotifForNodeTask.motifFinderIndex = Integer.valueOf(matcher.group(1));
		   		}
		   	}
		}
		FindMotifForNodeTask.motifFinderIndex++;
	}
	
	private void findMotif() {
		for (CyNode node : getRootNetwork().getNodeList()) {
			String sharedNodeName = (String) getRootNetwork().getSharedNodeTable().getRow(node.getSUID()).getRaw(CyRootNetwork.SHARED_NAME);			
			PropertyMap uniquePropertyMap = getOracle().getUniqueProperties(sharedNodeName, getRootNetwork().toString(), true);
			String sequence = (uniquePropertyMap.hasSequence()) ?
							   uniquePropertyMap.getSequence().getDescription() :
							   "";
	
			isMotifPatternInSequence(sequence, sharedNodeName);
		}
	}
	
	private void isMotifPatternInSequence(String sequence, String sharedNodeName) {
		Pattern p = Pattern.compile(getMotifPattern());
	   	Matcher matcher = p.matcher(sequence);
	   	while (matcher.find()) {
   			int start = matcher.start() + 1;
   			int end   = matcher.end();
   			NodeProperty property = new NodePropertyImpl(PropertyTable.MOTIF, getMotifPattern(), start, end);
   			getMotifsToAdd().addProperty(sharedNodeName, property);
	   	}
	}
	
	@SuppressWarnings("rawtypes")
	private void importMotifToOracle() {
		List newAttributes = Arrays.asList(getMotifPattern(), PropertyTable.MOTIF, NOT_APPLICABLE, getMotifPattern());
		getOracle().getKeywordTable().insertRow(newAttributes);
		
		UpdateOracleTask updateOracleTask = new UpdateOracleTask(network, getMotifsToAdd(), 
				"MotifFinder" + String.valueOf(motifFinderIndex), CyRootNetwork.SHARED_NAME);
		
		UpdateCytoscapeTask updateCytoscapeTask = new UpdateCytoscapeTask();
		
		TaskIterator iterator = new TaskIterator();
		iterator.append(updateOracleTask);
		iterator.append(updateCytoscapeTask);
		insertTasksAfterCurrentTask(iterator);
	}

}
