package org.cytoscape.PTMOracle.internal.model.painter;

import java.awt.Image;
import java.util.List;

import org.cytoscape.PTMOracle.internal.model.PieChart;
import org.cytoscape.PTMOracle.internal.model.PieSlice;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;

/**
 * PieChart is the visual representation of a pie chart graphic that can be used in Cytoscape.
 * It is comprised of one or more PieSlices.
 * @author aidan
 */
public class PieChartImpl implements PieChart {

	private Long id;
	private float fitRatio;
	private List<PieSlice> layers;
	private String displayName;
	private int width = 100;
	private int height = 100;
	
	public PieChartImpl(List<PieSlice> layers, float fitRatio) {
		this.fitRatio = fitRatio;
		this.layers   = layers;
	}
	
	@Override
	public Long getIdentifier() {
		return id;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public float getFitRatio() {
		return fitRatio;
	}

	@Override
	public Image getRenderedImage() {
		return null;
	}
	
	@Override
	// A graphic is made up of a number of 'layers' 
	// This is analogous to the layers in photoshop
	public List<PieSlice> getLayers(CyNetworkView networkView,
			View<? extends CyIdentifiable> grView) {

		return layers;
	}
	
	@Override
	public void setIdentifier(Long id) {
		this.id = id;
	}

	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toSerializableString() {
		//return getIdentifier().toString() + "\t" + getDisplayName();
		return "OraclePainter";	// This fixes the issue of serialisation when saving the file, but I'm not entirely sure what this actually does in the long term...
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public void setFitRatio(float ratio) {
		this.fitRatio = ratio;
	}
	
}
