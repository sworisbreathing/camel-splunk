package org.apache.camel.component.splunk.support;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import org.apache.camel.component.splunk.SplunkEndpoint;
import org.apache.camel.component.splunk.event.SplunkEvent;
import org.apache.log4j.Logger;

import com.splunk.Args;
import com.splunk.Service;

public abstract class SplunkDataWriter implements DataWriter {
    protected final Logger logger = Logger.getLogger(getClass());

    protected Socket socket;
    protected SplunkEndpoint endpoint;
    protected Args args;

    public SplunkDataWriter(SplunkEndpoint endpoint, Args args) {
        this.endpoint = endpoint;
        this.args = args;
    }

    protected abstract Socket createSocket(Service service) throws IOException;

    public void write(SplunkEvent event) throws Exception {
        logger.debug("writing event to splunk:" + event);
        doWrite(event, socket);
    }

    protected void doWrite(SplunkEvent event, Socket socket) throws IOException {
        OutputStream ostream = socket.getOutputStream();
        Writer writer = new OutputStreamWriter(ostream, "UTF8");
        writer.write(event.toString());
        writer.flush();
    }

    public Args getArgs() {
        return args;
    }

    @Override
    public synchronized void start() {
        try {
            socket = createSocket(endpoint.getService());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void stop() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
