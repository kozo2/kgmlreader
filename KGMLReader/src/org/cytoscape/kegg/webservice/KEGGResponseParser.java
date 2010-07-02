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

import net.arnx.jsonic.JSON;

public class KEGGResponseParser {

	private static final String EOF = "///";

	private final CyAttributes attr;

	KEGGResponseParser() {
		this.attr = Cytoscape.getNetworkAttributes();
	}

	public void parsePathway(final String response, CyNetwork network) {
		Map<String, List<String>> entryMap = parseFullEntry(response);

		// Get full name of this pathway
		List<String> fullName = entryMap.get("NAME");
		if (fullName != null && fullName.size() != 0)
			attr.setAttribute(network.getIdentifier(), "KEGG.fullName",
					fullName.get(0));

		// Import Modules as Network attribute
		mapModule(entryMap.get("MODULE"), network);
	}
	
	public void mapJsonKeys(final String response, CyNetwork network) {
		final List<String> moduleIDs = new ArrayList<String>(JSON.decode(response, HashMap.class).keySet());
		attr.setListAttribute(network.getIdentifier(), "KEGG.moduleID", moduleIDs);
	}

	private void mapModule(List<String> modules, CyNetwork network) {
		final List<String> moduleIDs = new ArrayList<String>();
		final List<String> moduleNames = new ArrayList<String>();

		for (final String module : modules) {
			String[] parts = module.split(" {2,}");
			moduleIDs.add(parts[0]);
			moduleNames.add(parts[1]);
			System.out.println("--------Module: " + parts[0] + " === " + parts[1]);
		}

		attr
				.setListAttribute(network.getIdentifier(), "KEGG.moduleID",
						moduleIDs);
		attr.setListAttribute(network.getIdentifier(), "KEGG.moduleName",
				moduleNames);
	}

	public Map<String, List<String>> parseFullEntry(final String response) {
		// Split
		final String[] lines = response.split("\n");
		Pattern p = Pattern.compile("^[A-Z].+");
		Matcher m;
		final Map<String, List<String>> data = new HashMap<String, List<String>>();

		String key = null;
		String val = null;
		for (int i = 0; i < lines.length; i++) {
			m = p.matcher(lines[i]);
			if (m.matches()) {
				final List<String> entry = new ArrayList<String>();

				String[] tags = lines[i].split(" ");
				if (tags.length < 2)
					continue;

				key = tags[0];
				val = lines[i].split(key)[1].trim();
				entry.add(val);
				System.out.println("Key, Val = " + key + ", " + val);

				i++;
				m = p.matcher(lines[i]);
				while (!m.matches()) {
					val = lines[i].trim();
					if (val.startsWith(EOF)) {
						data.put(key, entry);
						return data;
					}
					entry.add(val);
					System.out.println("Key, Val = " + key + ", " + val);
					i++;
					m = p.matcher(lines[i]);
				}
				i--;
				data.put(key, entry);
			}
		}

		return data;
	}

}
