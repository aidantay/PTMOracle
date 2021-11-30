package org.cytoscape.PTMOracle.internal.painter.tasks;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;
import org.cytoscape.work.util.BoundedDouble;

/**
 * Task for modifying OracleResults options.
 * @author 
 */
public class PainterOptionsTask extends AbstractTask {

	@Tunable(description="Change proportion", params="slider=true")
	public BoundedDouble proportionField;
	
	@Tunable(description="Clear previous paint")
	public boolean enableRefreshField;
	
	@Tunable(description="Selected nodes only")
	public boolean paintSelectedNodesField;
	
	private static double proportion          = 1.3;		// By default, proportion is 1.3 (slightly bigger than normal)
	private static boolean enableRefresh      = false;		// By default, do not clear color scheme before applying new one
	private static boolean paintSelectedNodes = false;		// By default, paint the whole network
	
	public PainterOptionsTask() {
		super();

		this.proportionField         = new BoundedDouble(0.0, proportion, 2.0, false, false);
		this.enableRefreshField      = enableRefresh;
		this.paintSelectedNodesField = paintSelectedNodes;
	}
		
	@ProvidesTitle
	public String getMenuTitle() {
		return "OraclePainter Options";
	}
	
	public double getProportion() {
		return proportion;
	}

	public boolean getEnableRefreshFlag() {
		return enableRefresh;
	}

	public boolean getPaintSelectedNodesFlag() {
		return paintSelectedNodes;
	}
	
	public BoundedDouble getProportionField() {
		return proportionField;
	}
	
	public boolean getEnableRefreshField() {
		return enableRefreshField;
	}
	
	public boolean getPaintSelectedNodesField() {
		return paintSelectedNodesField;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Updating painter options");
		taskMonitor.setStatusMessage("Options updated for OraclePainter");
		
		// Set the graphic proportion to 0.1.
		// We cannot have a proportion of 0, this will mean theres no graphic
		if (getProportionField().getValue() == 0.0) {
			getProportionField().setValue(0.1);
		}

		proportion         = getProportionField().getValue();
		enableRefresh      = getEnableRefreshField();
		paintSelectedNodes = getPaintSelectedNodesField();
	}	
	
}