package org.apache.camel.component.splunk;

import org.apache.camel.component.splunk.event.SplunkEvent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;

public abstract class SplunkTest extends CamelTestSupport {
    // TEST WILL RUN ON SPLUNK DEFAULT LOCALHOST+PORT
    // the Splunk username/pw created when Splunk was initialized during your
    // login.

    protected final String SPLUNK_USERNAME = "admin";
    protected final String SPLUNK_PASSWORD = "preben1212";
    // should be created in splunk before test run;
    protected final String INDEX = "junit";

    @Before
    public void init() throws Exception {
        SplunkEvent splunkEvent = new SplunkEvent();
        splunkEvent.addPair("key1", "value1");
        splunkEvent.addPair("key2", "value2");
        splunkEvent.addPair("key3", "value3");
        template.sendBody("direct:submit", splunkEvent);
    }

}
