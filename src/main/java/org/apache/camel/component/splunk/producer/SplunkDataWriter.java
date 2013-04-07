package org.apache.camel.component.splunk.producer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import org.apache.camel.component.splunk.event.SplunkEvent;
import org.apache.log4j.Logger;

import com.splunk.Args;
import com.splunk.Service;

public abstract class SplunkDataWriter implements DataWriter {
	protected final Logger logger = Logger.getLogger(getClass());

	protected Socket socket;
	protected Service service;
	protected Args args;

	public SplunkDataWriter(Service service, Args args) {
		this.service = service;
		this.args = args;
	}

	protected abstract Socket createSocket(Service service) throws IOException;

	public void write(SplunkEvent event) throws Exception {
		logger.debug("writing event to splunk:" + event);
		doWrite(event, socket);
	}

	protected void doWrite(SplunkEvent event, Socket socket) throws IOException {
		OutputStream ostream = socket.getOutputStream();
		Writer writer = new OutputStreamWriter(ostream, "UTF-8");
		writer.write(event.toString());
		writer.flush();
	}

	public Args getArgs() {
		return args;
	}

	public synchronized void start() {
		try {
			socket = createSocket(service);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

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
