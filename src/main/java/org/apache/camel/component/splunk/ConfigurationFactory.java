package org.apache.camel.component.splunk;

import java.util.Map;

import org.apache.camel.util.ObjectHelper;

public class ConfigurationFactory {
    public static final ConfigurationFactory DEFAULT = new ConfigurationFactory();

    public SplunkConfiguration parseMap(Map<String, Object> parameters) {
        String host = (String)parameters.get("host");
        String port = (String)parameters.get("port");
        String username = (String)parameters.get("username");
        String password = (String)parameters.get("password");
        if (ObjectHelper.isEmpty(username) || ObjectHelper.isEmpty(password)) {
            throw new IllegalArgumentException("Username and password has to be specified");
        }
        if (ObjectHelper.isNotEmpty(host) && ObjectHelper.isNotEmpty(port)) {
            return new SplunkConfiguration(host, Integer.valueOf(port).intValue(), username, password);
        }
        return new SplunkConfiguration(username, password);
    }

}
