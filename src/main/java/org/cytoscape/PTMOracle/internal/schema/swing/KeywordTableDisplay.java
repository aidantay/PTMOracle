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
 * TableDisplay for showing the accepted keyword-regex pairings in the Oracle. 
 * @author aidan
 */
public class KeywordTableDisplay extends TableDisplay {

	private static final long serialVersionUID = 7929040140234218238L;

	public KeywordTableDisplay() {
		super();
		
		setColumnRenderer(new ColorButtonRenderer(), 3);

		setMinColumnsWidth(0); // Override the original width
		setRowValues();
		
		removeColumn(getColumnModel().getColumn(0));
	}

	@Override
	public Vector<String> getColumnValues() {
		Vector<String> columnValues = new Vector<String>(getOracle().getKeywordTable().getColumnNames());	
		return columnValues;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setRowValues() {
		for (Object primaryKey : getOracle().getKeywordTable().getPrimaryKeys()) {
			Integer id = (Integer) primaryKey;
			List<?> compositeKey = getOracle().getKeywordTable().getCompositeKey(id);
			String keyword       = (String) compositeKey.get(0);
			String type          = (String) compositeKey.get(1);			

			Vector row = new Vector();
			row.add(id);
			row.add(keyword);
			row.add(type);
			row.add(getOracle().getKeywordTable().getColor(compositeKey));
			row.add(getOracle().getKeywordTable().getRegex(compositeKey));
			getModel().addRow(row);
		}
	}
	
	@Override
	public TableRowSorter<TableDisplayModel> getTableSorter() {
		TableRowSorter<TableDisplayModel> sorter = new TableRowSorter<TableDisplayModel>(getModel());
		sorter.setComparator(0, new TableColumnStringComparator());			
		sorter.setComparator(1, new TableColumnStringComparator());			
		sorter.setComparator(2, new TableColumnStringComparator());			
		return sorter;
	}
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateOracle() {
		List<Integer> prevAttributeKeys = new ArrayList(getOracle().getKeywordTable().getPrimaryKeys());
		getOracle().getKeywordTable().deleteRows(prevAttributeKeys);
		
		Vector updatedRows = (Vector) getModel().getDataVector();
		for (Object o : updatedRows) {
			Vector row = (Vector) o;
			String keyword    = (String) row.get(1);
			String type       = (String) row.get(2);
			String color      = (row.get(3) instanceof Color) ? "#".concat(Integer.toHexString(((Color) row.get(3)).getRGB()).substring(2).toUpperCase()) : (String) row.get(3);
			String regex      = (String) row.get(4);			
			
			List newAttributes = Arrays.asList(keyword, type, color, regex);
			getOracle().getKeywordTable().insertRow(newAttributes);
		}
	}

}