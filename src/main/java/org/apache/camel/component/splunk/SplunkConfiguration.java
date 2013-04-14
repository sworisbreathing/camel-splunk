package org.apache.camel.component.splunk;

import com.splunk.JobArgs.SearchMode;
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
	private int connectionTimeout = 5000;
	private String index;
	private String sourceType;
	private String source;
	private int tcpRecieverPort;
	private WriterType writerType = WriterType.stream;
	
	//consumer properties
	private SearchMode searchMode;
	private int count = 0;
	private String fieldList;
	private String search;
	private String earliestTime;
	private String latestTime;
	private String initEarliestTime;
	private String savedSearch;

	public String getInitEarliestTime() {
		return initEarliestTime;
	}
	
	public void setInitEarliestTime(String initEarliestTime) {
		this.initEarliestTime = initEarliestTime;
	}
	
	public SearchMode getSearchMode() {
		return searchMode;
	}

	public void setSearchMode(SearchMode searchMode) {
		this.searchMode = searchMode;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getFieldList() {
		return fieldList;
	}

	public void setFieldList(String fieldList) {
		this.fieldList = fieldList;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getEarliestTime() {
		return earliestTime;
	}

	public void setEarliestTime(String earliestTime) {
		this.earliestTime = earliestTime;
	}

	public String getLatestTime() {
		return latestTime;
	}

	public void setLatestTime(String latestTime) {
		this.latestTime = latestTime;
	}

	public String getSavedSearch() {
		return savedSearch;
	}

	public void setSavedSearch(String savedSearch) {
		this.savedSearch = savedSearch;
	}

	public int getTcpRecieverPort() {
		return tcpRecieverPort;
	}

	public void setTcpRecieverPort(int tcpRecieverPort) {
		this.tcpRecieverPort = tcpRecieverPort;
	}

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

	public int getConnectionTimeout() {
		return connectionTimeout;
	} 

	public void setConnectionTimeout(int timeout) {
		this.connectionTimeout = timeout;
	}
}
