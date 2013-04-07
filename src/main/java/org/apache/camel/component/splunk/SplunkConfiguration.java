package org.apache.camel.component.splunk;

import com.splunk.Service;

public class SplunkConfiguration {
	public static enum WriterType {
		stream, tcp, submit
	}

	private String host = Service.DEFAULT_HOST;
	private int port = Service.DEFAULT_PORT;
	private String scheme = Service.DEFAULT_SCHEME;
	private String app;
	private String owner;
	private String username;
	private String password;
	private int timeout;
	private String index;
	private String sourceType;
	private String source;
	private WriterType writerType = WriterType.stream;

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getIndex() {
		return index;
	}

	public void setWriterType(WriterType writerType) {
		this.writerType = writerType;
	}

	public WriterType getWriterType() {
		return writerType;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
