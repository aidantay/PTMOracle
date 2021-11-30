package org.cytoscape.PTMOracle.internal.schema.swing;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;

import java.util.List;
import java.util.Vector;

import javax.swing.table.TableRowSorter;

import org.cytoscape.PTMOracle.internal.model.NodeProperty;
import org.cytoscape.PTMOracle.internal.model.Property;
import org.cytoscape.PTMOracle.internal.model.PropertyMap;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplayModel;
import org.cytoscape.PTMOracle.internal.util.swing.impl.TableColumnNumericComparator;
import org.cytoscape.PTMOracle.internal.util.swing.impl.TableColumnStringComparator;

/**
 * TableDisplay for showing node properties in the Oracle.
 * @author aidan
 */
public class NodePropertyTableDisplay extends TableDisplay {

	private static final long serialVersionUID = 7929040140234218238L;

	public NodePropertyTableDisplay() {
		super();

		setMinColumnsWidth(0); // Override the original width
		setRowValues();
	}
	
	public NodePropertyTableDisplay(String sharedNodeName, String rootNetworkName) {
		super();
		
		setMinColumnsWidth(0); // Override the original width
		setRowValuesForNode(sharedNodeName, rootNetworkName);

		removeColumn(getColumnModel().getColumn(2));
		removeColumn(getColumnModel().getColumn(1));
		removeColumn(getColumnModel().getColumn(0));
	}

	@Override
	public Vector<String> getColumnValues() {
		Vector<String> columnValues = new Vector<String>(getOracle().getNodePropertyTable().getColumnNames());	
		return columnValues;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setRowValues() {
		for (Object primaryKey : getOracle().getNodePropertyTable().getPrimaryKeys()) {
			Integer id = (Integer) primaryKey;
			List<?> compositeKey = getOracle().getNodePropertyTable().getCompositeKey(id);
			String source             = (String) compositeKey.get(0);
			String sharedNodeName     = (String) compositeKey.get(1);
			NodeProperty nodeProperty = (NodeProperty) compositeKey.get(2);

			Vector row = new Vector();
			row.add(id);
			row.add(source);
			row.add(sharedNodeName);
			row.add(nodeProperty.getType());
			row.add(nodeProperty.getDescription());
			row.add(nodeProperty.getStartPosition());
			row.add(nodeProperty.getEndPosition());
			row.add(nodeProperty.getResidue());
			row.add(nodeProperty.getStatus());
			row.add(nodeProperty.getComments());
			getModel().addRow(row);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setRowValuesForNode(String sharedNodeName, String rootNetworkName) {
		PropertyMap uniquePropertyMap = getOracle().getUniqueProperties(sharedNodeName, rootNetworkName, true);
		for (String type : uniquePropertyMap.getAllPropertyTypes()) {
			for (Property property : uniquePropertyMap.getPropertiesByType(type)) {
				NodeProperty nodeProperty = (NodeProperty) property;
				Vector row = new Vector();
				row.add(0);
				row.add("");
				row.add(sharedNodeName);
				row.add(nodeProperty.getType());
				row.add(nodeProperty.getDescription());
				row.add(nodeProperty.getStartPosition());
				row.add(nodeProperty.getEndPosition());
				row.add(nodeProperty.getResidue());
				row.add(nodeProperty.getStatus());
				row.add(nodeProperty.getComments());
				getModel().addRow(row);
			}
		}
	}
	
	@Override
	public TableRowSorter<TableDisplayModel> getTableSorter() {
		TableRowSorter<TableDisplayModel> sorter = new TableRowSorter<TableDisplayModel>(getModel());
		sorter.setComparator(0, new TableColumnNumericComparator());			
		sorter.setComparator(1, new TableColumnStringComparator());			
		sorter.setComparator(2, new TableColumnStringComparator());			
		sorter.setComparator(3, new TableColumnStringComparator());			
		sorter.setComparator(4, new TableColumnStringComparator());			
		sorter.setComparator(5, new TableColumnNumericComparator());			
		sorter.setComparator(6, new TableColumnNumericComparator());			
		sorter.setComparator(7, new TableColumnStringComparator());			
		sorter.setComparator(8, new TableColumnStringComparator());		
		sorter.setComparator(9, new TableColumnStringComparator());		
		return sorter;
	}

	
}