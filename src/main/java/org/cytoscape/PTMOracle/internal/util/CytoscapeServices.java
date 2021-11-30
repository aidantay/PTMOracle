package org.cytoscape.PTMOracle.internal.util;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cytoscape.PTMOracle.internal.painter.swing.OraclePainterPanel;
import org.cytoscape.PTMOracle.internal.results.swing.OracleResultsPanel;
import org.cytoscape.PTMOracle.internal.results.swing.ResultsSummaryPanel;
import org.cytoscape.app.event.AppsFinishedStartingEvent;
import org.cytoscape.app.event.AppsFinishedStartingListener;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.events.SetCurrentNetworkEvent;
import org.cytoscape.application.events.SetCurrentNetworkListener;
import org.cytoscape.application.events.SetCurrentNetworkViewEvent;
import org.cytoscape.application.events.SetCurrentNetworkViewListener;
import org.cytoscape.application.events.SetCurrentRenderingEngineEvent;
import org.cytoscape.application.events.SetCurrentRenderingEngineListener;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableMetadata;
import org.cytoscape.model.events.NetworkAboutToBeDestroyedEvent;
import org.cytoscape.model.events.NetworkAboutToBeDestroyedListener;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.session.events.SessionLoadedEvent;
import org.cytoscape.session.events.SessionLoadedListener;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.RenderingEngine;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.events.SetCurrentVisualStyleEvent;
import org.cytoscape.view.vizmap.events.SetCurrentVisualStyleListener;
import org.cytoscape.work.TaskManager;
import org.cytoscape.work.TunableSetter;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;

/**
 * Contains all Cytoscape Services that we are important for use.
 * We make them globally accessible for ease of use. 
 * @author aidan
 */
