package org.cytoscape.PTMOracle.internal.schema.swing;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.table.TableRowSorter;

import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplayModel;
import org.cytoscape.PTMOracle.internal.util.swing.impl.TableColumnStringComparator;

/**
 * TableDisplay for showing the network-source pairings in the Oracle. 
 * @author aidan
 */
public class NetworkMappingTableDisplay extends TableDisplay {

	private static final long serialVersionUID = 7929040140234218238L;
	
	public NetworkMappingTableDisplay() {
		super();
		
		setColumnClass(Integer.class, 0);

		setMinColumnsWidth(0); // Override the original width
		setRowValues();

		removeColumn(getColumnModel().getColumn(0));
	}
	
	@Override
	public Vector<String> getColumnValues() {
		Vector<String> columnValues = new Vector<String>(getOracle().getNetworkMappingTable().getColumnNames());			
		return columnValues;
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setRowValues() {
		for (Integer id : getOracle().getNetworkMappingTable().getTable().getPrimaryKey().getValues(Integer.class)) {
			List<?> compositeKey   = getOracle().getNetworkMappingTable().getCompositeKey(id);
			String rootNetworkName = (String) compositeKey.get(0);
			String source          = (String) compositeKey.get(1);			

			Vector row = new Vector();
			row.add(id);
			row.add(rootNetworkName);
			row.add(source);
			getModel().addRow(row);
		}
	}
	
	@Override
	public TableRowSorter<TableDisplayModel> getTableSorter() {
		TableRowSorter<TableDisplayModel> sorter = new TableRowSorter<TableDisplayModel>(getModel());
		sorter.setComparator(1, new TableColumnStringComparator());
		sorter.setComparator(2, new TableColumnStringComparator());
		return sorter;
	}
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateOracle() {
		List<Integer> prevAttributeKeys = new ArrayList(getOracle().getNetworkMappingTable().getPrimaryKeys());
		getOracle().getNetworkMappingTable().deleteRows(prevAttributeKeys);
		
		Vector updatedRows = (Vector) getModel().getDataVector();
		for (Object o : updatedRows) {
			Vector row = (Vector) o;

			String rootNetworkName = (String) row.get(1);
			String source          = (String) row.get(2);
			
			List newAttributes = Arrays.asList(rootNetworkName, source);
			getOracle().getNetworkMappingTable().insertRow(newAttributes);
		}
		
	}
	
}
