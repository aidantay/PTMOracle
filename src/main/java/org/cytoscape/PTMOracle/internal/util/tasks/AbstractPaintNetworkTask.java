package org.cytoscape.PTMOracle.internal.util.tasks;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.cytoscape.PTMOracle.internal.model.ColorScheme;
import org.cytoscape.PTMOracle.internal.model.PieSlice;
import org.cytoscape.PTMOracle.internal.model.painter.PieChartImpl;
import org.cytoscape.PTMOracle.internal.model.painter.PieSliceImpl;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.view.model.View;
import org.cytoscape.view.model.VisualLexicon;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.presentation.customgraphics.CyCustomGraphics;
import org.cytoscape.work.TaskMonitor;

import static org.cytoscape.PTMOracle.internal.util.CytoscapeServices.getCurrentRenderingEngine;
import static org.cytoscape.PTMOracle.internal.util.CytoscapeServices.getCurrentNetworkView;

/**
 * Task for rendering NODES in a network networks as piecharts.
 * Sections in the piecharts are colored according to the assigned scheme.
 * We CURRENTLY restrict the painters for ONLY nodes.
 * TODO Extend to handle edges [requires changes to OraclePainter]  
 * @author aidan
 */
public abstract class AbstractPaintNetworkTask extends AbstractRootNetworkTask {

	private boolean selectedNodes;
	private double proportion;
	private ColorScheme colorScheme;
	
	public AbstractPaintNetworkTask(CyNetwork network) {
		super(network);
	}
	
	public AbstractPaintNetworkTask(CyNetwork network, boolean selectedNodes, double proportion, ColorScheme colorScheme) {
		super(network);
		setSelectedNodes(selectedNodes);
		setProportion(proportion);
		setColorScheme(colorScheme);
	}

	public ColorScheme getColorScheme() {
		return colorScheme;
	}
	
	public boolean getSelectedNodes() {
		return selectedNodes;
	}
	
	public double getProportion() {
		return proportion;
	}
	
	public void setColorScheme(ColorScheme colorScheme) {
		this.colorScheme = colorScheme;
	}
	
	public void setSelectedNodes(boolean selectedNodes) {
		this.selectedNodes = selectedNodes;
	}
	
	public void setProportion(double proportion) {
		this.proportion = proportion;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Painting network");
		taskMonitor.setStatusMessage("Applying OraclePainter colour scheme to network");

		List<CyNode> nodeList = (getSelectedNodes()) ? CyTableUtil.getNodesInState(network, "selected", true) : network.getNodeList();
		paintNetwork(nodeList);
		
		getCurrentNetworkView().updateView();
	}
	
	private void paintNetwork(List<CyNode> nodeList) {
		// We only paint nodes in the CURRENT network
		for (CyNode node : nodeList) {
			View<CyNode> nodeView = getCurrentNetworkView().getNodeView(node);

			paintNode(node, nodeView);
		}
	}
	
	abstract public void paintNode(CyNode node, View<CyNode> nodeView);
	abstract public Map<Pair<String, String>, Integer> getChartProportions(Object o);

	public CyCustomGraphics<PieSlice> createPieChart(Map<Pair<String, String>, Integer> proportions) {

		double arcStart  = 0;
		double arcLength = 0;
		float proportion = 1;
		int totalCount   = 0;
		List<PieSlice> layers = new ArrayList<PieSlice>();

		for (Integer i : proportions.values()) {
			totalCount = totalCount + i;
		}
				
		// Create empty pie chart
		if (proportions.isEmpty()) {
			arcLength      = 360 * proportion;
			Color color    = new Color(217,217,217);
			PieSlice slice = new PieSliceImpl(arcStart, arcLength, color);
			layers.add(slice);
			
		// Create pie chart of PTMs
		} else {
			for (Pair<String, String> pair : getColorScheme().getAllValues()) {
				if (proportions.containsKey(pair)) {
					int count      = proportions.get(pair);
					proportion     = (float) count / totalCount;
					arcLength      = 360 * proportion;
					Color color    = getColorScheme().getColor(pair);
					PieSlice slice = new PieSliceImpl(arcStart, arcLength, color);
					layers.add(slice);
					arcStart = arcStart + arcLength;
				}
			}
		}
		
		CyCustomGraphics<PieSlice> pieChart = new PieChartImpl(layers, (float) getProportion());
		return pieChart;
	}

	@SuppressWarnings("unchecked")
	public VisualProperty<CyCustomGraphics<?>> getCustomVisualProperty() {
		VisualLexicon lexicon = getCurrentRenderingEngine().getVisualLexicon();
		return (VisualProperty<CyCustomGraphics<?>>) lexicon.lookup(CyNode.class, "NODE_CUSTOMGRAPHICS_9");	// We need to use THIS lexicon for custom graphics
	}
	
	
}
