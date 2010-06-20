package org.cytoscape.kegg.browser;

import java.net.MalformedURLException;
import java.net.URL;

import giny.view.NodeView;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.cytoscape.data.reader.kgml.KEGGEntryType;

import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;
import cytoscape.view.CyNetworkView;

import ding.view.NodeContextMenuListener;

/**
 * Add context menu for KEGG pathways
 * 
 * @author kono
 *
 */
public class KEGGNodeContextMenuListener implements NodeContextMenuListener {
	private static final String COMPOUND_URL = "http://www.kegg.jp/Fig/compound/";
	private static final String REACTION_URL = "http://www.kegg.jp/Fig/reaction_small/";
	private static final String MAP_URL = "http://www.genome.jp/tmp/pathway_thumbnail/";
	
	private CyAttributes nodeAttr = Cytoscape.getNodeAttributes();
	private final CyNetworkView view;

	public KEGGNodeContextMenuListener(CyNetworkView view) {
		this.view = view;
	}

	@Override
	public void addNodeContextMenuItems(NodeView nv, JPopupMenu menu) {
		if (menu == null)
			return;

		final JMenu keggMenu = new JMenu("KEGG Options");
		
		final String attrValue = nodeAttr.getStringAttribute(nv.getNode().getIdentifier(), "KEGG.entry");
		if (attrValue == null) return;
		
		final KEGGEntryType entryType = KEGGEntryType.getType(attrValue);
		
		if (entryType == null) return;
		
		JMenuItem item = new JMenuItem(); 
		
		if(entryType.equals(KEGGEntryType.COMPOUND)) {
			try {
				final String compoundID = nodeAttr.getStringAttribute(nv.getNode().getIdentifier(), "KEGG.label");
				URL image = new URL(COMPOUND_URL + compoundID + ".gif");
				item.setIcon(new ImageIcon(image));
				item.setText("Compound: " + compoundID);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if (entryType.equals(KEGGEntryType.MAP)) {
			final String mapID = nodeAttr.getStringAttribute(nv.getNode().getIdentifier(), "KEGG.name").split(":")[1];
			URL image = null;
			try {
				image = new URL(MAP_URL + mapID + ".png");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			item.setIcon(new ImageIcon(image));
			item.setText("Pathway: " + mapID);
		} else if (entryType.equals(KEGGEntryType.GENE) || entryType.equals(KEGGEntryType.ORTHOLOG)) {
			try {
				String reactionID = nodeAttr.getStringAttribute(nv.getNode().getIdentifier(), "KEGG.reaction");
				URL image = new URL(REACTION_URL + reactionID.split(":")[1] + ".gif");
				item.setIcon(new ImageIcon(image));
				item.setText("Reaction: " + reactionID);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		menu.addSeparator();
		menu.add(keggMenu);
		menu.add(item);
	}
	
	

}
