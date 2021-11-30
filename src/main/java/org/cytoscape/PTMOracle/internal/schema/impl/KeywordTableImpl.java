package org.cytoscape.PTMOracle.internal.schema.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.openmbean.KeyAlreadyExistsException;

import org.cytoscape.PTMOracle.internal.schema.AbstractOracleTable;
import org.cytoscape.PTMOracle.internal.schema.KeywordTable;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableFactory;

import static org.cytoscape.PTMOracle.internal.schema.PropertyTable.SEQUENCE;
import static org.cytoscape.PTMOracle.internal.schema.PropertyTable.PTM;
import static org.cytoscape.PTMOracle.internal.schema.PropertyTable.MOTIF;
import static org.cytoscape.PTMOracle.internal.schema.PropertyTable.DOMAIN;
import static org.cytoscape.PTMOracle.internal.schema.PropertyTable.DISORDER;

/**
 * Table of keyword-regex pairings. 
 * Users can add, remove or modify pairings if necessary.
 * These pairings will map property descriptions to a common keyword based on the regex.
 * Table stores: 
 *  - ID (Primary Key)
 *  - Common keyword [eg. Phosphorylation] (Composite Key)
 *  - Property type [eg. PTM] (Composite Key)
 *  - Colour representation of the keyword used by OraclePainter (only for PTMs).
 *  - Regular expression that matches a property description
 * @author aidan
 */
public class KeywordTableImpl extends AbstractOracleTable implements KeywordTable {

	public KeywordTableImpl(CyTableFactory tableFactory) {
		super();
		
		CyTable table = tableFactory.createTable(getTableName(), ID, Integer.class, false, true); 
		table.createColumn(KEYWORD, String.class, false);
		table.createColumn(TYPE, String.class, false);
		table.createColumn(COLOR, String.class, false);		
		table.createColumn(REGEX, String.class, false);
		setTable(table);
		setColumnNames(Arrays.asList(ID, KEYWORD, TYPE, COLOR, REGEX));
		
		insertRow(Arrays.asList(SEQUENCE,               SEQUENCE, NOT_APPLICABLE, ANY_REGEX));
		insertRow(Arrays.asList(PHOSPHORYLATION,        PTM,      "#B2182B",      PHOSPHORYLATION_REGEX));
		insertRow(Arrays.asList(ACETYLATION,            PTM,      "#2166AC",      ACETYLATION_REGEX));
		insertRow(Arrays.asList(METHYLATION,            PTM,      "#525252",      METHYLATION_REGEX));
		insertRow(Arrays.asList(LIPIDATION,             PTM,      "#C51B7D",      LIPIDATION_REGEX));
		insertRow(Arrays.asList(N_LINKED_GLYCOSYLATION, PTM,      "#BF812D",      N_LINKED_GLYCOSYLATION_REGEX));
		insertRow(Arrays.asList(O_LINKED_GLYCOSYLATION, PTM,      "#35978F",      O_LINKED_GLYCOSYLATION_REGEX));
		insertRow(Arrays.asList(UBIQUITINATION,         PTM,      "#1B7837",      UBIQUITINATION_REGEX));
		insertRow(Arrays.asList(SUMOYLATION,            PTM,      "#762A83",      SUMOYLATION_REGEX));
		insertRow(Arrays.asList(MOTIF,                  MOTIF,    NOT_APPLICABLE, ANY_REGEX));
		insertRow(Arrays.asList(DOMAIN,                 DOMAIN,   NOT_APPLICABLE, ANY_REGEX));
		insertRow(Arrays.asList(DISORDER,               DISORDER, NOT_APPLICABLE, ANY_REGEX));
//		insertRow(Arrays.asList(ESTER,                  PTM,      NOT_APPLICABLE, ESTER_REGEX));
//		insertRow(Arrays.asList(DDI,                    DDI,      NOT_APPLICABLE, ANY_REGEX));
//		insertRow(Arrays.asList(DMI,                    DMI,      NOT_APPLICABLE, ANY_REGEX));
//		insertRow(Arrays.asList(INT_RES,                INT_RES,  NOT_APPLICABLE, ANY_REGEX));
	}
	
	@Override
	public String getTableName() {
		return "Keywords";
	}

	@Override
	public void insertRow(List<?> rowValues)  {
		if (rowValues.size() + 1 != getTable().getColumns().size()) {
			throw new IllegalArgumentException("Unable to insert row. Invalid number of columns\t" + getTableName());
		}

		String keyword    = (String)  rowValues.get(0);
		String type       = (String)  rowValues.get(1);		
		String color      = (String)  rowValues.get(2);
		String regex      = (String)  rowValues.get(3);
		int id = getLastRowIndex() + 1;

		if (hasRow(Arrays.asList(keyword, type))) {
			throw new KeyAlreadyExistsException("Unable to insert row. Primary key exists\t" + getTableName());
		}
		
		CyRow row = getTable().getRow(id);
		row.set(KEYWORD, keyword);
		row.set(TYPE, type);
		row.set(COLOR, color);
		row.set(REGEX, regex);
	}
	
	@Override
	public boolean hasRow(Object primaryKey) {
		List<?> compositeKey = (List<?>) primaryKey;
		String keyword = (String) compositeKey.get(0);
		String type    = (String) compositeKey.get(1);
		
		Collection<CyRow> rows = getTable().getMatchingRows(KEYWORD, keyword);
		for (CyRow row : rows) {
			if (row.get(TYPE, String.class).equals(type)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public CyRow getRow(Object primaryKey) {
		List<?> compositeKey = (List<?>) primaryKey;
		String keyword = (String) compositeKey.get(0);
		String type    = (String) compositeKey.get(1);

		Collection<CyRow> rows = getTable().getMatchingRows(KEYWORD, keyword);
		for (CyRow row : rows) {
			if (row.get(TYPE, String.class).equals(type)) {
				return row;
			}
		}
		return null;
	}
	
	@Override
	public List<String> convertDescriptionToKeyword(String description, String type) {
		List<String> assignedKeywords = new ArrayList<String>();
		Collection<CyRow> rows = getTable().getMatchingRows(TYPE, type);

		for (CyRow row : rows) {
			String keyword = (String) row.getRaw(KEYWORD); 
			String regex   = (String) row.getRaw(REGEX);
			Pattern p = Pattern.compile(regex);
			Matcher matcher = p.matcher(description);
			if (matcher.find()) {
				assignedKeywords.add(keyword);
			}
		}
		return assignedKeywords;
	}
	
	@Override
	public List<?> getCompositeKey(Integer id) {
		String keyword = getTable().getRow(id).get(KEYWORD, String.class);
		String type    = getTable().getRow(id).get(TYPE, String.class);
		return Arrays.asList(keyword, type);		
	}
	
	@Override
	public Object getColor(List<?> compositeKey) {
		try {
			return Color.decode(getRow(compositeKey).get(COLOR, String.class));
			
		} catch(NumberFormatException e) {
			return (String) getRow(compositeKey).get(COLOR, String.class);
		}
	}
	
	@Override
	public String getRegex(List<?> compositeKey) {
		return (String) getRow(compositeKey).get(REGEX, String.class);
	}

}