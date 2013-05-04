package org.apache.camel.component.splunk.integration;

import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.splunk.event.SplunkEvent;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("run manually since it requires a running local splunk server")
public class RealtimeSearchTest extends SplunkTest {

    @Test
    public void testRealtimeSearch() throws Exception {
        MockEndpoint searchMock = getMockEndpoint("mock:search-saved");
        searchMock.expectedMessageCount(1);

        assertMockEndpointsSatisfied();
        SplunkEvent recieved = searchMock.getReceivedExchanges().get(0).getIn().getBody(SplunkEvent.class);
        assertNotNull(recieved);
        Map<String, String> data = recieved.getEventData();
        assertEquals("value1", data.get("key1"));
        assertEquals("value2", data.get("key2"));
        assertEquals("value3", data.get("key3"));
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("direct:submit").to("splunk://submit?username=" + SPLUNK_USERNAME + "&password=" + SPLUNK_PASSWORD + "&index=" + INDEX + "&sourceType=testSource&source=test")
                    .to("mock:submit-result");
                from(
                     "splunk://realtime?delay=5s&username=" + SPLUNK_USERNAME + "&password=" + SPLUNK_PASSWORD + "&initEarliestTime=rt-10s&search=search index=" + INDEX
                         + " sourcetype=testSource").to("mock:search-saved");
            }
        };
    }
}
