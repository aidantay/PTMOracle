package org.cytoscape.PTMOracle.internal;

import java.util.Properties;

import org.cytoscape.PTMOracle.internal.examples.human.HumanExperimentalBinaryInteractomeTaskFactory;
import org.cytoscape.PTMOracle.internal.examples.human.HumanExperimentalCocomplexInteractomeTaskFactory;
import org.cytoscape.PTMOracle.internal.examples.human.HumanLiteratureInteractomeTaskFactory;
import org.cytoscape.PTMOracle.internal.examples.yeast.YeastInteractomeTaskFactory;
import org.cytoscape.PTMOracle.internal.examples.yeast.YeastMethylproteomeTaskFactory;
import org.cytoscape.PTMOracle.internal.painter.swing.OraclePainterPanel;
import org.cytoscape.PTMOracle.internal.preferences.swing.PreferencesPanel;
import org.cytoscape.PTMOracle.internal.preferences.tasks.PreferencesTaskFactory;
import org.cytoscape.PTMOracle.internal.results.swing.OracleResultsPanel;
import org.cytoscape.PTMOracle.internal.schema.impl.Oracle;
import org.cytoscape.PTMOracle.internal.schema.swing.ExportPropertiesPanel;
import org.cytoscape.PTMOracle.internal.schema.swing.ImportPropertiesPanel;
import org.cytoscape.PTMOracle.internal.schema.tasks.ReadPropertiesTaskFactory;
import org.cytoscape.PTMOracle.internal.schema.tasks.WritePropertiesTaskFactory;
import org.cytoscape.PTMOracle.internal.tools.swing.CalculatorPanel;
import org.cytoscape.PTMOracle.internal.tools.swing.MotifFinderPanel;
import org.cytoscape.PTMOracle.internal.tools.swing.PairFinderPanel;
import org.cytoscape.PTMOracle.internal.tools.swing.RegionFinderPanel;
import org.cytoscape.PTMOracle.internal.tools.tasks.CalculatorTaskFactory;
import org.cytoscape.PTMOracle.internal.tools.tasks.MotifFinderTaskFactory;
import org.cytoscape.PTMOracle.internal.tools.tasks.PairFinderTaskFactory;
import org.cytoscape.PTMOracle.internal.tools.tasks.RegionFinderTaskFactory;
import org.cytoscape.PTMOracle.internal.util.CytoscapeServices;
import org.cytoscape.PTMOracle.internal.util.swing.SwingHandler;
import org.cytoscape.PTMOracle.internal.util.swing.impl.ConvertPropertyPanel;
import org.cytoscape.PTMOracle.internal.util.tasks.ConvertFileTaskFactory;
import org.cytoscape.app.event.AppsFinishedStartingListener;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.events.SetCurrentNetworkListener;
import org.cytoscape.application.events.SetCurrentNetworkViewListener;
import org.cytoscape.application.events.SetCurrentRenderingEngineListener;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.model.events.NetworkAboutToBeDestroyedListener;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.session.events.SessionLoadedListener;
import org.cytoscape.task.NetworkTaskFactory;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.events.SetCurrentVisualStyleListener;
import org.cytoscape.work.ServiceProperties;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskManager;
import org.cytoscape.work.TunableSetter;
import org.cytoscape.work.swing.GUITunableHandlerFactory;
import org.cytoscape.work.swing.SimpleGUITunableHandlerFactory;
import org.osgi.framework.BundleContext;

public class CyActivator extends AbstractCyActivator {
	
	// Cytoscape menu names
	private static final String ORACLE_MENU   = "Apps.PTMOracle";
	private static final String EXAMPLES_MENU = "Apps.PTMOracle.Examples";
	private static final String QUERY_MENU    = "Apps.PTMOracle.OracleTools";

	// Cytoscape Services
	private CyServiceRegistrar cyServiceRegistrarServiceRef;
	private CyNetworkManager cyNetworkManagerServiceRef;
	private CyNetworkNaming cyNetworkNamingServiceRef;
	private CyNetworkFactory cyNetworkFactoryServiceRef;
	private CyNetworkViewManager cyNetworkViewManagerServiceRef;
	private CyNetworkViewFactory cyNetworkViewFactoryServiceRef;
	private CyApplicationManager cyApplicationManagerServiceRef;
	private CyRootNetworkManager cyRootNetworkManagerServiceRef;
	private CyTableManager cyTableManagerServiceRef;
	private CyTableFactory cyTableFactoryServiceRef;
	private VisualMappingManager visualMappingManagerServiceRef;
	private TaskManager<?, ?> taskManagerServiceRef;
	private TunableSetter tunableSetterServiceRef;
	
