package org.cytoscape.PTMOracle.internal.schema.swing;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComboBox;

import org.apache.commons.lang3.tuple.Pair;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.impl.ComboBoxEditor;
import org.cytoscape.PTMOracle.internal.util.swing.impl.ComboBoxRenderer;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;

/**
 * TableDisplay for users to manually inspect multiple keyword mappings.
 * TODO Option to NOT import an description that has multiple keyword mappings
 * @author aidan
 */
public class PropertyCheckTableDisplay extends TableDisplay {

	private static final long serialVersionUID = 7929040140234218238L;
	
	private Map<Pair<String, String>, List<String>> possibleKeywordMap;

	public PropertyCheckTableDisplay(Map<Pair<String, String>, List<String>> possibleKeywordMap) {
		super();	
		
		this.possibleKeywordMap = possibleKeywordMap;

		setColumnEditor(new ComboBoxEditor(), 3);
		setColumnRenderer(new ComboBoxRenderer(), 3);
		
		setColumnEditability(true, 3);
		setMinColumnsWidth(5);
		setRowValues();
	}

	@Override
	public Vector<String> getColumnValues() {
		Vector<String> columnValues = new Vector<String>();
		columnValues.add("Edge or Node");
		columnValues.add("Type");
		columnValues.add("Description");
		columnValues.add("Possible Mappings");
		return columnValues;
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setRowValues() {
		for (Pair<String, String> typeDescriptionPair : possibleKeywordMap.keySet()) {
			List<String> possibleKeywords = possibleKeywordMap.get(typeDescriptionPair);
			String type        = typeDescriptionPair.getLeft();
			String description = typeDescriptionPair.getRight();
			boolean isNode     = getOracle().getPropertyTable().getNodePropertyFlag(type);
			JComboBox<String> possibleKeywordsComboBox = new JComboBox<String>(possibleKeywords.toArray(new String[possibleKeywords.size()]));
			
			Vector row = new Vector();			
			if (isNode) {
				row.add("Node");
			} else {
				row.add("Edge");
			}
			row.add(type);
			row.add(description);
			row.add(possibleKeywordsComboBox);
			getModel().addRow(row);
		}
	}

}