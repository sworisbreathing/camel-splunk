package org.apache.camel.component.splunk;


public enum ProducerType {
    TCP, SUBMIT, STREAM, UNKNOWN;

    public static ProducerType fromUri(String uri) {
        for (ProducerType producerType : ProducerType.values()) {
            if (producerType.name().equalsIgnoreCase(uri)) {
                return producerType;
            }
        }
        return ProducerType.UNKNOWN;
    }
}
