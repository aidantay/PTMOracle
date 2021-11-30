package org.cytoscape.PTMOracle.internal.painter.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.cytoscape.PTMOracle.internal.model.ColorScheme;
import org.cytoscape.PTMOracle.internal.model.PieSlice;
import org.cytoscape.PTMOracle.internal.util.tasks.AbstractPaintNetworkTask;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.customgraphics.CyCustomGraphics;

/**
 * Task for rendering networks as piecharts according to the assigned color scheme.
 * This task is specifically for MultiPainter
 * @author 
 */
public class ApplyMultiPaintTask extends AbstractPaintNetworkTask {

	public ApplyMultiPaintTask(CyNetwork network, boolean selectedNodes,
			double proportion, ColorScheme colorScheme) {
		
		super(network, selectedNodes, proportion, colorScheme);
	}

	@Override
	public void paintNode(CyNode node, View<CyNode> nodeView) {
		// Calculate the pie-chart proportions for the node
		Map<Pair<String, String>, Integer> proportions = getChartProportions(node);

		// Create pie chart and apply it to on the node
		CyCustomGraphics<PieSlice> pieChart = createPieChart(proportions);
		nodeView.setLockedValue(getCustomVisualProperty(), pieChart);	// This is the bypass value
	}

	@SuppressWarnings("unchecked")
	public Map<Pair<String, String>, Integer> getChartProportions(Object o) {
		CyNode node = (CyNode) o;
		Map<Pair<String, String>, Integer> proportions = new HashMap<Pair<String, String>, Integer>();

		for (Pair<String, String> pair : getColorScheme().getAllValues()) {
			String attribute = pair.getLeft();
			String value     = pair.getRight();
			boolean isList   = getRootNodeTable().getColumn(attribute).getType().equals(List.class);

			List<Object> idList = null;
			if (isList) {
				idList = (List<Object>) getRootNodeTable().getRow(node.getSUID()).getRaw(attribute);
			} else {
				Object id = getRootNodeTable().getRow(node.getSUID()).getRaw(attribute);
				idList = new ArrayList<Object>();
				idList.add(id);
			}

			if (idList != null) {
				for (Object i : idList) {
					String tableValue = (String) String.valueOf(i);
					if (tableValue.equals(value)) {
						proportions.put(pair, 1);
					}
				}
			}
		}
		
		return proportions;
	}
}