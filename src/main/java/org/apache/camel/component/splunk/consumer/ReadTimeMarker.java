package org.apache.camel.component.splunk.consumer;

public interface ReadTimeMarker {
	String readLatestMark();
	void writeLatestMark(String marker);
}
