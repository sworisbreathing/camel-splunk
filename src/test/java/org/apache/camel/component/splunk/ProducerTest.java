package org.apache.camel.component.splunk;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.splunk.event.SplunkEvent;
import org.junit.Test;

public class ProducerTest extends SplunkMockTestSupport {

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
        splunkEvent.addPair("key3", "value3");
        template.sendBody("direct:tcp", splunkEvent);
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("direct:stream").to("splunk://stream?username=foo&password=bar&index=myindex&sourceType=StreamSourceType&source=StreamSource").to("mock:stream-result");

                from("direct:submit").to("splunk://submit?username=foo&password=bar&index=myindex&sourceType=testSource&source=test").to("mock:submitresult");

                from("direct:tcp").to("splunk://tcp?username=foo&password=bar&tcpRecieverPort=2222&index=myindex&sourceType=testSource&source=test").to("mock:tcpresult");
            }
        };
    }
}
