package org.cytoscape.data.reader.kgml;

import org.cytoscape.kegg.browser.KEGGNetworkListener;

import cytoscape.Cytoscape;
import cytoscape.data.ImportHandler;
import cytoscape.plugin.CytoscapePlugin;
import cytoscape.view.CytoscapeDesktop;

/**
 * KGML Reader Main class
 * 
 * @author kono
 *
 */
public class KGMLReaderPlugin extends CytoscapePlugin {
	
	public KGMLReaderPlugin() {
		final ImportHandler importHandler = new ImportHandler();
		importHandler.addFilter(new KGMLFilter());
		
		// Add context menu listeners.
		Cytoscape.getSwingPropertyChangeSupport().addPropertyChangeListener(
				CytoscapeDesktop.NETWORK_VIEW_CREATED, new KEGGNetworkListener());
	}

}