	// User-defined services
	@SuppressWarnings("unused")
	private Oracle oracle;
	private CytoscapeServices cytoscapeSerivces;
	
	@Override
	public void start(BundleContext context) throws Exception {
		// Register CYTOSCAPE services
		registerCoreServices(context);

		// Register any services or objects that WE define ourselves.
		registerApplicationServices(context);

		// Register the example network tasks
		registerNetworkExamples(context);
		
		// Register all stuff for sub-menus
		registerSubMenus(context);
		
		// Register OraclePainter
		registerOraclePainterPanel(context);
		
		// Register OracleResults
		registerOracleResultsPanel(context);

		// Register all OracleTools
		registerOracleTools(context);
	}
	
	// Register CYTOSCAPE services
	// Any CYTOSCAPE services that we need should be registered here
	// They should also be added into the CytoscapeServiceManager
	private void registerCoreServices(BundleContext context) {
		cyServiceRegistrarServiceRef = getService(context, CyServiceRegistrar.class);
		
		// Cytoscape network services
		cyNetworkManagerServiceRef = getService(context, CyNetworkManager.class);
		cyNetworkNamingServiceRef = getService(context, CyNetworkNaming.class);
		cyNetworkFactoryServiceRef = getService(context, CyNetworkFactory.class);

		cyRootNetworkManagerServiceRef = getService(context, CyRootNetworkManager.class);
		
		// Cytoscape network view services
		cyNetworkViewManagerServiceRef = getService(context, CyNetworkViewManager.class);
		cyNetworkViewFactoryServiceRef = getService(context, CyNetworkViewFactory.class);
		cyApplicationManagerServiceRef = getService(context, CyApplicationManager.class);
		
		// Cytoscape table services
		cyTableManagerServiceRef = getService(context, CyTableManager.class);
		cyTableFactoryServiceRef = getService(context, CyTableFactory.class);

		// Cytoscape visual mapping services
		visualMappingManagerServiceRef = getService(context, VisualMappingManager.class);

		// Cytoscape task services
		taskManagerServiceRef = getService(context, TaskManager.class);
		tunableSetterServiceRef = getService(context, TunableSetter.class);
	}
	
	// Register any services or objects that WE define ourselves.
	private void registerApplicationServices(BundleContext context) {
		// We can create our own tunable panels and register them here
		// This allows more control over how the actual panel looks
		// AND they can be handled appropriately by the Cytioscape
		registerGUIHandler(context, PreferencesPanel.class);
		registerGUIHandler(context, ImportPropertiesPanel.class);
		registerGUIHandler(context, ExportPropertiesPanel.class);
		registerGUIHandler(context, ConvertPropertyPanel.class);
		registerGUIHandler(context, CalculatorPanel.class);
		registerGUIHandler(context, MotifFinderPanel.class);
		registerGUIHandler(context, RegionFinderPanel.class);
		registerGUIHandler(context, PairFinderPanel.class);
		
		oracle = new Oracle(cyTableManagerServiceRef, cyTableFactoryServiceRef);
		
		cytoscapeSerivces = new CytoscapeServices(cyServiceRegistrarServiceRef, cyApplicationManagerServiceRef, 
			cyRootNetworkManagerServiceRef, cyNetworkManagerServiceRef,
			visualMappingManagerServiceRef, taskManagerServiceRef, tunableSetterServiceRef);
		registerService(context, cytoscapeSerivces, AppsFinishedStartingListener.class, new Properties());
		registerService(context, cytoscapeSerivces, NetworkAboutToBeDestroyedListener.class, new Properties());
		registerService(context, cytoscapeSerivces, SessionLoadedListener.class, new Properties());
		registerService(context, cytoscapeSerivces, SetCurrentNetworkListener.class, new Properties());
		registerService(context, cytoscapeSerivces, SetCurrentNetworkViewListener.class, new Properties());
		registerService(context, cytoscapeSerivces, SetCurrentVisualStyleListener.class, new Properties());
		registerService(context, cytoscapeSerivces, SetCurrentRenderingEngineListener.class, new Properties());
	}

	@SuppressWarnings("rawtypes")
	private void registerGUIHandler(BundleContext context, Class<?> classToMatch) {
		SimpleGUITunableHandlerFactory<SwingHandler> GUIHandler 
			= new SimpleGUITunableHandlerFactory<SwingHandler>(SwingHandler.class, classToMatch);
	
		registerService(context, GUIHandler, GUITunableHandlerFactory.class, new Properties());
	}
	
