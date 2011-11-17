package org.cytoscape.data.reader.kgml.test;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleClientTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testClient() throws Exception {
		
		final HttpGet httpget = new HttpGet("http://taruo.net/e/");
		final DefaultHttpClient httpclient = new DefaultHttpClient();
		final HttpParams param = httpclient.getParams();
		HttpProtocolParams.setUserAgent(param, "Cytoscape ");
		final HttpResponse response = httpclient.execute(httpget);
		final HttpEntity entity = response.getEntity();

		System.out.println(EntityUtils.toString(entity));
	}
}
