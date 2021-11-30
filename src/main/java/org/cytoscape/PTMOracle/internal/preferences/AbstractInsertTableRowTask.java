package org.cytoscape.PTMOracle.internal.preferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TunableValidator;

/**
 * Abstract implementation of a task for adding rows into preference tables
 * Preference tables are GUI representations of the tables in the Oracle
 *  
 * @author 
 */
public abstract class AbstractInsertTableRowTask extends AbstractTask implements TunableValidator {
	
	private TableDisplay table;
	
	public AbstractInsertTableRowTask(TableDisplay table) {
		super();
		
		this.table = table;
	}
	
	public TableDisplay getTable() {
		return table;
	}

	// Input validators may put these things into a global Validation class
	public boolean isValidBoolean(String stringValue) {
		Pattern pattern = Pattern.compile("^true$|^false$");
	   	Matcher matcher = pattern.matcher(stringValue.toLowerCase());
	   	if (!matcher.find()) {
			return false;
	   	}
		return true;
	}
	
	public boolean isValidHexCode(String stringValue) {
		Pattern pattern = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
	   	Matcher matcher = pattern.matcher(stringValue.toLowerCase());
	   	if (!matcher.find()) {
			return false;
	   	}
		return true;
	}
	
	public boolean isValidCellString(String stringValue) {
		if (stringValue == null || stringValue.isEmpty()) {
			return false;
		}
		return true;
	}

	
}