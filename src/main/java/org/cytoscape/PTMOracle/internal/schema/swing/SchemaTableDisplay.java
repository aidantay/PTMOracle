package org.cytoscape.PTMOracle.internal.schema.swing;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;

import java.util.Vector;

import org.cytoscape.PTMOracle.internal.schema.OracleTable;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;

/**
 * TableDisplay for showing the oracle schema.
 * @author aidan
 */
public class SchemaTableDisplay extends TableDisplay {

	private static final long serialVersionUID = 7929040140234218238L;

	public SchemaTableDisplay() {
		super();
			
		setMinColumnsWidth(0); // Override the original width
		setRowValues();
	}

	@Override
	public Vector<String> getColumnValues() {
		Vector<String> columnValues = new Vector<String>();
		columnValues.add("TableName");
		columnValues.add("SUID");
		columnValues.add("ColumnNames");
		return columnValues;
	}
	
	@Override
	public void setRowValues() {
		for (OracleTable table : getOracle().getTables()) {
			Vector<String> row = new Vector<String>();
			row.add(String.valueOf(table.getTable().getSUID()));
			row.add(table.getTableName());
			row.add(table.getColumnNames().toString());
			getModel().addRow(row);
		}
	}

}
