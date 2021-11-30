package org.cytoscape.PTMOracle.internal.results.tasks;

import javax.swing.JTabbedPane;

import org.cytoscape.PTMOracle.internal.results.swing.ResultsSummaryPanel;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

/**
 * Task for refreshing tabs in OracleResults.
 * @author 
 */
public class RefreshResultsTask extends AbstractTask {

	private JTabbedPane tabbedPane;
	
	public RefreshResultsTask(JTabbedPane tabbedPane) {
		super();
		
		this.tabbedPane = tabbedPane;
	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Refreshing results");
		taskMonitor.setStatusMessage("OracleResults refreshed");

		// For each tab, create a new summary panel and swap it with old
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			ResultsSummaryPanel prevSummaryPanel = (ResultsSummaryPanel) tabbedPane.getComponentAt(i);
			ResultsSummaryPanel currSummaryPanel = new ResultsSummaryPanel(prevSummaryPanel.getRootNetwork(), prevSummaryPanel.getComponent());
			tabbedPane.setComponentAt(i, currSummaryPanel);
		}
	}
	
}