package org.apache.camel.component.splunk;

import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import com.splunk.JobArgs.SearchMode;

public class SplunkComponentConfigurationTest extends CamelTestSupport {
	@Test
	public void createProducerEndpointWithMinimalConfiguration() throws Exception {
		SplunkComponent component = new SplunkComponent();
		component.setCamelContext(context);
		SplunkEndpoint endpoint = (SplunkEndpoint) component.createEndpoint("splunk://test?username=test&password=pw");
		assertEquals("localhost", endpoint.getConfiguration().getHost());
		assertEquals(8089, endpoint.getConfiguration().getPort());
		assertEquals("test", endpoint.getConfiguration().getUsername());
		assertEquals("pw", endpoint.getConfiguration().getPassword());
		assertEquals("https", endpoint.getConfiguration().getScheme());
		assertEquals(5000, endpoint.getConfiguration().getConnectionTimeout());
	}

	@Test
	public void createProducerEndpointWithMaximalConfiguration() throws Exception {
		SplunkComponent component = new SplunkComponent();
		component.setCamelContext(context);
		SplunkEndpoint endpoint = (SplunkEndpoint) component
				.createEndpoint("splunk://test?username=test&password=pw&host=myhost&port=3333&" +
						"writerType=tcp&tcpRecieverPort=4444&index=myindex&sourceType=testSource&" +
						"source=test&owner=me&app=fantasticapp");
		assertEquals("myhost", endpoint.getConfiguration().getHost());
		assertEquals(3333, endpoint.getConfiguration().getPort());
		assertEquals("test", endpoint.getConfiguration().getUsername());
		assertEquals("pw", endpoint.getConfiguration().getPassword());
		assertEquals(4444, endpoint.getConfiguration().getTcpRecieverPort());
		assertEquals("myindex", endpoint.getConfiguration().getIndex());
		assertEquals("testSource", endpoint.getConfiguration().getSourceType());
		assertEquals("test", endpoint.getConfiguration().getSource());
		assertEquals("me", endpoint.getConfiguration().getOwner());
		assertEquals("fantasticapp", endpoint.getConfiguration().getApp());
	}

	@Test
	public void createConsumerEndpointWithMinimalConfiguration() throws Exception {
		SplunkComponent component = new SplunkComponent();
		component.setCamelContext(context);
		SplunkEndpoint endpoint = (SplunkEndpoint) component.createEndpoint("splunk://test?username=test&" +
				"password=pw&search=Splunk search query goes here");
		assertEquals("localhost", endpoint.getConfiguration().getHost());
		assertEquals(8089, endpoint.getConfiguration().getPort());
		assertEquals("test", endpoint.getConfiguration().getUsername());
		assertEquals("pw", endpoint.getConfiguration().getPassword());
		assertEquals("https", endpoint.getConfiguration().getScheme());
		assertEquals(5000, endpoint.getConfiguration().getConnectionTimeout());
		assertEquals("Splunk search query goes here", endpoint.getConfiguration().getSearch());
		assertEquals(SearchMode.NORMAL, endpoint.getConfiguration().getSearchMode());
	}
	
	@Test
	public void createConsumerEndpointWithMaximalConfiguration() throws Exception {
		SplunkComponent component = new SplunkComponent();
		component.setCamelContext(context);
		SplunkEndpoint endpoint = (SplunkEndpoint) component
				.createEndpoint("splunk://test?username=test&password=pw&host=myhost&port=3333&delay=10s&" +
						"search=Splunk search query goes here&initEarliestTime=-1d" +
						"&latestTime=now&fieldList=field1,field2&maxRows=10&" +
						"owner=me&app=fantasticapp");
		assertEquals("myhost", endpoint.getConfiguration().getHost());
		assertEquals(3333, endpoint.getConfiguration().getPort());
		assertEquals("test", endpoint.getConfiguration().getUsername());
		assertEquals("pw", endpoint.getConfiguration().getPassword());
		assertEquals("-1d", endpoint.getConfiguration().getInitEarliestTime());
		assertEquals("now", endpoint.getConfiguration().getLatestTime());
		assertEquals("field1,field2", endpoint.getConfiguration().getFieldList());
		assertEquals(10, endpoint.getConfiguration().getMaxRows());
		assertEquals("me", endpoint.getConfiguration().getOwner());
		assertEquals("fantasticapp", endpoint.getConfiguration().getApp());
	}

}
