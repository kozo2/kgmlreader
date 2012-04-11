package org.cytoscape.kegg.browser;

import giny.view.NodeView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.cytoscape.data.reader.kgml.KEGGEntryType;

import cytoscape.CyNode;
import cytoscape.Cytoscape;
import cytoscape.actions.LoadNetworkTask;
import cytoscape.data.CyAttributes;
import cytoscape.logger.CyLogger;
import cytoscape.view.CyNetworkView;
import ding.view.NodeContextMenuListener;

/**
 * Add context menu for KEGG pathways
 */
public class KEGGNodeContextMenuListener implements NodeContextMenuListener {

	// Static URLs for the KEGG web site
	private static final String COMPOUND_URL = "http://www.kegg.jp/Fig/compound/";
	private static final String REACTION_URL = "http://www.kegg.jp/Fig/reaction_small/";
	private static final String MAP_URL = "http://www.genome.jp/kegg/misc/thumbnail/";

	private static final String COMPOUND_DBGET_URL = "http://www.genome.jp/dbget-bin/www_bget?cpd:";
	private static final String REACTION_DBGET_URL = "http://www.genome.jp/dbget-bin/www_bget?";
	private static final String MAP_DBGET_URL = "http://www.genome.jp/dbget-bin/www_bget?pathway+";

	// FTP access is no longer public, and we need a KGML URL from web
	private static final String KGML_LINK_BASE_URL = "http://www.genome.jp/kegg-bin/download?entry=";
	private static final String KGML_FORMAT = "&format=kgml";

	private final CyAttributes nodeAttr = Cytoscape.getNodeAttributes();
	private final NestedNodeExpander expander;

	public KEGGNodeContextMenuListener(CyNetworkView view) {
		this.expander = new NestedNodeExpander(view);
	}

	private URL getLinkToKGML(String mapID) throws MalformedURLException {
		return new URL(KGML_LINK_BASE_URL + mapID + KGML_FORMAT);
	}

	@Override
	public void addNodeContextMenuItems(NodeView nv, JPopupMenu menu) {
		if (menu == null || nv == null)
			return;

		final String attrValue = nodeAttr.getStringAttribute(nv.getNode().getIdentifier(), "KEGG.entry");
		if (attrValue == null)
			return;

		final KEGGEntryType entryType = KEGGEntryType.getType(attrValue);

		if (entryType == null)
			return;

		final JMenuItem item = new JMenuItem();

		if (entryType.equals(KEGGEntryType.COMPOUND))
			addCompoundMenu(nv, item);
		else if (entryType.equals(KEGGEntryType.MAP))
			addMapLinkMenu(nv, item, menu);
		else if (entryType.equals(KEGGEntryType.GENE) || entryType.equals(KEGGEntryType.ORTHOLOG))
			addGeneMenu(nv, item);
		
		menu.add(item);
	}

	private void addCompoundMenu(final NodeView nv, final JMenuItem menu) {

		final String compoundID = nodeAttr.getStringAttribute(nv.getNode().getIdentifier(), "KEGG.label");

		URL image = null;
		String urlString = null;
		try {
			urlString = COMPOUND_URL + compoundID + ".gif";
			image = new URL(urlString);
		} catch (MalformedURLException e) {
			CyLogger.getLogger().warn("Invalid URL: " + urlString);
		}

		menu.setIcon(new ImageIcon(image));
		menu.setText("Open link to Compound: " + compoundID);
		menu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cytoscape.util.OpenBrowser.openURL(COMPOUND_DBGET_URL + compoundID);
			}
		});
	}

	private void addMapLinkMenu(final NodeView nv, final JMenuItem item, JPopupMenu parentMenu) {
		final JMenu keggMenu = new JMenu("Import New KEGG Pathway");

		final JMenuItem expandNnfItem = new JMenuItem();
		final JMenuItem setNnfItem = new JMenuItem();
		
		keggMenu.add(setNnfItem);
		
		// TODO: implement this function
		//keggMenu.add(expandNnfItem);

		final String mapID = nodeAttr.getStringAttribute(nv.getNode().getIdentifier(), "KEGG.name").split(":")[1];
		final CyNode mapNode = Cytoscape.getCyNode(nv.getNode().getIdentifier());
		URL image = null;

		try {
			image = new URL(MAP_URL + mapID + ".gif");
		} catch (MalformedURLException e2) {
			CyLogger.getLogger().warn("Invalid URL: " + MAP_URL + mapID + ".gif");
		}
		setNnfItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					LoadNetworkTask.loadURL(getLinkToKGML(mapID), true);
					mapNode.setNestedNetwork(Cytoscape.getCurrentNetworkView().getGraphPerspective());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		expandNnfItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				expander.expandNestedNode(mapNode);
			}
		});

		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cytoscape.util.OpenBrowser.openURL(MAP_DBGET_URL + mapID);
			}
		});

		setNnfItem.setText("Import pathway " + mapID + " and set as Nested Network");
		expandNnfItem.setText("Expand pathway here");

		if(image != null)
			item.setIcon(new ImageIcon(image));
		item.setText("Open link to Pathway: " + mapID);

		parentMenu.add(keggMenu);
	}

	private void addGeneMenu(final NodeView nv, final JMenuItem item) {
		final String reactionID = nodeAttr.getStringAttribute(nv.getNode().getIdentifier(), "KEGG.reaction");
		URL image = null;
		final String urlString = REACTION_URL + reactionID.split(":")[1] + ".gif";
		try {
			image = new URL(urlString);
		} catch (MalformedURLException e1) {
			CyLogger.getLogger().warn("Invalid URL: " + urlString);
		}

		item.setIcon(new ImageIcon(image));
		item.setText("Open Link to Reaction: " + reactionID);
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cytoscape.util.OpenBrowser.openURL(REACTION_DBGET_URL + reactionID);
			}
		});
	}
}
