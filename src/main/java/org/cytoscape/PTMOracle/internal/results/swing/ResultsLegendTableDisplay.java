package org.cytoscape.PTMOracle.internal.results.swing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.impl.ColorButtonEditor;
import org.cytoscape.PTMOracle.internal.util.swing.impl.ColorButtonRenderer;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;
/**
 * TableDisplay for showing properties and their associated colours.
 * @author aidan
 */
public class ResultsLegendTableDisplay extends TableDisplay {

	private static final long serialVersionUID = 7929040140234218238L;

	private boolean isNodeProperty;

	public ResultsLegendTableDisplay(boolean isNodeProperty) {
		super();
		
		this.isNodeProperty = isNodeProperty;

		setColumnEditor(new ColorButtonEditor(), 1);
		setColumnRenderer(new ColorButtonRenderer(), 1);
		
		setMinColumnsWidth(0); // Override the original width
		setRowValues();
	}

	@Override
	public Vector<String> getColumnValues() {
		Vector<String> columnValues = new Vector<String>();
		columnValues.add("Property");
		columnValues.add("Color");
		return columnValues;
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setRowValues() {
		Map<String, Color> valueMap = new HashMap<String, Color>();
		for (Object primaryKey : getOracle().getPropertyTable().getPrimaryKeys()) {
			String type = (String) primaryKey;
	
			if (isNodeProperty == getOracle().getPropertyTable().getNodePropertyFlag(type) || 
				!isNodeProperty == !getOracle().getPropertyTable().getNodePropertyFlag(type)) {

				valueMap.put(type, getOracle().getPropertyTable().getPropertyColour(type));
			}
		}
		List<String> sortedValues = new ArrayList<String>(valueMap.keySet());
		Collections.sort(sortedValues);
		for (String type : sortedValues) {
			Vector row = new Vector();
			row.add(type);
			row.add(valueMap.get(type));
			getModel().addRow(row);
		}
		
	}

}