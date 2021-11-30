package org.cytoscape.PTMOracle.internal.results.tasks;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.cytoscape.PTMOracle.internal.results.swing.ResultsSummaryPanel;
import org.cytoscape.PTMOracle.internal.util.tasks.AbstractRootNetworkTask;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.work.TaskMonitor;

/**
 * Task for creating the results summary tabs in the Results Panel.
 * @author aidan
 */
public class CreateResultsSummaryTask extends AbstractRootNetworkTask {

	private JTabbedPane tabbedPane;
	private List<String> queryNames;

	private boolean isNode;
	private String searchColumnName;
	
	public CreateResultsSummaryTask(CyNetwork network, JTabbedPane tabbedPane, List<String> queryNames, String searchColumnName) {
		super(network);
		
		this.tabbedPane = tabbedPane;
		this.queryNames = queryNames;
		this.searchColumnName = searchColumnName.substring(6);
		this.isNode = searchColumnName.substring(0, 4).equals("Node") ? true : false; 
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Creating results");

		// Get the row identifiers for each queryname
		// This determines which queries are valid and invalid
		Map<Boolean, List<Object>> rowIdentifiers = getRowIdentifiers();
		
		// Create results panel for valid queries
		if (rowIdentifiers.containsKey(true)) {
			List<Object> validRowIdentifiers = rowIdentifiers.get(true);
			processValidQueries(validRowIdentifiers);
		}
		
		// Deal with invalid queries
		if (rowIdentifiers.containsKey(false)) {
			List<Object> invalidRowIdentifiers = rowIdentifiers.get(false);
			processInvalidQueries(invalidRowIdentifiers);
		}

	}

	private Map<Boolean, List<Object>> getRowIdentifiers() {
		Map<Boolean, List<Object>> rowIdentifierMap = new HashMap<Boolean, List<Object>>();
		
		// Go through each query
		for (String queryName : queryNames) {
			Collection<CyRow> rows;
			
			// If we are dealing with the SUID, then we just have to make sure the queryname is a Long
			if (searchColumnName.equals(CyRootNetwork.SUID)) {
				rows = isNode ? getRootNetwork().getSharedNodeTable().getMatchingRows(searchColumnName, Long.valueOf(queryName)) : 
								getRootNetwork().getSharedEdgeTable().getMatchingRows(searchColumnName, Long.valueOf(queryName));

			} else {
				rows = isNode ? getRootNetwork().getSharedNodeTable().getMatchingRows(searchColumnName, queryName) : 
								getRootNetwork().getSharedEdgeTable().getMatchingRows(searchColumnName, queryName);
			}
			
			// If the query is valid, get the row identifiers
			if (rows.size() == 1) {
				for (CyRow row : rows) {
					List<Object> currList = (rowIdentifierMap.containsKey(true)) ? rowIdentifierMap.get(true) : new ArrayList<Object>(); 
					String suid = String.valueOf(row.getRaw(CyRootNetwork.SUID));
					String name = (String) row.getRaw(CyRootNetwork.SHARED_NAME);
					Pair<String, String> rowIdentifier = new ImmutablePair<String, String>(suid, name);
					
					currList.add(rowIdentifier);
					rowIdentifierMap.put(true, currList);
				}
				
			// This means we got either:
			// * No nodes in the network with the corresponding IDs or
			// * Multiple nodes in the network with the same ID
			} else {
				List<Object> currList = (rowIdentifierMap.containsKey(false)) ? rowIdentifierMap.get(false) : new ArrayList<Object>();
				currList.add(queryName);
				rowIdentifierMap.put(false, currList);
			}
		}
		
		return rowIdentifierMap;
	}
	
	@SuppressWarnings("unchecked")
	private void processValidQueries(List<Object> validRowIdentifiers) {
		
		for (Object o : validRowIdentifiers) {
			Pair<String, String> rowIdentifier = (Pair<String, String>) o;
			String suidIdentifier = rowIdentifier.getLeft();
			String nameIdentifier = rowIdentifier.getRight();
			
			// Create a panel if we currently don't have one
			// Otherwise, just ignore
			if (!hasSummaryPanel(nameIdentifier)) {
				createSummaryPanel(suidIdentifier, nameIdentifier);
			}
		}
		
	}
	
	public boolean hasSummaryPanel(String nameIdentifier) {
		int tabIndex = tabbedPane.indexOfTab(nameIdentifier);
		if (tabIndex == -1) {
			return false;
			
		} else {
			ResultsSummaryPanel summaryPanel = (ResultsSummaryPanel) tabbedPane.getComponentAt(tabIndex);
			if (!summaryPanel.getRootNetwork().toString().equals(getRootNetwork().toString())) {
				return false;
			}
		}
		tabbedPane.setSelectedIndex(tabIndex);
		return true;
	}
	
	private void createSummaryPanel(String suidIdentifier, String nameIdentifier) {
		
		if (isNode) {
			CyNode node = getRootNetwork().getNode(Long.valueOf(suidIdentifier));
			tabbedPane.addTab(nameIdentifier, new ResultsSummaryPanel(getRootNetwork(), node));
			tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(nameIdentifier));

		} else {
			CyEdge edge = getRootNetwork().getEdge(Long.valueOf(suidIdentifier));
			tabbedPane.addTab(nameIdentifier, new ResultsSummaryPanel(getRootNetwork(), edge));
			tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(nameIdentifier));
		}

		// Modify the tab with a close button
		// NOTE: The tab is actually a panel itself!
		int tabIndex = tabbedPane.indexOfTab(nameIdentifier);
		ButtonTabPanel buttonTabPanel = new ButtonTabPanel(nameIdentifier);
		tabbedPane.setTabComponentAt(tabIndex, buttonTabPanel);
	}
	
	private void processInvalidQueries(List<Object> invalidRowIdentifiers) throws Exception {
		
		String output = "Unable to find results for the following queries. Please see manual.\n";
		for (Object o : invalidRowIdentifiers) {
			String queryName = String.valueOf(o);
			output = output.concat("* " + queryName + "\n");
		}
		throw new Exception(output);

	}

	private class ButtonTabPanel extends JPanel implements ActionListener {
		
		private static final long serialVersionUID = 439843440718011768L;
	
		public ButtonTabPanel(String queryName) {
			super(new GridBagLayout());
			
			JButton btnClose = new JButton("x");
			btnClose.addActionListener(this);
	
			setOpaque(false);
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 1;
			add(new JLabel(queryName), gbc);
	
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.weightx = 0;
			add(btnClose, gbc);
		}
		
		public void actionPerformed(ActionEvent e) {
			int i = tabbedPane.indexOfTabComponent(this);
			if (i != -1) {
				tabbedPane.remove(i);
			}
		}
	}
	
}