package org.cytoscape.PTMOracle.internal.preferences.tasks;

import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

/**
 * Task factory for the preferences menu. 
 * @author aidan
 */
public class PreferencesTaskFactory extends AbstractTaskFactory {
	
	public PreferencesTaskFactory() {
		super();
	}
	
	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(new PreferencesTask());
	}

}
