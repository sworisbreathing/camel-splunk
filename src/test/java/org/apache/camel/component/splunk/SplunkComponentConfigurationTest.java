package org.apache.camel.component.splunk;

import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class SplunkComponentConfigurationTest extends CamelTestSupport {
	@Test
	public void createEndpointWithMinimalConfiguration() throws Exception {
		SplunkComponent component = new SplunkComponent();
		component.setCamelContext(context);
		SplunkEndpoint endpoint = (SplunkEndpoint) component.createEndpoint("splunk://test?username=test&password=pw");
		assertEquals("localhost", endpoint.getConfiguration().getHost());
		assertEquals(8089, endpoint.getConfiguration().getPort());
		assertEquals("test", endpoint.getConfiguration().getUsername());
		assertEquals("pw", endpoint.getConfiguration().getPassword());
	}

	@Test
	public void createEndpointWithMaximalConfiguration() throws Exception {
		SplunkComponent component = new SplunkComponent();
		component.setCamelContext(context);
		SplunkEndpoint endpoint = (SplunkEndpoint) component
				.createEndpoint("splunk://test?username=test&password=pw&host=myhost&port=3333&writerType=tcp&tcpRecieverPort=4444&index=myindex&sourceType=testSource&source=test");
		assertEquals("myhost", endpoint.getConfiguration().getHost());
		assertEquals(3333, endpoint.getConfiguration().getPort());
		assertEquals("test", endpoint.getConfiguration().getUsername());
		assertEquals("pw", endpoint.getConfiguration().getPassword());
		assertEquals(4444, endpoint.getConfiguration().getTcpRecieverPort());
		assertEquals("myindex", endpoint.getConfiguration().getIndex());
		assertEquals("testSource", endpoint.getConfiguration().getSourceType());
		assertEquals("test", endpoint.getConfiguration().getSource());
	}

}
