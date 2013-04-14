package org.apache.camel.component.splunk;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.splunk.event.SplunkEvent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class SplunkProducerTest extends CamelTestSupport {
	// TEST WILL RUN ON SPLUNK DEFAULT LOCALHOST+PORT
	// the Splunk username/pw created when Splunk was initialized during your login.
	private final String SPLUNK_USERNAME = "admin";
	private final String SPLUNK_PASSWORD = "preben1212";
	
	//Index name created in Splunk for integration test
	private final String INDEX = "junit";
	
	//Splunk tcp reciever port configured in Splunk
	private final String TCP_RECIEVER_PORT = "9997";
	
	
	@Test
	public void testStreamWriter() throws Exception {
		MockEndpoint mock = getMockEndpoint("mock:stream-result");
		mock.expectedMinimumMessageCount(1);
		SplunkEvent splunkEvent = new SplunkEvent();
		splunkEvent.addPair("key11", "value1");
		splunkEvent.addPair("key22", "value2");
		splunkEvent.addPair("key33", "value3");
		template.sendBody("direct:stream", splunkEvent);
		assertMockEndpointsSatisfied();
	}

	@Test
	public void testSubmitWriter() throws Exception {
		MockEndpoint mock = getMockEndpoint("mock:submitresult");
		mock.expectedMinimumMessageCount(1);
		SplunkEvent splunkEvent = new SplunkEvent();
		splunkEvent.addPair("key1", "value1");
		splunkEvent.addPair("key2", "value2");
		splunkEvent.addPair("key3", "value1");
		template.sendBody("direct:submit", splunkEvent);
		assertMockEndpointsSatisfied();
	}

	@Test
	public void testTcpWriter() throws Exception {
		MockEndpoint mock = getMockEndpoint("mock:tcpresult");
		mock.expectedMinimumMessageCount(1);
		SplunkEvent splunkEvent = new SplunkEvent();
		splunkEvent.addPair("key1", "value1");
		splunkEvent.addPair("key2", "value2");
		splunkEvent.addPair("key3", "value1");
		template.sendBody("direct:tcp", splunkEvent);
		assertMockEndpointsSatisfied();
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			public void configure() {
				from("direct:stream")
						.to("splunk://stream1?username=" + SPLUNK_USERNAME + 
								"&password="+ SPLUNK_PASSWORD + 
								"&writerType=stream" +
								"&index=" + INDEX + 
								"&sourceType=StreamSourceType&source=StreamSource")
						.to("mock:stream-result");
				
				from("direct:submit")
						.to("splunk://submit1?username=" + SPLUNK_USERNAME +
								"&password=" + SPLUNK_PASSWORD +
								"&writerType=submit&index=" + INDEX +
								"&sourceType=testSource&source=test")
						.to("mock:submitresult");
				
				from("direct:tcp")
						.to("splunk://tcp1?username=" + SPLUNK_USERNAME +
								"&password=" + SPLUNK_PASSWORD +
								"&writerType=tcp&tcpRecieverPort=" + TCP_RECIEVER_PORT + 
								"&index=" + INDEX +
								"&sourceType=testSource&source=test")
						.to("mock:tcpresult");
			}
		};
	}
}
