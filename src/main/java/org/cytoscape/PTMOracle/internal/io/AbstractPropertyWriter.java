package org.cytoscape.PTMOracle.internal.io;

import java.io.File;

/**
 * Abstract implementation of a PropertyReader.
 * PropertyReaders are individual readers that parses properties for NODES and EDGES from an input file
 * This currently includes: XML and TSV 
 * @author aidan
 */
public abstract class AbstractPropertyWriter implements PropertyWriter {
	
	private File file;
	
	public AbstractPropertyWriter(File file) {
		this.file = file;
	}
	
	@Override
	public File getFile() {
		return file;
	}

	@Override
	public void cancel() {
		
	}
	
}
