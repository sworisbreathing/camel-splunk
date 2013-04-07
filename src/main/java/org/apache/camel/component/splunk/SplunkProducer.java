package org.apache.camel.component.splunk;

import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.component.splunk.SplunkConfiguration.WriterType;
import org.apache.camel.component.splunk.event.SplunkEvent;
import org.apache.camel.component.splunk.producer.SplunkDataWriter;
import org.apache.camel.component.splunk.producer.StreamDataWriter;
import org.apache.camel.component.splunk.producer.SubmitDataWriter;
import org.apache.camel.component.splunk.producer.TcpDataWriter;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Splunk producer.
 */
public class SplunkProducer extends DefaultProducer {
	private static final transient Logger LOG = LoggerFactory.getLogger(SplunkProducer.class);
	private SplunkEndpoint endpoint;
	private SplunkDataWriter dataWriter;

	public SplunkProducer(SplunkEndpoint endpoint) {
		super(endpoint);
		this.endpoint = endpoint;
		createWriter();
	}

	public void process(Exchange exchange) throws Exception {
		dataWriter.write(exchange.getIn().getMandatoryBody(SplunkEvent.class));
	}

	@Override
	protected void doStart() throws Exception {
		super.doStart();
		dataWriter.start();
	}

	@Override
	protected void doStop() throws Exception {
		dataWriter.stop();
		super.doStop();
	}

	private void createWriter() {
		WriterType writerType = endpoint.getConfiguration().getWriterType();
		switch (writerType) {
		case tcp: {
			LOG.info("Creating TcpDataWriter");
			dataWriter = new TcpDataWriter(endpoint.getService(), endpoint.buildSplunkArgs());
			((TcpDataWriter) dataWriter).setPort(endpoint.getConfiguration().getTcpRecieverPort());
			LOG.info("TcpDataWriter created for endpoint " + endpoint);
			break;
		}
		case submit: {
			LOG.info("Creating SubmitDataWriter");
			dataWriter = new SubmitDataWriter(endpoint.getService(), endpoint.buildSplunkArgs());
			((SubmitDataWriter) dataWriter).setIndex(endpoint.getConfiguration().getIndex());
			LOG.info("SubmitDataWriter created for endpoint " + endpoint);
			break;
		}
		case stream: {
			LOG.info("Creating StreamDataWriter");
			dataWriter = new StreamDataWriter(endpoint.getService(), endpoint.buildSplunkArgs());
			((StreamDataWriter) dataWriter).setIndexName(endpoint.getConfiguration().getIndex());
			LOG.info("StreamDataWriter created for endpoint " + endpoint);
			break;
		}
		default:
			throw new RuntimeCamelException("Unknown writerType : " + writerType);
		}
	}

}
