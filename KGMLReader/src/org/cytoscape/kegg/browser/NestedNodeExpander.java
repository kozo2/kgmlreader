package org.cytoscape.kegg.browser;

import java.util.Iterator;

import giny.model.GraphPerspective;
import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import cytoscape.view.CyEdgeView;
import cytoscape.view.CyNetworkView;
import cytoscape.view.CyNodeView;

public class NestedNodeExpander {
	
	private final CyNetworkView view;
	
	public NestedNodeExpander(final CyNetworkView view) {
		this.view = view;
	}

	public void expandNestedNode(final CyNode nestedNode) {
		// This cast is OK since current implementation is always CyNetwork.
		final CyNetwork nestedNetwork = (CyNetwork) nestedNode.getNestedNetwork();
		
		final CyNetworkView nestedNetworkView = Cytoscape.getNetworkView(nestedNetwork.getIdentifier());
		
		final Iterator<CyNodeView> nodeViewItr = nestedNetworkView.getNodeViewsIterator();
		final Iterator<CyEdgeView> edgeViewItr = nestedNetworkView.getEdgeViewsIterator();
		
		while(nodeViewItr.hasNext()) {
			final CyNodeView nv = nodeViewItr.next();
			//view.addNodeView(arg0, nv);
		}
		
		
	}
	
}
