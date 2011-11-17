package org.cytoscape.kegg.browser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import cytoscape.Cytoscape;
import cytoscape.view.CyNetworkView;
import cytoscape.view.CytoscapeDesktop;

/**
 * Add context (right-click) menu to the current views
 * 
 * @author kono
 * 
 */
public class KEGGNetworkListener implements PropertyChangeListener {

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (CytoscapeDesktop.NETWORK_VIEW_CREATED.equals(event
				.getPropertyName())) {
			
			final CyNetworkView view = (CyNetworkView) event.getNewValue();

			// Node right-click menu
			final KEGGNodeContextMenuListener nodeMenuListener = new KEGGNodeContextMenuListener(view);
			Cytoscape.getCurrentNetworkView().addNodeContextMenuListener(
					nodeMenuListener);
			
			// Edge right-click menu
			final KEGGEdgeContextMenuListener edgeMenuListener = new KEGGEdgeContextMenuListener(view);
			Cytoscape.getCurrentNetworkView().addEdgeContextMenuListener(
					edgeMenuListener);
		}
	}
}