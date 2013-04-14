package org.apache.camel.component.splunk;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.splunk.event.SplunkEvent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class SplunkConsumerTest extends CamelTestSupport {
	// TEST WILL RUN ON SPLUNK DEFAULT LOCALHOST+PORT
	// the Splunk username/pw created when Splunk was initialized during your
	// login.
	private final String SPLUNK_USERNAME = "admin";
	private final String SPLUNK_PASSWORD = "preben1212";

	// Index name created in Splunk for integration test
	private final String INDEX = "junit";

	@Test
	public void testSearch() throws Exception {
		getMockEndpoint("mock:search-result").expectedMessageCount(1);
		getMockEndpoint("mock:submit-result").expectedMessageCount(1);
			
		SplunkEvent splunkEvent = new SplunkEvent();
		splunkEvent.addPair("key1", "value1");
		splunkEvent.addPair("key2", "value2");
		splunkEvent.addPair("key3", "value3");
		template.sendBody("direct:submit", splunkEvent);
		assertMockEndpointsSatisfied();
		
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			public void configure() {
				from("direct:submit")
				.to("splunk://submit1?username=" + SPLUNK_USERNAME + "&password=" + SPLUNK_PASSWORD +
						"&writerType=submit&index=" + INDEX + "&sourceType=testSource&source=test")
				.to("mock:submit-result");

				from("splunk://search1?username=" + SPLUNK_USERNAME + "&password=" + SPLUNK_PASSWORD +
						"&searchMode=NORMAL&initEarliestTime=-10s&latestTime=now"+
						"&search=search index=" + INDEX + " sourcetype=testSource").to("mock:search-result");
			}
		};
	}
}
