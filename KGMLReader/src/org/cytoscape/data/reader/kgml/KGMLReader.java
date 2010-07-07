package org.cytoscape.data.reader.kgml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.cytoscape.data.reader.kgml.generated.Pathway;
import org.cytoscape.kegg.webservice.KEGGRestClient;

import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;
import cytoscape.data.readers.AbstractGraphReader;
import cytoscape.util.CyNetworkNaming;
import cytoscape.util.URLUtil;

public class KGMLReader extends AbstractGraphReader {

	private static final String PACKAGE_NAME = "org.cytoscape.data.reader.kgml.generated";

	static final String NETWORK_TYPE = "network type";
	static final String NETWORK_TYPE_VALUE = "KEGG Pathway";
	static final String SPECIES = "KEGG.org";
	static final String NUMBER = "KEGG.number";
	static final String IMAGE = "KEGG.image";
	static final String LINK = "KEGG.link";
	static final String TITLE = "KEGG.title";

	private URL targetURL;

	private int[] nodeIdx;
	private int[] edgeIdx;

	private String networkName;

	private PathwayMapper mapper;

	private Pathway pathway;

	public KGMLReader(final String fileName) {
		super(fileName);
		System.out.println("Debug: KGML File name = " + fileName);
		try {
			this.targetURL = (new File(fileName)).toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doPostProcessing(CyNetwork network) {
		mapper.updateView(network);

		// Set network Attr
		this.mapNetwork(network);
	}

	@Override
	public int[] getEdgeIndicesArray() {
		return edgeIdx;
	}

	@Override
	public String getNetworkName() {
		return networkName;
	}

	@Override
	public int[] getNodeIndicesArray() {
		return nodeIdx;
	}

	@Override
	public void read() throws IOException {
		InputStream is = null;
		pathway = null;

		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(
					PACKAGE_NAME, this.getClass().getClassLoader());
			final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			is = URLUtil.getBasicInputStream(targetURL);
			pathway = (Pathway) unmarshaller.unmarshal(is);
			networkName = CyNetworkNaming.getSuggestedNetworkTitle(pathway
					.getTitle());

		} catch (Exception e) {
			e.printStackTrace();

			throw new IOException("Could not unmarshall KGML file");
		} finally {
			if (is != null) {
				is.close();
			}
		}

		mapper = new PathwayMapper(pathway);
		mapper.doMapping();
		nodeIdx = mapper.getNodeIdx();
		edgeIdx = mapper.getEdgeIdx();

	}

	/**
	 * Store general pathway information as network attribute
	 */
	private void mapNetwork(final CyNetwork network) {
		final CyAttributes netAttr = Cytoscape.getNetworkAttributes();
		netAttr.setAttribute(network.getIdentifier(), PathwayMapper.KEGG_NAME,
				pathway.getName());
		netAttr
				.setAttribute(network.getIdentifier(), SPECIES, pathway
						.getOrg());
		netAttr.setAttribute(network.getIdentifier(), NUMBER, pathway
				.getNumber());
		netAttr
				.setAttribute(network.getIdentifier(), IMAGE, pathway
						.getImage());
		netAttr.setAttribute(network.getIdentifier(), LINK, pathway.getLink());
		netAttr
				.setAttribute(network.getIdentifier(), TITLE, pathway
						.getTitle());
		netAttr.setAttribute(network.getIdentifier(), NETWORK_TYPE,
				NETWORK_TYPE_VALUE);

		if (!pathway.getNumber().equals("01100")
				&& !pathway.getNumber().equals("01110")) {
			try {
				KEGGRestClient.getCleint().importAnnotation(
						pathway.getOrg() + pathway.getNumber(), network);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

}
