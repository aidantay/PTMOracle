package org.cytoscape.PTMOracle.internal.io;

import static org.cytoscape.PTMOracle.internal.schema.impl.Oracle.getOracle;
import static org.cytoscape.PTMOracle.internal.schema.PropertyTable.SEQUENCE;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComboBox;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.cytoscape.PTMOracle.internal.model.Property;
import org.cytoscape.PTMOracle.internal.model.PropertyCollection;
import org.cytoscape.PTMOracle.internal.model.PropertyMap;
import org.cytoscape.PTMOracle.internal.model.property.PropertyCollectionImpl;
import org.cytoscape.PTMOracle.internal.util.swing.TableDisplay;

/**
 * Abstract implementation of a PropertyReader.
 * PropertyReaders are individual readers that parses properties for NODES and EDGES from an input file
 * This currently includes: XML and TSV 
 * @author aidan
 */
public abstract class AbstractPropertyReader implements PropertyReader {
	
	private File file;
	
	private PropertyCollection initialPropertyCollection;
	private PropertyCollection resolvedPropertyCollection;
	private PropertyCollection unresolvedPropertyCollection;
	private Map<Pair<String, String>, List<String>> possibleKeywordMap;	// Map of Type_Description-[PossibleKeywords] pairings

	public AbstractPropertyReader(File file) {
		this.file = file;
		this.initialPropertyCollection = new PropertyCollectionImpl();
		
		this.resolvedPropertyCollection    = new PropertyCollectionImpl();
		this.unresolvedPropertyCollection  = new PropertyCollectionImpl();
		this.possibleKeywordMap            = new HashMap<Pair<String, String>, List<String>>();
	}

	@Override
	public File getFile() {
		return file;
	}
	
	@Override
	public PropertyCollection getInitialPropertyCollection() {
		return initialPropertyCollection;
	}
	
	@Override
	public PropertyCollection getResolvedPropertyCollection() {
		return resolvedPropertyCollection;
	}

	@Override
	public PropertyCollection getUnresolvedPropertyCollection() {
		return unresolvedPropertyCollection;
	}
	
	@Override
	public Map<Pair<String, String>, List<String>> getPossibleKeywordMap() {
		return possibleKeywordMap;
	}

	@Override
	public void cancel() {
		
	}
	
	public void convertDescriptionsToKeyword() {
		
		for (String accession : getInitialPropertyCollection().getAllIds()) {
			PropertyMap propertyMap = getInitialPropertyCollection().getPropertyMap(accession); 

			for (String type : propertyMap.getAllPropertyTypes()) {
				Set<Property> propertySet = propertyMap.getPropertiesByType(type);
				
				for (Property p : propertySet) {
					if (p.getType().equals(SEQUENCE)) {
						getResolvedPropertyCollection().addProperty(accession, p);
						
					} else {
						List<String> possibleKeywords = getOracle().getKeywordTable().convertDescriptionToKeyword(p.getDescription(), type);
	
						if (possibleKeywords.size() == 1) {
							p.setDescription(possibleKeywords.get(0));
							getResolvedPropertyCollection().addProperty(accession, p);
							
						} else if (possibleKeywords.size() > 1) {
							Pair<String, String> typeDescriptionPair = new ImmutablePair<String, String>(p.getType(), p.getDescription());
							getUnresolvedPropertyCollection().addProperty(accession, p);
							getPossibleKeywordMap().put(typeDescriptionPair, possibleKeywords);
						}
					}
				}
			}
		}		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setUnresolvedProperties(TableDisplay tableDisplay) {
		Vector tableData = tableDisplay.getModel().getDataVector();
		for (Object row : tableData) {
			Vector rowData = (Vector) row;
			
			String type           = (String) rowData.get(1);
			String description    = (String) rowData.get(2);
			String newDescription = (String) ((JComboBox<String>) rowData.get(3)).getSelectedItem();

			for (String accession : unresolvedPropertyCollection.getAllIds()) {
				PropertyMap propertyMap = unresolvedPropertyCollection.getPropertyMap(accession);
				for (Property p : propertyMap.getPropertiesByType(type)) {
					if (p.getDescription().equals(description)) {
						p.setDescription(newDescription);
						resolvedPropertyCollection.addProperty(accession, p);
					}
				}
			}
		}
	}	
}
