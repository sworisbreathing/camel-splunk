package org.apache.camel.component.splunk.producer;

import org.apache.camel.component.splunk.event.SplunkEvent;


public interface DataWriter {
	void write(SplunkEvent data) throws Exception;

	void stop();

	void start();
}
