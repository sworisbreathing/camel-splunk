package org.apache.camel.component.splunk.producer;

import java.io.IOException;
import java.net.Socket;

import org.apache.camel.RuntimeCamelException;

import com.splunk.Args;
import com.splunk.Index;
import com.splunk.Receiver;
import com.splunk.Service;

public class StreamDataWriter extends SplunkDataWriter {

	private String indexName;

	public StreamDataWriter(Service service, Args args) {
		super(service, args);
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	@Override
	protected Socket createSocket(Service service) throws IOException {
		Index indexObject = null;
		Receiver receiver = null;
		Socket socket = null;

		if (indexName != null) {
			indexObject = service.getIndexes().get(indexName);
			if (indexObject == null) {
				throw new RuntimeCamelException(String.format("cannot find index [%s]", indexName));
			}
			socket = indexObject.attach(args);
		} else {
			receiver = service.getReceiver();
			socket = receiver.attach(args);
		}
		logger.debug(String.format("created a socket on %s", socket.getRemoteSocketAddress()));
		return socket;
	}

}
