package org.cytoscape.data.reader.kgml.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.cytoscape.data.reader.kgml.KGMLReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KGMLReaderTest {
	
	private KGMLReader reader1;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRead() throws Exception {
		reader1 = new KGMLReader("testData/kgml/metabolic/organisms/bsu/bsu00010.xml");
		reader1.read();
		
		final int[] nodeArray = reader1.getNodeIndicesArray();
	
		assertNotNull(nodeArray);
		assertEquals(93, nodeArray.length);
		
		final int[] edgeArray = reader1.getEdgeIndicesArray();
		
		assertNotNull(edgeArray);

	}

}
