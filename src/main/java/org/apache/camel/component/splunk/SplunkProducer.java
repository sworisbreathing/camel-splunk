package org.apache.camel.component.splunk;

import org.apache.camel.Exchange;
import org.apache.camel.component.splunk.event.SplunkEvent;
import org.apache.camel.component.splunk.support.DataWriter;
import org.apache.camel.component.splunk.support.StreamDataWriter;
import org.apache.camel.component.splunk.support.SubmitDataWriter;
import org.apache.camel.component.splunk.support.TcpDataWriter;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.splunk.Args;
import com.splunk.HttpException;

/**
 * The Splunk producer.
 */
public class SplunkProducer extends DefaultProducer {
    private static final transient Logger LOG = LoggerFactory.getLogger(SplunkProducer.class);
    private SplunkEndpoint endpoint;
    private DataWriter dataWriter;

    public SplunkProducer(SplunkEndpoint endpoint, ProducerType producerType) {
        super(endpoint);
        this.endpoint = endpoint;
        createWriter(producerType);
    }

    public void process(Exchange exchange) throws Exception {
        try {
            dataWriter.write(exchange.getIn().getMandatoryBody(SplunkEvent.class));
        } catch (HttpException e) {
            if (e.getStatus() == 401) {
                log.info("Got response 401 (call not properly authenticated) from Splunk. Trying to reconnect");
                endpoint.reconnect();
                dataWriter.start();
            } else {
                throw e;
            }
        }
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

    private void createWriter(ProducerType producerType) {
        switch (producerType) {
        case TCP: {
            LOG.info("Creating TcpDataWriter");
            dataWriter = new TcpDataWriter(endpoint, buildSplunkArgs());
            ((TcpDataWriter)dataWriter).setPort(endpoint.getConfiguration().getTcpRecieverPort());
            LOG.info("TcpDataWriter created for endpoint " + endpoint);
            break;
        }
        case SUBMIT: {
            LOG.info("Creating SubmitDataWriter");
            dataWriter = new SubmitDataWriter(endpoint, buildSplunkArgs());
            ((SubmitDataWriter)dataWriter).setIndex(endpoint.getConfiguration().getIndex());
            LOG.info("SubmitDataWriter created for endpoint " + endpoint);
            break;
        }
        case STREAM: {
            LOG.info("Creating StreamDataWriter");
            dataWriter = new StreamDataWriter(endpoint, buildSplunkArgs());
            ((StreamDataWriter)dataWriter).setIndex(endpoint.getConfiguration().getIndex());
            LOG.info("StreamDataWriter created for endpoint " + endpoint);
            break;
        }
        case UNKNOWN: {
            throw new RuntimeException("unknown producerType");
        }
        }
    }

    private Args buildSplunkArgs() {
        Args args = new Args();
        if (endpoint.getConfiguration().getSourceType() != null) {
            args.put("sourcetype", endpoint.getConfiguration().getSourceType());
        }
        if (endpoint.getConfiguration().getSource() != null) {
            args.put("source", endpoint.getConfiguration().getSource());
        }
        return args;
    }

}
