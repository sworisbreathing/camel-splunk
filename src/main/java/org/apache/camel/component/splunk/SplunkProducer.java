/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    protected DataWriter getDataWriter() {
        return dataWriter;
    }
}
