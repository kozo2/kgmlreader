package org.cytoscape.kegg.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;

public class KEGGResponseParser {

	private final CyAttributes attr;

	KEGGResponseParser() {
		this.attr = Cytoscape.getNetworkAttributes();
	}

	public void mapModule(String modules, CyNetwork network) {
		final List<String> moduleIDs = new ArrayList<String>();

		for (final String module : modules.split("\t")) {
			final String[] parts = module.split("  ");
			moduleIDs.add(parts[0]);
		}

		attr.setListAttribute(network.getIdentifier(), "KEGG.moduleID",
				moduleIDs);

	}

	public void mapRelpathway(String relpathways, CyNetwork network) {
		final List<String> relpathwayIDs = new ArrayList<String>();

		for (String relpathway : relpathways.split("\t")) {
			relpathwayIDs.add(relpathway.split("  ")[0]);

		}

		attr.setListAttribute(network.getIdentifier(), "KEGG.relpathwayID",
				relpathwayIDs);

	}

	public void mapDisease(String diseases, CyNetwork network) {
		final List<String> diseaseIDs = new ArrayList<String>();

		for (String disease : diseases.split("\t")) {
			diseaseIDs.add(disease.split("  ")[0]);
		}

		attr.setListAttribute(network.getIdentifier(), "KEGG.diseaseID",
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
			attr.setListAttribute(network.getIdentifier(), "UMBBD.dblinks",
					umbbdIDs);
			attr.setListAttribute(network.getIdentifier(), "GO.dblinks", goIDs);
		} else if (dblinks.split("\t").length == 1) {
			if (dblinks.split("UMBBD: ").length == 2) {
				final List<String> umbbdIDs = new ArrayList<String>();
				for (String umbbdID : dblinks.split("UMBBD: ")[1].split(" ")) {
					umbbdIDs.add(umbbdID);
				}
				attr.setListAttribute(network.getIdentifier(), "UMBBD.dblinks",
						umbbdIDs);
			} else if (dblinks.split("GO: ").length == 2) {
				final List<String> goIDs = new ArrayList<String>();
				for (String goID : dblinks.split("GO: ")[1].split(" ")) {
					goIDs.add(goID);
				}
				attr.setListAttribute(network.getIdentifier(), "GO.dblinks",
						goIDs);
			}
		}

	}

}
