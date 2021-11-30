package org.cytoscape.PTMOracle.internal.model;

import org.cytoscape.view.presentation.customgraphics.PaintedShape;

/**
 * PieSlice is the visual representation of an individual portion of a pie chart.
 * @author aidan
 */
public interface PieSlice extends PaintedShape {
	
	public double getArcStart();
	public double getArcLength();

}
