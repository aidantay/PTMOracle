package org.cytoscape.PTMOracle.internal.schema.swing;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.table.TableRowSorter;

import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplayModel;
import org.cytoscape.PTMOracle.internal.util.swing.impl.ColorButtonRenderer;
import org.cytoscape.PTMOracle.internal.util.swing.impl.TableColumnStringComparator;

/**
 * TableDisplay for showing the accepted properties in the Oracle. 
 * @author aidan
 */
public class PropertyTableDisplay extends TableDisplay {

	private static final long serialVersionUID = 7929040140234218238L;

	public PropertyTableDisplay() {
		super();
			
		setColumnRenderer(new ColorButtonRenderer(), 2);
		setColumnClass(Boolean.class, 3);
		setColumnClass(Boolean.class, 4);
		setColumnClass(Boolean.class, 5);
		setColumnClass(Boolean.class, 6);
		setColumnClass(Boolean.class, 7);

		setMinColumnsWidth(0); // Override the original width
		setRowValues();
	}

	@Override
	public Vector<String> getColumnValues() {
		Vector<String> columnValues = new Vector<String>(getOracle().getPropertyTable().getColumnNames());			
		return columnValues;
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setRowValues() {
		for (Object primaryKey : getOracle().getPropertyTable().getPrimaryKeys()) {
			String type = (String) primaryKey;
			Vector row = new Vector();
			row.add(type);
			row.add(getOracle().getPropertyTable().getPropertyColumnName(type));
			row.add(getOracle().getPropertyTable().getPropertyColour(type));
			row.add(getOracle().getPropertyTable().getNodePropertyFlag(type));
			row.add(getOracle().getPropertyTable().getIntervalFlag(type));
			row.add(getOracle().getPropertyTable().getMergeableFlag(type));
			row.add(getOracle().getPropertyTable().getListFlag(type));
			row.add(getOracle().getPropertyTable().getDefaultFlag(type));
			getModel().addRow(row);
		}
	}
	
	@Override
	public TableRowSorter<TableDisplayModel> getTableSorter() {
		TableRowSorter<TableDisplayModel> sorter = new TableRowSorter<TableDisplayModel>(getModel());
		sorter.setComparator(0, new TableColumnStringComparator());
		sorter.setComparator(1, new TableColumnStringComparator());
		return sorter;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateOracle() {
		List<String> prevAttributeKeys = new ArrayList(getOracle().getPropertyTable().getPrimaryKeys());
		getOracle().getPropertyTable().deleteRows(prevAttributeKeys);
		
		Vector updatedRows = (Vector) getModel().getDataVector();		
		for (Object o : updatedRows) {
			Vector row = (Vector) o;
			String type            = (String)  row.get(0);
			String columnName      = (String)  row.get(1);
			String color           = "#".concat(Integer.toHexString(((Color) row.get(2)).getRGB()).substring(2).toUpperCase());	// We only want 6 digit hex, not 8
			Boolean isNodeProperty = (Boolean) row.get(3);
			Boolean isInterval     = (Boolean) row.get(4);
			Boolean isMergable     = (Boolean) row.get(5);
			Boolean isList         = (Boolean) row.get(6);
			Boolean isDefault      = (Boolean) row.get(7);
			
			List newAttributes = Arrays.asList(type, columnName, color, isNodeProperty, isInterval, isMergable, isList, isDefault);
			getOracle().getPropertyTable().insertRow(newAttributes);
		}
	}

}
