package org.apache.camel.component.splunk;

import static org.mockito.Mockito.reset;

import java.net.Socket;

import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.splunk.Service;

public abstract class SplunkMockTestSupport extends CamelTestSupport {
    @Mock
    Service service;

    @Mock
    Socket socket;

    @Before
    @Override
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        reset(service);
        reset(socket);
        Helper.mockComponent(service, socket);
        super.setUp();
    }

}
