package org.apache.camel.component.splunk;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.splunk.event.SplunkEvent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class SplunkComponentTest extends CamelTestSupport {

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
						.to("splunk://stream1?host=localhost&port=8089&username=admin&password=preben1212&writerType=stream&index=stream-test&sourceType=StreamSourceTypee&source=StreamSource")
						.to("mock:stream-result");
				from("direct:submit")
						.to("splunk://submit1?host=localhost&port=8089&username=admin&password=preben1212&writerType=submit&index=test&sourceType=testSource&source=test")
						.to("mock:submitresult");
				from("direct:tcp")
						.to("splunk://tcp1?host=localhost&port=8089&username=admin&password=preben1212&writerType=tcp&tcpRecieverPort=9997&index=test&sourceType=testSource&source=test")
						.to("mock:tcpresult");
			}
		};
	}
}
