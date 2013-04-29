package org.apache.camel.component.splunk.support;

import java.io.IOException;
import java.net.Socket;

import org.apache.camel.component.splunk.SplunkEndpoint;

import com.splunk.Args;
import com.splunk.Input;
import com.splunk.Service;

public class TcpDataWriter extends SplunkDataWriter {
    private int port;

    public TcpDataWriter(SplunkEndpoint endpoint, Args args) {
        super(endpoint, args);
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    protected Socket createSocket(Service service) throws IOException {
        Input input = service.getInputs().get(String.valueOf(port));
        if (input == null) {
            throw new RuntimeException("no input defined for port " + port);
        }
        if (input.isDisabled()) {
            throw new RuntimeException(String.format("input on port %d is disabled", port));
        }
        Socket socket = service.open(port);
        return socket;
    }
}
