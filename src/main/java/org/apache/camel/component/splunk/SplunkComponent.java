package org.apache.camel.component.splunk;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.ScheduledPollEndpoint;

/**
 * Represents the component that manages {@link SplunkEndpoint}.
 */
public class SplunkComponent extends DefaultComponent {
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        SplunkConfiguration configuration = new SplunkConfiguration();
        setProperties(configuration, parameters);

        Endpoint endpoint = new SplunkEndpoint(uri, this, configuration);
        ((ScheduledPollEndpoint)endpoint).setConsumerProperties(parameters);
        return endpoint;
    }
}
