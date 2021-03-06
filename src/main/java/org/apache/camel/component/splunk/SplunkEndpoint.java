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

import java.util.regex.Pattern;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.ScheduledPollEndpoint;

import com.splunk.Service;

/**
 * Represents a Splunk endpoint.
 */
public class SplunkEndpoint extends ScheduledPollEndpoint {
    private SplunkConfiguration configuration;
    private Service service;

    public SplunkEndpoint() {
    }

    public SplunkEndpoint(String uri, SplunkComponent component, SplunkConfiguration configuration) {
        super(uri, component);
        this.configuration = configuration;
    }

    public Producer createProducer() throws Exception {
        String[] uriSplit = splitUri(getEndpointUri());
        if (uriSplit.length > 0) {
            ProducerType producerType = ProducerType.fromUri(uriSplit[0]);
            return new SplunkProducer(this, producerType);
        }
        throw new IllegalArgumentException("Cannot create any producer with uri " + getEndpointUri() + ". A producer type was not provided (or an incorrect pairing was used).");
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        if (configuration.getInitEarliestTime() == null) {
            throw new IllegalArgumentException("Required initialEarliestTime option could not be found");
        }
        String[] uriSplit = splitUri(getEndpointUri());
        if (uriSplit.length > 0) {
            ConsumerType consumerType = ConsumerType.fromUri(uriSplit[0]);
            SplunkConsumer consumer = new SplunkConsumer(this, processor, consumerType);
            configureConsumer(consumer);
            return consumer;
        }
        throw new IllegalArgumentException("Cannot create any consumer with uri " + getEndpointUri() + ". A consumer type was not provided (or an incorrect pairing was used).");
    }

    public boolean isSingleton() {
        return true;
    }

    @Override
    protected void doStop() throws Exception {
        service = null;
        super.doStop();
    }

    public Service getService() {
        if (service == null) {
            this.service = configuration.createService();
        }
        return service;
    }

    private static String[] splitUri(String uri) {
        Pattern p1 = Pattern.compile("splunk:(//)*");
        Pattern p2 = Pattern.compile("\\?.*");

        uri = p1.matcher(uri).replaceAll("");
        uri = p2.matcher(uri).replaceAll("");

        return uri.split("/");
    }

    public SplunkConfiguration getConfiguration() {
        return configuration;
    }

    public synchronized void reconnect() {
        this.service = null;
        getService();
    }
}