public class CytoscapeServices implements SessionLoadedListener, NetworkAboutToBeDestroyedListener, AppsFinishedStartingListener,
										  SetCurrentNetworkListener, SetCurrentNetworkViewListener, 
										  SetCurrentVisualStyleListener, SetCurrentRenderingEngineListener {
	
	private static CyServiceRegistrar serviceRegistrar;
	
	private static CyRootNetworkManager rootNetMgr;
	private static CyNetworkManager netMgr;
	private static CyNetwork currNetwork;
	private static CyNetworkView currNetworkView;

	private static RenderingEngine<CyNetwork> currRenderingEngine;
	private static VisualStyle currVisualStyle;

	private static TaskManager<?, ?> taskMgr;
	private static TunableSetter tunableSetter;
	
	public CytoscapeServices(CyServiceRegistrar serviceRegistrar, CyApplicationManager appMgr, 
			CyRootNetworkManager rootNetMgr, CyNetworkManager netMgr,
			VisualMappingManager visMapMgr, TaskManager<?, ?> taskMgr, TunableSetter tunableSetter) {
		
		CytoscapeServices.serviceRegistrar = serviceRegistrar;
		
		CytoscapeServices.rootNetMgr = rootNetMgr;
		CytoscapeServices.netMgr = netMgr;
		CytoscapeServices.currNetwork = appMgr.getCurrentNetwork();
		CytoscapeServices.currNetworkView = appMgr.getCurrentNetworkView();		

		CytoscapeServices.currVisualStyle = visMapMgr.getCurrentVisualStyle();
		CytoscapeServices.currRenderingEngine = appMgr.getCurrentRenderingEngine();

		CytoscapeServices.taskMgr = taskMgr;
		CytoscapeServices.tunableSetter = tunableSetter;
	}
	
	public static CyServiceRegistrar getServiceRegistrar() {
		return serviceRegistrar;
	}
	
	public static CyRootNetworkManager getRootNetworkManager() {
		return rootNetMgr;
	}
	
	public static CyNetworkManager getNetworkManager() {
		return netMgr;
	}
	
	public static CyNetwork getCurrentNetwork() {
		return currNetwork;
	}
	
	public static CyNetworkView getCurrentNetworkView() {
		return currNetworkView;
	}
	
	public static CyRootNetwork getRootNetwork(CyNetwork network) {
		return rootNetMgr.getRootNetwork(network);
	}
	
	public static RenderingEngine<CyNetwork> getCurrentRenderingEngine() {
		return currRenderingEngine;
	}
	
	public static VisualStyle getCurrentVisualStyle() {
		return currVisualStyle;
	}
	
	public static TaskManager<?, ?> getTaskManager() {
		return taskMgr;
	}

	public static TunableSetter getTunableSetter() {
		return tunableSetter;
	}

	public static boolean hasRootNetwork(String rootNetworkName) {
		for (CyNetwork network : getNetworkManager().getNetworkSet()) {
			CyRootNetwork rootNetwork = getRootNetwork(network);
			if (rootNetworkName.equals(rootNetwork.toString())) {
				return true;
			}
		}
		return false;
	}
	
	public static OraclePainterPanel getOraclePainterPanel() {
		CySwingApplication swingApplication = getServiceRegistrar().getService(CySwingApplication.class);
		CytoPanel panel = swingApplication.getCytoPanel(CytoPanelName.WEST);
		for (int i = 0; i < panel.getCytoPanelComponentCount(); i++) {
			Component c = panel.getComponentAt(i);
			if (c instanceof OraclePainterPanel) {
				return (OraclePainterPanel) c;
			}
		}
		return null;
	}
	
	public static OracleResultsPanel getOracleResultsPanel() {
		CySwingApplication swingApplication = getServiceRegistrar().getService(CySwingApplication.class);
		CytoPanel panel = swingApplication.getCytoPanel(CytoPanelName.EAST);
		for (int i = 0; i < panel.getCytoPanelComponentCount(); i++) {
			Component c = panel.getComponentAt(i);
			if (c instanceof OracleResultsPanel) {
				return (OracleResultsPanel) c;
			}
		}
		return null;
	}
	
	@Override
	public void handleEvent(AppsFinishedStartingEvent e) {
		// Setup the Oracle upon initialisation of the Cytoscape program
		getOracle().unregisterTablesInCytoscape();
		getOracle().createTables();
		getOracle().registerTablesToCytoscape();
	}
	
	@Override	
	public void handleEvent(SessionLoadedEvent e) {
		// This event occurs AFTER THE CURRENT NETWORK CHANGES
		// Setup Oracle components upon initialisation of a new Cytoscape session. These occur when the user has:
		// * Loaded a session (from file) or 
		// * Started a new session

		/***************************************************************************************************/
		
		// Setup the Oracle schema
		// Firstly, unregister all previously used tables in Cytoscape and create new ORACLE TABLES
		getOracle().unregisterTablesInCytoscape();
		getOracle().createTables();

		// Next, iterate through the list of tables in the new Cytoscape session.
		// If we have started a new session, then will be no tables for us to iterate
		// If we have loaded a session (from file), then check if they are OracleTables
		// * Replace/Set any OracleTable in the Oracle with those from the loaded session
		for (CyTableMetadata loadedMetadataTable : e.getLoadedSession().getTables()) {
			CyTable loadedTable = loadedMetadataTable.getTable();
			if (loadedTable.getTitle().equals(getOracle().getPropertyTable().getTableName())) {
				getOracle().getPropertyTable().setTable(loadedTable);
			}
			if (loadedTable.getTitle().equals(getOracle().getKeywordTable().getTableName())) {
				getOracle().getKeywordTable().setTable(loadedTable);
			}
			if (loadedTable.getTitle().equals(getOracle().getNetworkMappingTable().getTableName())) {
				getOracle().getNetworkMappingTable().setTable(loadedTable);
			}
			if (loadedTable.getTitle().equals(getOracle().getNodePropertyTable().getTableName())) {
				getOracle().getNodePropertyTable().setTable(loadedTable);
			}
			if (loadedTable.getTitle().equals(getOracle().getEdgePropertyTable().getTableName())) {
				getOracle().getEdgePropertyTable().setTable(loadedTable);
			}
		}

		// Finally, register all newly created or assigned tables in Cytoscape
		getOracle().registerTablesToCytoscape();

		/***************************************************************************************************/
		
		// After the Oracle schema has been set, we can setup the OraclePainter
		// Clear all previously used palettes and create new COLOR PALETTES based on the OracleTables (from above)
		OraclePainterPanel painterMenu = getOraclePainterPanel();
		painterMenu.getModPainterPanel().clearColorPalette();
		painterMenu.getModPainterPanel().createColorPalette();

		painterMenu.getMultiPainterPanel().clearColorPalette();
		painterMenu.getMultiPainterPanel().createColorPalette();
		
		/***************************************************************************************************/
		
		// After the Oracle schema has been set, we can setup the OracleResults
		// Clear all previously used tables 
		OracleResultsPanel resultsMenu = getOracleResultsPanel();
		resultsMenu.getTabbedPane().removeAll();
	}

	@Override
	public void handleEvent(NetworkAboutToBeDestroyedEvent e) {
		// Update the Oracle components when a network is about to be destroyed. This occurs when the user has:	
		// * Destroyed a network

		// Firstly, set the root network with the network we are destroying 
		CyRootNetwork rootNetwork = CytoscapeServices.getRootNetwork(e.getNetwork());

		// After the network has been set, we can setup the different Oracle components
		// Check whether the network we are destroying is a root network or not 
		if (rootNetwork.getSubNetworkList().size() == 1) {
			// Setup the Oracle schema
			// Remove all mappings in the Oracle associated with that network			
			List<Integer> primaryKeysToRemove = new ArrayList<Integer>();
			for (Integer id : getOracle().getNetworkMappingTable().getTable().getPrimaryKey().getValues(Integer.class)) {
				List<?> compositeKey   = getOracle().getNetworkMappingTable().getCompositeKey(id);
				String rootNetworkName = (String) compositeKey.get(0);
				if (rootNetwork.toString().equals(rootNetworkName)) {
					primaryKeysToRemove.add(id);
				}
			}
			getOracle().getNetworkMappingTable().deleteRows(primaryKeysToRemove);
			
			/***************************************************************************************************/

			// Setup the OracleResults
			// Remove all tabs associated with the network we are destroying
			OracleResultsPanel resultsMenu = getOracleResultsPanel();
			List<Integer> tabsToRemove = new ArrayList<Integer>();
			for (int i = 0; i < resultsMenu.getTabbedPane().getTabCount(); i++) {
				ResultsSummaryPanel summaryPanel = (ResultsSummaryPanel) resultsMenu.getTabbedPane().getComponentAt(i);
				if (summaryPanel.getRootNetwork().toString().equals(rootNetwork.toString())) {
					tabsToRemove.add(i);
				}
			}
			Collections.sort(tabsToRemove);
			Collections.reverse(tabsToRemove);
			for (Integer i : tabsToRemove) {
				resultsMenu.getTabbedPane().remove(i);
			}
		}
	}
	
	@Override
	public void handleEvent(SetCurrentNetworkEvent e) {
		// This event occurs BEFORE A SESSION HAS BEEN LOADED
		// Update the Oracle components whenever the current network changes. These occur when the user has:
		// * Created a new network 
		// * Selected another network, or
		// * Destroyed a network
		
		// Firstly, set the current network with the new network 
		currNetwork = e.getNetwork();

		// After the network has been set, we can setup the different Oracle components
		// Check whether we have changed to a network or not
		if (currNetwork == null) {
			// These are cases where the current network changes from:
			// * Null      -> Null
			// * Network X -> Null
			
			/***************************************************************************************************/
			
			// Setup the OraclePainter
			// Whenever we encounter a Null network:
			// * Show the empty palette
			OraclePainterPanel painterMenu = getOraclePainterPanel();
			painterMenu.getModPainterPanel().showEmptyPanel();
			painterMenu.getMultiPainterPanel().showEmptyPanel();

		} else {
			// These are cases where the current network changes from:
			// * Null      -> Network X 
			// * Network X -> Network Y

			/***************************************************************************************************/
			
			// Setup the OraclePainter
			// Whenever we encounter a new network:
			// * Show the color palette
			OraclePainterPanel painterMenu = getOraclePainterPanel();
			painterMenu.getModPainterPanel().showSelectionPanel();
			painterMenu.getMultiPainterPanel().showSelectionPanel();
		}
	}
		
	@Override
	public void handleEvent(SetCurrentNetworkViewEvent e) {
		currNetworkView = e.getSource().getCurrentNetworkView();
	}
	
	@Override
	public void handleEvent(SetCurrentVisualStyleEvent e) {
		currVisualStyle = e.getVisualStyle();
	}
	
	@Override
	public void handleEvent(SetCurrentRenderingEngineEvent e) {
		currRenderingEngine = e.getRenderingEngine();
	}

}
