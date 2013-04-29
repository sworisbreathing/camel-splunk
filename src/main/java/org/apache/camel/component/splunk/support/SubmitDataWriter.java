package org.apache.camel.component.splunk.support;

import java.io.IOException;
import java.net.Socket;

import org.apache.camel.component.splunk.SplunkEndpoint;
import org.apache.camel.component.splunk.event.SplunkEvent;

import com.splunk.Args;
import com.splunk.Index;
import com.splunk.Receiver;
import com.splunk.Service;

public class SubmitDataWriter extends SplunkDataWriter {
    private String index;

    public SubmitDataWriter(SplunkEndpoint endpoint, Args args) {
        super(endpoint, args);
    }

    protected void doWrite(SplunkEvent event, Socket socket) throws IOException {
        Index index = getIndex();
        if (index != null) {
            index.submit(args, event.toString());
        } else {
            Receiver receiver = endpoint.getService().getReceiver();
            receiver.submit(args, event.toString());
        }
    };

    @Override
    protected Socket createSocket(Service service) throws IOException {
        return null;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    private Index getIndex() {
        return (index == null) ? null : endpoint.getService().getIndexes().get(index);
    }

}
