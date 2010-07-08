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
			
			moduleIDs.add(module.split("  ")[0]);

			System.out.println("--------Module: " + module.split("  ")[0]
					+ " === " + module.split("  ")[1]);
		}

		attr.setListAttribute(network.getIdentifier(), "KEGG.moduleID",
				moduleIDs);

	}

	public void mapRelpathway(String relpathways, CyNetwork network) {
		final List<String> relpathwayIDs = new ArrayList<String>();

		for (String relpathway : relpathways.split("\t")) {
			relpathwayIDs.add(relpathway.split("  ")[0]);

			System.out.println("--------RelPathway: "
					+ relpathway.split("  ")[0] + " === "
					+ relpathway.split("  ")[1]);
		}

		attr.setListAttribute(network.getIdentifier(), "KEGG.relpathwayID",
				relpathwayIDs);

	}

}
