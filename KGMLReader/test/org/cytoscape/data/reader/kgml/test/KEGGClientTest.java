package org.cytoscape.data.reader.kgml.test;


import static org.junit.Assert.*;

import java.util.List;

import org.cytoscape.kegg.webservice.KEGGRestClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;


public class KEGGClientTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testClient() throws Exception {
		CyNetwork net = Cytoscape.createNetwork("kegg dummy1");
		CyAttributes attr = Cytoscape.getNetworkAttributes();
		
		KEGGRestClient.getCleint().importAnnotation("hsa00020", net);
	
		String fullName = attr.getStringAttribute(net.getIdentifier(), "KEGG.fullName");
		assertNotNull(fullName);
		assertEquals("Citrate cycle (TCA cycle) - Homo sapiens (human)", fullName);
		
		List moduleIDs = attr.getListAttribute(net.getIdentifier(), "KEGG.moduleID");
		assertNotNull(moduleIDs);
		assertEquals(6, moduleIDs.size());
	}

}
