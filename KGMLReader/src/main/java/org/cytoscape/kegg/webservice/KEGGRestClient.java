package org.cytoscape.kegg.webservice;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;
import cytoscape.view.CyNetworkView;
import cytoscape.visual.VisualStyle;

/**
 * Very simple Client for togoWS Rest service.
 * 
 * @author kono
 * @author Kozo.Nishida
 * 
 */
public class KEGGRestClient {

	private static final String USER_AGENT = "Cytoscape KEGG/togoWS REST Client v0.06 (Apache HttpClient 4.0.1)";

	private static final String KEGG_BASE_URL = "http://togows.dbcls.jp/entry/";
	private static final String FORMAT_JSON = ".json";
	private final CyAttributes attr;
	private final CyAttributes nodeAttr;

	private enum DatabaseType {
		COMPOUND("compound"), PATHWAY("kegg-pathway"), MODULE("kegg-module");

		private final String type;

		private DatabaseType(final String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}

	private enum FieldType {
		DISEASE("diseases"), DBLINKS("dblinks"), REL_PATHWAY("relpathways"), MODULE(
				"modules"), MODULE_JSON("modules.json"), NAME("name"), REACTION(
				"reactions");

		private final String type;

		private FieldType(final String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}

	// This client is a singleton.
	private static KEGGRestClient client = new KEGGRestClient();

	public static KEGGRestClient getCleint() {
		return client;
	}

	private final KEGGResponseParser parser;

	private KEGGRestClient() {
		this.parser = new KEGGResponseParser();
		this.attr = Cytoscape.getNetworkAttributes();
		this.nodeAttr = Cytoscape.getNodeAttributes();
	}

	public void importCompoundName(final String pathwayName, CyNetwork network)
			throws IOException {

		final List<CyNode> cyNodes = Cytoscape.getCyNodesList();
		final String vsName = "KEGG: " + network.getTitle() + " ("
				+ pathwayName + ")";

		for (CyNode cyNode : cyNodes) {
			if (nodeAttr.getStringAttribute(cyNode.getIdentifier(),
					"KEGG.entry").equals("compound")) {
				final String compoundName = getEntryField(
						DatabaseType.COMPOUND, nodeAttr.getStringAttribute(
								cyNode.getIdentifier(), "KEGG.label"),
						FieldType.NAME);
				nodeAttr.setAttribute(cyNode.getIdentifier(),
						"KEGG.label.first", compoundName);
				nodeAttr.setAttribute(cyNode.getIdentifier(),
						"compound.label.width", 10);

			}
		}

		final VisualStyle targetStyle = Cytoscape.getVisualMappingManager()
				.getCalculatorCatalog().getVisualStyle(vsName);
		Cytoscape.getVisualMappingManager().setVisualStyle(targetStyle);
		final CyNetworkView view = Cytoscape.getNetworkView(network
				.getIdentifier());
		Cytoscape.getVisualMappingManager().setNetworkView(view);
		view.redrawGraph(false, true);

	}

	public void importAnnotation(final String pathwayID, CyNetwork network)
			throws IOException {

		final String result = getEntries(DatabaseType.PATHWAY, pathwayID);
		final String moduleEntryField = getEntryField(DatabaseType.PATHWAY,
				pathwayID, FieldType.MODULE);
		final String relpathwayEntryField = getEntryField(DatabaseType.PATHWAY,
				pathwayID, FieldType.REL_PATHWAY);
		final String dblinkEntryField = getEntryField(DatabaseType.PATHWAY,
				pathwayID, FieldType.DBLINKS);
		final String diseaseEntryField = getEntryField(DatabaseType.PATHWAY,
				pathwayID, FieldType.DISEASE);

		if (moduleEntryField != null) {
			parser.mapModule(moduleEntryField, network);

		}

		if (relpathwayEntryField != null) {
			parser.mapRelpathway(relpathwayEntryField, network);
		}

		if (dblinkEntryField != null) {
			parser.mapDblink(dblinkEntryField, network);
		}

		if (diseaseEntryField != null) {
			parser.mapDisease(diseaseEntryField, network);
		}

		final List<String> moduleIDs = attr.getListAttribute(
				network.getIdentifier(), "KEGG.moduleID");

		final Map<String, List<String>> module2reactionMap = new HashMap<String, List<String>>();

		for (String moduleID : moduleIDs) {
			String moduleReactions = getEntryField(DatabaseType.MODULE,
					moduleID, FieldType.REACTION);
			module2reactionMap.put(moduleID,
					parser.getReactionIDs(moduleReactions));

		}

		if (module2reactionMap != null) {
			parser.mapModuleReaction(module2reactionMap, network);
		}

	}

	private String getEntries(final DatabaseType type, final String id)
			throws IOException {
		final HttpGet httpget = new HttpGet(KEGG_BASE_URL + type.getType()
				+ "/" + id);

		return fetchData(httpget);
	}

	
	private String getEntryField(final DatabaseType dbType, final String id,
			final FieldType fieldType) throws IOException {
		final HttpGet httpget = new HttpGet(KEGG_BASE_URL + dbType.getType()
				+ "/" + id + "/" + fieldType.getType());
		
		return fetchData(httpget);
	}

	
	private String fetchData(HttpGet httpget) throws IOException {
		final DefaultHttpClient httpclient = new DefaultHttpClient();
		final HttpParams param = httpclient.getParams();
		HttpProtocolParams.setUserAgent(param, USER_AGENT);
		final HttpResponse response = httpclient.execute(httpget);
		final HttpEntity entity = response.getEntity();

		if (entity != null) {
			return EntityUtils.toString(entity);
		} else
			return null;
	}

}
