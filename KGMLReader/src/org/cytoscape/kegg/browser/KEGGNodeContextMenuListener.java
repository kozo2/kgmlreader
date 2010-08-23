package org.cytoscape.kegg.browser;

import giny.view.NodeView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.cytoscape.data.reader.kgml.KEGGEntryType;

import cytoscape.CyNode;
import cytoscape.Cytoscape;
import cytoscape.actions.LoadNetworkTask;
import cytoscape.data.CyAttributes;
import cytoscape.view.CyNetworkView;
import ding.view.NodeContextMenuListener;

/**
 * Add context menu for KEGG pathways
 * 
 * @author kono
 * @author Kozo.Nishida
 * 
 */
public class KEGGNodeContextMenuListener implements NodeContextMenuListener {
	private static final String COMPOUND_URL = "http://www.kegg.jp/Fig/compound/";
	private static final String REACTION_URL = "http://www.kegg.jp/Fig/reaction_small/";
	private static final String MAP_URL = "http://www.genome.jp/tmp/pathway_thumbnail/";

	private static final String COMPOUND_DBGET_URL = "http://www.genome.jp/dbget-bin/www_bget?cpd:";
	private static final String REACTION_DBGET_URL = "http://www.genome.jp/dbget-bin/www_bget?";
	private static final String MAP_DBGET_URL = "http://www.genome.jp/dbget-bin/www_bget?pathway+";
	
	private static final String KGML_URL = "ftp://ftp.genome.jp/pub/kegg/xml/kgml/metabolic/";

	private CyAttributes nodeAttr = Cytoscape.getNodeAttributes();
	private final CyNetworkView view;
	
	private final NestedNodeExpander expander;
	
	

	public KEGGNodeContextMenuListener(CyNetworkView view) {
		this.view = view;
		this.expander = new NestedNodeExpander(view);
	}
	
	private URL convertToFTP(String mapID) throws MalformedURLException {
		Pattern ecPattern = Pattern.compile("ec[0-9]{5}");
		Matcher ecMatcher = ecPattern.matcher(mapID);
		Pattern koPattern = Pattern.compile("ko[0-9]{5}");
		Matcher koMatcher = koPattern.matcher(mapID);
		Pattern rnPattern = Pattern.compile("rn[0-9]{5}");
		Matcher rnMatcher = rnPattern.matcher(mapID);
				
		if (ecMatcher.find() || koMatcher.find() || rnMatcher.find()){
			return new URL(KGML_URL + mapID.substring(0, 2) + "/" + mapID + ".xml");
		} else{
			return new URL(KGML_URL + "organisms/" + mapID.substring(0, 3) + "/" + mapID + ".xml");	
		}
	}
	
	private void expandNnfPathway(CyNode mapNode){
		for (Integer nodeIndex : mapNode.getNestedNetwork().getNodeIndicesArray()) {
			mapNode.getNestedNetwork().getNode(nodeIndex);
		}
	}

	@Override
	public void addNodeContextMenuItems(NodeView nv, JPopupMenu menu) {
		if (menu == null)
			return;

		final JMenu keggMenu = new JMenu("KEGG Options");
		JMenuItem setNnfItem = new JMenuItem();
		JMenuItem expandNnfItem = new JMenuItem();
		keggMenu.add(setNnfItem);
		keggMenu.add(expandNnfItem);

		final String attrValue = nodeAttr.getStringAttribute(nv.getNode()
				.getIdentifier(), "KEGG.entry");
		if (attrValue == null)
			return;

		final KEGGEntryType entryType = KEGGEntryType.getType(attrValue);

		if (entryType == null)
			return;

		JMenuItem item = new JMenuItem();

		if (entryType.equals(KEGGEntryType.COMPOUND)) {
			try {
				final String compoundID = nodeAttr.getStringAttribute(nv
						.getNode().getIdentifier(), "KEGG.label");
				URL image = new URL(COMPOUND_URL + compoundID + ".gif");
				item.setIcon(new ImageIcon(image));
				item.setText("Compound: " + compoundID);
				item.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						cytoscape.util.OpenBrowser.openURL(COMPOUND_DBGET_URL
								+ compoundID);
					}
				});
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		} else if (entryType.equals(KEGGEntryType.MAP)) {
			final String mapID = nodeAttr.getStringAttribute(
					nv.getNode().getIdentifier(), "KEGG.name").split(":")[1];
			final CyNode mapNode = Cytoscape.getCyNode(nv.getNode().getIdentifier());
			URL image = null;
			try {
				image = new URL(MAP_URL + mapID + ".png");
				setNnfItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							LoadNetworkTask.loadURL(convertToFTP(mapID), true);
							mapNode.setNestedNetwork(Cytoscape.getCurrentNetworkView().getGraphPerspective());
						} catch (MalformedURLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				
				expandNnfItem.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							expander.expandNestedNode(mapNode);

						} catch (Exception e2) {
							// TODO: handle exception
						}

						
					}
				});
				
				item.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						cytoscape.util.OpenBrowser.openURL(MAP_DBGET_URL
								+ mapID);
					}
				});
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			setNnfItem.setText("import maplink pathway: " + mapID + " and set as NNF");
			expandNnfItem.setText("expand maplink pathway: " + mapID + " here");
	
			item.setIcon(new ImageIcon(image));
			item.setText("open pathway: " + mapID + " in Browser");
			
		} else if (entryType.equals(KEGGEntryType.GENE)
				|| entryType.equals(KEGGEntryType.ORTHOLOG)) {
			try {
				final String reactionID = nodeAttr.getStringAttribute(nv.getNode()
						.getIdentifier(), "KEGG.reaction");
				URL image = new URL(REACTION_URL + reactionID.split(":")[1]
						+ ".gif");
				item.setIcon(new ImageIcon(image));
				item.setText("Reaction: " + reactionID);
				item.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						cytoscape.util.OpenBrowser.openURL(REACTION_DBGET_URL+ reactionID);
					}
				});
				
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
