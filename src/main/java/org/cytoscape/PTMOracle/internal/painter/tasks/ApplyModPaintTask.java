package org.cytoscape.PTMOracle.internal.painter.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.cytoscape.PTMOracle.internal.model.ColorScheme;
import org.cytoscape.PTMOracle.internal.model.PieSlice;
import org.cytoscape.PTMOracle.internal.model.Property;
import org.cytoscape.PTMOracle.internal.model.PropertyMap;
import org.cytoscape.PTMOracle.internal.util.tasks.AbstractPaintNetworkTask;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.customgraphics.CyCustomGraphics;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;
import static org.cytoscape.PTMOracle.internal.schema.PropertyTable.PTM; 

/**
 * Task for rendering networks as piecharts according to the assigned color scheme.
 * This task is specifically for ModPainter
 * @author 
 */
public class ApplyModPaintTask extends AbstractPaintNetworkTask {

	public ApplyModPaintTask(CyNetwork network, boolean selectedNodes,
			double proportion, ColorScheme colorScheme) {
		
		super(network, selectedNodes, proportion, colorScheme);
	}
	
	public void paintNode(CyNode node, View<CyNode> nodeView) {
		// Get the properties of the node
		String sharedNodeName = (String) getRootNodeTable().getRow(node.getSUID()).getRaw(CyRootNetwork.SHARED_NAME);
		PropertyMap propertyMap = getOracle().getUniqueProperties(sharedNodeName, getRootNetwork().toString(), true);

		// Calculate the pie-chart proportions for the node
		Set<Property> properties = propertyMap.getPropertiesByType(PTM);
		Map<Pair<String, String>, Integer> proportions = getChartProportions(properties);

		// Create pie chart and apply it to on the node
		CyCustomGraphics<PieSlice> pieChart = createPieChart(proportions);
		nodeView.setLockedValue(getCustomVisualProperty(), pieChart);	// This is the bypass value
	}

	@SuppressWarnings("unchecked")
	public Map<Pair<String, String>, Integer> getChartProportions(Object o) {
		Set<Property> properties = (Set<Property>) o;
		Map<Pair<String, String>, Integer> proportions = new HashMap<Pair<String, String>, Integer>();

		for (Pair<String, String> pair : getColorScheme().getAllValues()) {
			String description = pair.getRight();
			Integer count = 0;
			
			if (properties != null) {
				for (Property property : properties) {
					if (description.equals(property.getDescription())) {
						count++;
					}
				}
				if (count != 0) {
					proportions.put(pair, count);
				}
			}
		}
		
		return proportions;
	}

}