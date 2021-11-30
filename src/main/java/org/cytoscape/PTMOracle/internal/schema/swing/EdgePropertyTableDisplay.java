package org.cytoscape.PTMOracle.internal.schema.swing;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;

import java.util.List;
import java.util.Vector;

import javax.swing.table.TableRowSorter;

import org.cytoscape.PTMOracle.internal.model.EdgeProperty;
import org.cytoscape.PTMOracle.internal.model.Property;
import org.cytoscape.PTMOracle.internal.model.PropertyMap;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplayModel;
import org.cytoscape.PTMOracle.internal.util.swing.impl.TableColumnNumericComparator;
import org.cytoscape.PTMOracle.internal.util.swing.impl.TableColumnStringComparator;

/**
 * TableDisplay for showing edge properties in the Oracle. 
 * @author aidan
 */
public class EdgePropertyTableDisplay extends TableDisplay {

	private static final long serialVersionUID = 7929040140234218238L;

	public EdgePropertyTableDisplay() {
		super();

		setMinColumnsWidth(0); // Override the original width
		setRowValues();
	}
	
	public EdgePropertyTableDisplay(String sharedEdgeName, String rootNetworkName) {
		super();
		
		setMinColumnsWidth(0); // Override the original width
		setRowValuesForEdge(sharedEdgeName, rootNetworkName);
		
		removeColumn(getColumnModel().getColumn(3));
		removeColumn(getColumnModel().getColumn(2));
		removeColumn(getColumnModel().getColumn(1));
		removeColumn(getColumnModel().getColumn(0));
	}

	@Override
	public Vector<String> getColumnValues() {
		Vector<String> columnValues = new Vector<String>(getOracle().getEdgePropertyTable().getColumnNames());	
		return columnValues;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setRowValues() {
		for (Object primaryKey : getOracle().getEdgePropertyTable().getPrimaryKeys()) {
			Integer id = (Integer) primaryKey;
			List<?> compositeKey = getOracle().getEdgePropertyTable().getCompositeKey(id);
			String source             = (String) compositeKey.get(0);
			String sharedEdgeName     = (String) compositeKey.get(1);
			EdgeProperty edgeProperty = (EdgeProperty) compositeKey.get(2);

			Vector row = new Vector();
			row.add(id);
			row.add(source);
			row.add(sharedEdgeName);
			row.add(edgeProperty.getType());
			row.add(edgeProperty.getDescription());
			row.add(edgeProperty.getStatus());
			row.add(edgeProperty.getComments());
			getModel().addRow(row);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setRowValuesForEdge(String sharedEdgeName, String rootNetworkName) {
		PropertyMap uniquePropertyMap = getOracle().getUniqueProperties(sharedEdgeName, rootNetworkName, false);
		for (String type : uniquePropertyMap.getAllPropertyTypes()) {
			for (Property property : uniquePropertyMap.getPropertiesByType(type)) {
				EdgeProperty edgeProperty = (EdgeProperty) property;

				Vector row = new Vector();
				row.add(0);
				row.add("");
				row.add(sharedEdgeName);
				row.add(edgeProperty.getType());
				row.add(edgeProperty.getDescription());
				row.add(edgeProperty.getStatus());
				row.add(edgeProperty.getComments());
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
		sorter.setComparator(5, new TableColumnStringComparator());			
		sorter.setComparator(6, new TableColumnStringComparator());			
		return sorter;
	}

}