package org.apache.camel.component.splunk;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.ScheduledPollEndpoint;

/**
 * Represents the component that manages {@link SplunkEndpoint}.
 */
public class SplunkComponent extends DefaultComponent {
    static ConfigurationFactory connFactory = ConfigurationFactory.DEFAULT;

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        SplunkConfiguration configuration = connFactory.parseMap(parameters);
        setProperties(configuration, parameters);

        Endpoint endpoint = new SplunkEndpoint(uri, this, configuration);
        ((ScheduledPollEndpoint)endpoint).setConsumerProperties(parameters);
        return endpoint;
    }

    public static void setConnFactory(ConfigurationFactory connFactory) {
        SplunkComponent.connFactory = connFactory;
    }
}
