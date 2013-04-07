package org.apache.camel.component.splunk.producer;

import java.io.IOException;
import java.net.Socket;

import org.apache.camel.component.splunk.event.SplunkEvent;

import com.splunk.Args;
import com.splunk.Index;
import com.splunk.Receiver;
import com.splunk.Service;

public class SubmitDataWriter extends SplunkDataWriter {
	private String index;

	public SubmitDataWriter(Service service, Args args) {
		super(service, args);
	}

	protected void doWrite(SplunkEvent event, Socket socket) throws IOException {
		Index index = getIndex();
		if (index != null) {
			index.submit(args, event.toString());
		} else {
			Receiver receiver = service.getReceiver();
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
		return (index == null) ? null : service.getIndexes().get(index);
	}

}