	// Register the example network tasks
	private void registerNetworkExamples(BundleContext context) {
		// Examples Menu - Yeast PPI Network
		TaskFactory yeastInteractomeTaskFactory 
			= new YeastInteractomeTaskFactory(cyNetworkManagerServiceRef, cyNetworkNamingServiceRef, 
				cyNetworkFactoryServiceRef, cyNetworkViewManagerServiceRef, 
				cyNetworkViewFactoryServiceRef, visualMappingManagerServiceRef); 
		
		registerNetwork(context, yeastInteractomeTaskFactory, 
			"Yeast Interactome", "SBI network from Pang et al. (2012)");
		
//		// Examples Menu - Yeast Methylproteome Network
//		TaskFactory yeastMethylproteomeTaskFactory
//			= new YeastMethylproteomeTaskFactory(cyNetworkManagerServiceRef, cyNetworkNamingServiceRef, 
//				cyNetworkFactoryServiceRef, cyNetworkViewManagerServiceRef, 
//				cyNetworkViewFactoryServiceRef, visualMappingManagerServiceRef); 
//		
//		registerNetwork(context, yeastMethylproteomeTaskFactory, 
//			"Yeast Methylproteome", "");

		// Examples Menu - Human Experimental Cocomplex PPI Network
		TaskFactory humanExperimentalCocomplexInteractomeTaskFactory
			= new HumanExperimentalCocomplexInteractomeTaskFactory(cyNetworkManagerServiceRef, cyNetworkNamingServiceRef, 
				cyNetworkFactoryServiceRef, cyNetworkViewManagerServiceRef, 
				cyNetworkViewFactoryServiceRef, visualMappingManagerServiceRef); 

		registerNetwork(context, humanExperimentalCocomplexInteractomeTaskFactory, 
			"Human Experimental Cocomplex Interactome", "BioPlex 2.0 network from Huttlin et al. (2017)");
		
		// Examples Menu - Human Experimental Binary PPI Network
		TaskFactory humanExperimentalBinaryInteractomeTaskFactory
			= new HumanExperimentalBinaryInteractomeTaskFactory(cyNetworkManagerServiceRef, cyNetworkNamingServiceRef, 
				cyNetworkFactoryServiceRef, cyNetworkViewManagerServiceRef, 
				cyNetworkViewFactoryServiceRef, visualMappingManagerServiceRef); 

		registerNetwork(context, humanExperimentalBinaryInteractomeTaskFactory, 
				"Human Experimental Binary Interactome", "HI-II-14 network from Rolland et al. (2014)");

		// Examples Menu - Human Literature PPI Network
		TaskFactory humanLiteratureInteractomeTaskFactory
			= new HumanLiteratureInteractomeTaskFactory(cyNetworkManagerServiceRef, cyNetworkNamingServiceRef, 
				cyNetworkFactoryServiceRef, cyNetworkViewManagerServiceRef, 
				cyNetworkViewFactoryServiceRef, visualMappingManagerServiceRef); 

		registerNetwork(context, humanLiteratureInteractomeTaskFactory, 
			"Human Literature Interactome", "Lit-BM-13 network from Rolland et al. (2014)");
	}
	
	private void registerNetwork(BundleContext context, TaskFactory networkTaskFactory, 
		String title, String tooltip) {
		
		Properties properties = new Properties();
		properties.setProperty(ServiceProperties.PREFERRED_MENU, EXAMPLES_MENU);
		properties.setProperty(ServiceProperties.TITLE, title);
		properties.setProperty(ServiceProperties.TOOLTIP, tooltip);
		registerService(context, networkTaskFactory, TaskFactory.class, properties);		
	}

	private void registerOraclePainterPanel(BundleContext context) {
		// Painter Menu - Resources for painting the network
		CytoPanelComponent painterMenu = new OraclePainterPanel(cyApplicationManagerServiceRef.getCurrentNetwork());		
		registerService(context, painterMenu, CytoPanelComponent.class, new Properties());
	}
	
	public void registerOracleResultsPanel(BundleContext context) {
		// Results Menu - Provides a summary of features / summary of modifications on proteins
		CytoPanelComponent resultsMenu = new OracleResultsPanel();
		registerService(context, resultsMenu, CytoPanelComponent.class, new Properties());
	}

