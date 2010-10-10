package org.cytoscape.kegg.webservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;

public class KEGGResponseParser {

	private final CyAttributes netAttr;
	private final CyAttributes nodeAttr;
	private final List<CyNode> nodes;

	KEGGResponseParser() {
		this.netAttr = Cytoscape.getNetworkAttributes();
		this.nodeAttr = Cytoscape.getNodeAttributes();
		this.nodes = Cytoscape.getCyNodesList();
	}

	public void mapModule(String modules, CyNetwork network) {
		final List<String> moduleIDs = new ArrayList<String>();

		for (final String module : modules.split("\t")) {
			final String[] parts = module.split("  ");
			moduleIDs.add(parts[0]);
		}

		netAttr.setListAttribute(network.getIdentifier(), "KEGG.moduleID",
				moduleIDs);

	}

	public List<String> getReactionIDs(String reactions) {
		final List<String> reactionIDs = new ArrayList<String>();

		for (String reaction : reactions.split("\t")) {
			reactionIDs.add(reaction.split("  ")[0]);
		}

		return reactionIDs;
	}

	public void mapModuleReaction(Map<String, List<String>> module2reactionMap,
			CyNetwork network) {
		for (CyNode node : nodes) {
			List<String> reactionIDs = nodeAttr.getListAttribute(
					node.getIdentifier(), "KEGG.reaction.list");

			if (reactionIDs != null) {
				List<String> keggModules = new ArrayList<String>();
				for (String reactionID : reactionIDs) {
					
					for (String moduleID : module2reactionMap.keySet()) {
						if (module2reactionMap.get(moduleID).contains(
								reactionID.replace("rn:", ""))) {
							keggModules.add(moduleID);
						}
					}
				}
				nodeAttr.setListAttribute(node.getIdentifier(),
						"KEGG.module.list", keggModules);
			}
		}
	}

	public void mapRelpathway(String relpathways, CyNetwork network) {
		final List<String> relpathwayIDs = new ArrayList<String>();

		for (String relpathway : relpathways.split("\t")) {
			relpathwayIDs.add(relpathway.split("  ")[0]);

		}

		netAttr.setListAttribute(network.getIdentifier(), "KEGG.relpathwayID",
				relpathwayIDs);

	}

	public void mapDisease(String diseases, CyNetwork network) {
		final List<String> diseaseIDs = new ArrayList<String>();

		for (String disease : diseases.split("\t")) {
			diseaseIDs.add(disease.split("  ")[0]);
		}

		netAttr.setListAttribute(network.getIdentifier(), "KEGG.diseaseID",
				diseaseIDs);

	}

	public void mapDblink(String dblinks, CyNetwork network) {
		if (dblinks.split("\t").length == 2) {
			final List<String> umbbdIDs = new ArrayList<String>();
			final List<String> goIDs = new ArrayList<String>();
			for (String umbbdID : dblinks.split("\t")[0].split(": ")[1]
					.split(" ")) {
				umbbdIDs.add(umbbdID);
			}
			for (String goID : dblinks.split("\t")[1].split(": ")[1].split(" ")) {
				goIDs.add(goID);
			}
			netAttr.setListAttribute(network.getIdentifier(), "UMBBD.dblinks",
					umbbdIDs);
			netAttr.setListAttribute(network.getIdentifier(), "GO.dblinks",
					goIDs);
		} else if (dblinks.split("\t").length == 1) {
			if (dblinks.split("UMBBD: ").length == 2) {
				final List<String> umbbdIDs = new ArrayList<String>();
				for (String umbbdID : dblinks.split("UMBBD: ")[1].split(" ")) {
					umbbdIDs.add(umbbdID);
				}
				netAttr.setListAttribute(network.getIdentifier(),
						"UMBBD.dblinks", umbbdIDs);
			} else if (dblinks.split("GO: ").length == 2) {
				final List<String> goIDs = new ArrayList<String>();
				for (String goID : dblinks.split("GO: ")[1].split(" ")) {
					goIDs.add(goID);
				}
				netAttr.setListAttribute(network.getIdentifier(), "GO.dblinks",
						goIDs);
			}
		}

	}

}
