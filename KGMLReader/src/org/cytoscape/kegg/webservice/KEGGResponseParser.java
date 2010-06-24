package org.cytoscape.kegg.webservice;

import java.util.ArrayList;
import java.util.List;

import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;

public class KEGGResponseParser {
	
	private final CyAttributes attr;
	
	KEGGResponseParser() {
		this.attr = Cytoscape.getNetworkAttributes();
	}
	
	
	public void parse(final String response, CyNetwork network) {
		final String[] lines = response.split("\n");
		for (int i = 0; i < lines.length; i++) {
			final String[] entries = lines[i].split(" {2,}");
			for (final String entry : entries) {
				System.out.println("####" + entry);
				if (entry.startsWith("NAME")) {
					System.out.println("Name found----> " + entries[1]);
					attr.setAttribute(network.getIdentifier(), "KEGG.fullName",
							entries[1]);
				} else if (entry.startsWith("MODULE")) {
					final List<String> modules = new ArrayList<String>();
					final List<String> moduleNames = new ArrayList<String>();
					modules.add(entries[1]);
					moduleNames.add(entries[2]);
					i++;
					String[] moduleEntry = lines[i].split(" {2,}");
					while (i < lines.length
							&& moduleEntry[0].trim().length() == 0) {
						modules.add(moduleEntry[1]);
						moduleNames.add(moduleEntry[2]);
						i++;
						moduleEntry = lines[i].split(" {2,}");
					}
					attr.setListAttribute(network.getIdentifier(),
							"KEGG.moduleID", modules);
					attr.setListAttribute(network.getIdentifier(),
							"KEGG.moduleName", moduleNames);
					break;
				}
			}

			System.out.println("--------------------------\n");
		}
	}


}
