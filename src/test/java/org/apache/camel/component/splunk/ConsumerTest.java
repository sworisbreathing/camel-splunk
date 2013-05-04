package org.apache.camel.component.splunk;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.splunk.event.SplunkEvent;
import org.junit.Test;

import com.splunk.Job;
import com.splunk.JobCollection;

public class ConsumerTest extends SplunkMockTestSupport {
    @Test
    public void testSearch() throws Exception {
        MockEndpoint searchMock = getMockEndpoint("mock:search-result");
        searchMock.expectedMessageCount(3);
        JobCollection jobCollection = mock(JobCollection.class);
        Job jobMock = mock(Job.class);
        when(service.getJobs()).thenReturn(jobCollection);
        when(jobCollection.create(anyString(), anyMap())).thenReturn(jobMock);
        when(jobMock.isDone()).thenReturn(Boolean.TRUE);
        InputStream stream = ConsumerTest.class.getResourceAsStream("/splunk-response.xml");
        when(jobMock.getResults(anyMap())).thenReturn(stream);

        assertMockEndpointsSatisfied(20, TimeUnit.SECONDS);
        SplunkEvent recieved = searchMock.getReceivedExchanges().get(0).getIn().getBody(SplunkEvent.class);
        assertNotNull(recieved);
        Map<String, String> data = recieved.getEventData();
        assertEquals("name1", data.get("name"));
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("splunk://normal?delay=5s&username=foo&password=bar&initEarliestTime=-10s&latestTime=now&search=search index=myindex&sourceType=testSource")
                    .to("mock:search-result");
            }
        };
    }
}
