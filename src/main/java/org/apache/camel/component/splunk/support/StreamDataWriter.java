package org.apache.camel.component.splunk.support;

import java.io.IOException;
import java.net.Socket;

import org.apache.camel.RuntimeCamelException;
import org.apache.camel.component.splunk.SplunkEndpoint;

import com.splunk.Args;
import com.splunk.Index;
import com.splunk.Receiver;
import com.splunk.Service;

public class StreamDataWriter extends SplunkDataWriter {
    private String index;

    public StreamDataWriter(SplunkEndpoint endpoint, Args args) {
        super(endpoint, args);
    }

    public void setIndex(String index) {
        this.index = index;
    }

    @Override
    protected Socket createSocket(Service service) throws IOException {
        Index indexObject = null;
        Receiver receiver = null;
        Socket socket = null;

        if (index != null) {
            indexObject = service.getIndexes().get(index);
            if (indexObject == null) {
                throw new RuntimeCamelException(String.format("cannot find index [%s]", index));
            }
            socket = indexObject.attach(args);
        } else {
            receiver = service.getReceiver();
            socket = receiver.attach(args);
        }
        logger.trace(String.format("created a socket on %s", socket.getRemoteSocketAddress()));
        return socket;
    }

}
