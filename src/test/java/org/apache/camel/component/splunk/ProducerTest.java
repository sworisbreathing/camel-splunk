package org.apache.camel.component.splunk;

import static org.hamcrest.CoreMatchers.instanceOf;

import org.apache.camel.EndpointInject;
import org.apache.camel.Producer;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.splunk.event.SplunkEvent;
import org.apache.camel.component.splunk.support.StreamDataWriter;
import org.apache.camel.component.splunk.support.SubmitDataWriter;
import org.apache.camel.component.splunk.support.TcpDataWriter;
import org.junit.Test;

public class ProducerTest extends SplunkMockTestSupport {
    @EndpointInject(uri = "splunk://stream")
    protected SplunkEndpoint streamEndpoint;

    @EndpointInject(uri = "splunk://submit")
    protected SplunkEndpoint submitEndpoint;

    @EndpointInject(uri = "splunk://tcp")
    protected SplunkEndpoint tcpEndpoint;

    @Test
    public void testStreamWriter() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:stream-result");
        mock.setExpectedMessageCount(1);
        SplunkEvent splunkEvent = new SplunkEvent();
        splunkEvent.addPair("key11", "value1");
        splunkEvent.addPair("key22", "value2");
        splunkEvent.addPair("key33", "value3");
        template.sendBody("direct:stream", splunkEvent);
        assertMockEndpointsSatisfied();
        Producer streamProducer = streamEndpoint.createProducer();
        assertThat(((SplunkProducer)streamProducer).getDataWriter(), instanceOf(StreamDataWriter.class));
    }

    @Test
    public void testSubmitWriter() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:submitresult");
        mock.setExpectedMessageCount(1);
        SplunkEvent splunkEvent = new SplunkEvent();
        splunkEvent.addPair("key1", "value1");
        splunkEvent.addPair("key2", "value2");
        splunkEvent.addPair("key3", "value1");
        template.sendBody("direct:submit", splunkEvent);
        assertMockEndpointsSatisfied();
        Producer submitProducer = submitEndpoint.createProducer();
        assertThat(((SplunkProducer)submitProducer).getDataWriter(), instanceOf(SubmitDataWriter.class));
    }

    @Test
    public void testTcpWriter() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:tcpresult");
        mock.setExpectedMessageCount(1);
        SplunkEvent splunkEvent = new SplunkEvent();
        splunkEvent.addPair("key1", "value1");
        splunkEvent.addPair("key2", "value2");
        splunkEvent.addPair("key3", "value3");
        template.sendBody("direct:tcp", splunkEvent);
        assertMockEndpointsSatisfied();
        Producer tcpProducer = tcpEndpoint.createProducer();
        assertThat(((SplunkProducer)tcpProducer).getDataWriter(), instanceOf(TcpDataWriter.class));
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
