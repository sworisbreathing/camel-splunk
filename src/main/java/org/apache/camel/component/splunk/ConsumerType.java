package org.apache.camel.component.splunk;

public enum ConsumerType {
    NORMAL, SAVEDSEARCH, UNKNOWN;

    public static ConsumerType fromUri(String uri) {
        for (ConsumerType consumerType : ConsumerType.values()) {
            if (consumerType.name().equalsIgnoreCase(uri)) {
                return consumerType;
            }
        }
        return ConsumerType.UNKNOWN;
    }

}
