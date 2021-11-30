package org.cytoscape.PTMOracle.internal.util.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cytoscape.PTMOracle.internal.io.read.PropertyTSVReaderImpl;
import org.cytoscape.PTMOracle.internal.util.swing.impl.ConvertPropertyPanel;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ProvidesTitle;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;
import org.cytoscape.work.TunableValidator;

/**
 * Task for writing oracle properties to file. 
 * @author aidan
 */
public class ImportConvertFileTask extends AbstractTask implements TunableValidator {

	@Tunable
	public ConvertPropertyPanel panel;
	
	private File inputFile;
	private List<Integer> indexList;
	
	public ImportConvertFileTask(File inputFile) {
		super();
		
		this.inputFile  = inputFile;
		this.panel      = new ConvertPropertyPanel(inputFile);
		this.indexList  = new ArrayList<Integer>();
	}

	public ConvertPropertyPanel getPanel() {
		return panel;
	}
	
	public File getFile() {
		return inputFile;
	}
	
	public List<Integer> getIndexList() {
		return indexList;
	}

	@ProvidesTitle
	public String getMenuTitle() {
		return "Convert";
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setStatusMessage("Importing properties");

		PropertyTSVReaderImpl reader = new PropertyTSVReaderImpl(getFile());
		reader.run(taskMonitor, getIndexList());
		
		ExportConvertFileTask task = new ExportConvertFileTask(reader.getInitialPropertyCollection());
		insertTasksAfterCurrentTask(task);
	}

	@Override
	public ValidationState getValidationState(Appendable errMsg) {
		Pattern pattern = Pattern.compile("^-?[0-9]+$");
		for (String s : getPanel().getIndexList()) {
		   	Matcher matcher = pattern.matcher(s);
		   	if (!matcher.find()) {
				return ValidationState.INVALID;
				
		   	} else {
		   		getIndexList().add(Integer.valueOf(s));
		   	}
		}
		
		return ValidationState.OK;
	}
}
