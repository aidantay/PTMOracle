package org.cytoscape.PTMOracle.internal.io.write;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Vector;

import org.cytoscape.PTMOracle.internal.io.AbstractPropertyWriter;
import org.cytoscape.PTMOracle.internal.io.PropertyWriter;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;
import org.cytoscape.work.TaskMonitor;

/**
 * PropertyTSVWriter composes properties for NODEs and EDGES into a TSV file
 * @author aidan
 */
public class PropertyTSVWriterImpl extends AbstractPropertyWriter implements PropertyWriter {

	private TableDisplay tableDisplay;	
	
	public PropertyTSVWriterImpl(File file, TableDisplay tableDisplay) {
		super(file);
		
		this.tableDisplay = tableDisplay;
	}
	
	public TableDisplay getTableDisplay() {
		return tableDisplay;
	}
	
 	@Override
	@SuppressWarnings("unchecked")
	public void run(TaskMonitor taskMonitor) throws Exception {
 	    try {
	    	Writer fileWriter = new FileWriter(getFile());
			// Write the table to file
			for (Object row : getTableDisplay().getModel().getDataVector()) {
				Vector<Object> rowValues = (Vector<Object>) row;
				Iterator<Object> iterator = rowValues.iterator();
				
				while (iterator.hasNext()) {
					Object value = iterator.next();
					fileWriter.append(value.toString());
					if (iterator.hasNext()) {
						fileWriter.append("\t");
					}
				}
				fileWriter.append(System.getProperty("line.separator"));
			}
	    	fileWriter.close();
	    	
	    } catch (IOException e1) {
			e1.printStackTrace();
		}
 	}

}