	private void registerSubMenus(BundleContext context) {
		Properties properties = new Properties();

		// Preferences Menu - Customisation of oracle
		TaskFactory preferencesTaskFactory = new PreferencesTaskFactory();
		properties.setProperty(ServiceProperties.PREFERRED_MENU, ORACLE_MENU);
		properties.setProperty(ServiceProperties.TITLE, "Preferences");
		properties.setProperty(ServiceProperties.MENU_GRAVITY, "5.0");
		registerService(context, preferencesTaskFactory, TaskFactory.class, properties);
		
		// Import Menu - Import properties from file
		NetworkTaskFactory readOracleFileTaskFactory = new ReadPropertiesTaskFactory(); 
		properties.setProperty(ServiceProperties.PREFERRED_MENU, ORACLE_MENU);
		properties.setProperty(ServiceProperties.TITLE, "Import...");
		properties.setProperty(ServiceProperties.MENU_GRAVITY, "1.0");
		properties.setProperty(ServiceProperties.TOOLTIP, "Import properties in Oracle format ");
		registerService(context, readOracleFileTaskFactory, NetworkTaskFactory.class, properties);

		// Export Menu - Export properties to file
		NetworkTaskFactory writeOraclePropertiesTaskFactory = new WritePropertiesTaskFactory(); 
		properties.setProperty(ServiceProperties.PREFERRED_MENU, ORACLE_MENU);
		properties.setProperty(ServiceProperties.TITLE, "Export...");
		properties.setProperty(ServiceProperties.MENU_GRAVITY, "2.0");
		properties.setProperty(ServiceProperties.TOOLTIP, "Export properties to delimitered file");
		registerService(context, writeOraclePropertiesTaskFactory, NetworkTaskFactory.class, properties);
		
//		// Convert Menu - Convert tab separated file into Oracle format
//		// For internal purposes
//		TaskFactory convertTaskFactory = new ConvertFileTaskFactory(); 
//		properties.setProperty(ServiceProperties.PREFERRED_MENU, ORACLE_MENU);
//		properties.setProperty(ServiceProperties.TITLE, "Convert...");
//		properties.setProperty(ServiceProperties.MENU_GRAVITY, "3.0");
//		properties.setProperty(ServiceProperties.TOOLTIP, "Convert delimitered file to Oracle format");
//		registerService(context, convertTaskFactory, TaskFactory.class, properties);
	}
	
	private void registerOracleTools(BundleContext context) {
		Properties properties = new Properties();

		// Calculator Menu - Calculate property counts
		NetworkTaskFactory calculatorTaskFactory = new CalculatorTaskFactory(); 
		properties.setProperty(ServiceProperties.PREFERRED_MENU, QUERY_MENU);
		properties.setProperty(ServiceProperties.TITLE, "Calculator");
		properties.setProperty(ServiceProperties.MENU_GRAVITY, "1.0");
		properties.setProperty(ServiceProperties.TOOLTIP, "Count properties on proteins");
		registerService(context, calculatorTaskFactory, NetworkTaskFactory.class, properties);

		// MotifFinder Menu - Find motif sequences
		NetworkTaskFactory motifFinderFactory = new MotifFinderTaskFactory(); 
		properties.setProperty(ServiceProperties.PREFERRED_MENU, QUERY_MENU);
		properties.setProperty(ServiceProperties.TITLE, "MotifFinder");
		properties.setProperty(ServiceProperties.MENU_GRAVITY, "2.0");
		properties.setProperty(ServiceProperties.TOOLTIP, "Find motif patterns on proteins");
		registerService(context, motifFinderFactory, NetworkTaskFactory.class, properties);

		// RegionFinder Menu - Find overlapping properties
		NetworkTaskFactory regionFinderFactory = new RegionFinderTaskFactory();
		properties.setProperty(ServiceProperties.PREFERRED_MENU, QUERY_MENU);
		properties.setProperty(ServiceProperties.TITLE, "RegionFinder");
		properties.setProperty(ServiceProperties.MENU_GRAVITY, "3.0");
		properties.setProperty(ServiceProperties.TOOLTIP, "Find regions with PTMs");
		registerService(context, regionFinderFactory, NetworkTaskFactory.class, properties);

		// PairFinder Menu - Find PTMs within a range
		NetworkTaskFactory pairFinderFactory = new PairFinderTaskFactory();
		properties.setProperty(ServiceProperties.PREFERRED_MENU, QUERY_MENU);
		properties.setProperty(ServiceProperties.TITLE, "PairFinder");
		properties.setProperty(ServiceProperties.MENU_GRAVITY, "4.0");
		properties.setProperty(ServiceProperties.TOOLTIP, "Find PTM pairs within a range on proteins");
		registerService(context, pairFinderFactory, NetworkTaskFactory.class, properties);
	}

	
}
