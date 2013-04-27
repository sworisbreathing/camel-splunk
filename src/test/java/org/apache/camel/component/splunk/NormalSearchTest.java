package org.apache.camel.component.splunk;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.splunk.event.SplunkEvent;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("run manually since it requires a running local splunk server")
public class NormalSearchTest extends SplunkTest {
    @Test
    public void testSearch() throws Exception {
        MockEndpoint searchMock = getMockEndpoint("mock:search-result");
        searchMock.expectedMessageCount(1);
        getMockEndpoint("mock:submit-result").expectedMessageCount(1);

        assertMockEndpointsSatisfied(20, TimeUnit.SECONDS);
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
                from("direct:submit").to("splunk://submit1?username=" + SPLUNK_USERNAME + "&password=" + SPLUNK_PASSWORD + "&writerType=submit&index=" + INDEX
                                             + "&sourceType=testSource&source=test").to("mock:submit-result");

                from(
                     "splunk://search1?delay=5s&username=" + SPLUNK_USERNAME + "&password=" + SPLUNK_PASSWORD + "&searchMode=NORMAL&initEarliestTime=-10s&latestTime=now"
                         + "&search=search index=" + INDEX + " sourcetype=testSource").to("mock:search-result");
            }
        };
    }
}
