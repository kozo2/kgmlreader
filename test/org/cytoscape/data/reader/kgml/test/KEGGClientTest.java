package org.cytoscape.data.reader.kgml.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.cytoscape.kegg.webservice.KEGGRestClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;

//import net.arnx.jsonic.JSON;

public class KEGGClientTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testUserAgent() {
		
	}

	@Test
	public void testClient() throws Exception {
		CyNetwork net = Cytoscape.createNetwork("kegg dummy1");
		CyAttributes attr = Cytoscape.getNetworkAttributes();
		
		KEGGRestClient.getCleint().importAnnotation("bsu00010", net);
		
		List<String> moduleIDs = attr.getListAttribute(net.getIdentifier(), "KEGG.moduleID");
		assertNotNull(moduleIDs);
		assertEquals(8, moduleIDs.size());
		
		List<String> relpathwayIDs = attr.getListAttribute(net.getIdentifier(), "KEGG.relpathwayID");
		assertNotNull(relpathwayIDs);
		assertEquals(6, relpathwayIDs.size());
	
//		String fullName = attr.getStringAttribute(net.getIdentifier(), "KEGG.fullName");
//		assertNotNull(fullName);
//		assertEquals("Citrate cycle (TCA cycle) - Homo sapiens (human)", fullName);
		
//		String modules = KEGGRestClient.getCleint().importAnnotation("hsa00020", net);
//		assertNotNull(modules.split("\t"));
//		assertEquals(6, modules.split("\t").length);
		
//		List moduleIDs = attr.getListAttribute(net.getIdentifier(), "KEGG.moduleID");
//		assertNotNull(moduleIDs);
	}
}
