package org.cytoscape.PTMOracle.internal.examples;

import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskIterator;

/**
 * Abstract implementation of the task factory used to create an ExampleNetwork.
 * @author aidan
 */
public abstract class AbstractExampleNetworkTaskFactory extends AbstractTaskFactory {

	private final CyNetworkManager netMgr;
	private final CyNetworkFactory cnf;
	private final CyNetworkNaming namingUtil;
	private final CyNetworkViewManager viewMgr;
	private final CyNetworkViewFactory cnvf;
	private final VisualMappingManager visMapMgr;
	
	public AbstractExampleNetworkTaskFactory(CyNetworkManager netMgr,
			CyNetworkNaming namingUtil, CyNetworkFactory cnf,
			CyNetworkViewManager viewMgr, CyNetworkViewFactory cnvf,
			VisualMappingManager visMapMgr) {

		this.netMgr = netMgr;
		this.namingUtil = namingUtil;
		this.cnf = cnf;
		this.viewMgr = viewMgr;
		this.cnvf = cnvf;
		this.visMapMgr = visMapMgr;
	}
	
	public CyNetworkManager getNetMgr() {
		return netMgr;
	}

	public CyNetworkFactory getCnf() {
		return cnf;
	}

	public CyNetworkNaming getNamingUtil() {
		return namingUtil;
	}

	public CyNetworkViewManager getViewMgr() {
		return viewMgr;
	}

	public CyNetworkViewFactory getCnvf() {
		return cnvf;
	}

	public VisualMappingManager getVisMapMgr() {
		return visMapMgr;
	}

	abstract public Task createTask(ExampleNetwork network);
	abstract public ExampleNetwork createExampleNetwork();
	
	public TaskIterator createTaskIterator() {
		ExampleNetwork exampleNetwork = createExampleNetwork();
		Task task = createTask(exampleNetwork);
		return new TaskIterator(task);
	}

}

