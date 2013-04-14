package org.apache.camel.component.splunk;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

import com.splunk.Args;
import com.splunk.Service;

/**
 * Represents a Splunk endpoint.
 */
public class SplunkEndpoint extends DefaultEndpoint {
	private SplunkConfiguration configuration;
	private Service service;

	public SplunkEndpoint() {
	}

	public SplunkEndpoint(String uri, SplunkComponent component, SplunkConfiguration configuration) {
		super(uri, component);
		this.configuration = configuration;
	}

	public Producer createProducer() throws Exception {
		return new SplunkProducer(this);
	}

	public Consumer createConsumer(Processor processor) throws Exception {
		return new SplunkConsumer(this, processor);
	}

	public boolean isSingleton() {
		return true;
	}

	@Override
	protected void doStart() throws Exception {
		super.doStart();
		service = getService();
	}

	@Override
	protected void doStop() throws Exception {
		service = null;
		super.doStop();
	}

	public Service getService() {
		if (service == null) {
			createService();
		}
		return service;
	}

	private void createService() {
		final Map<String, Object> args = new HashMap<String, Object>();
		if (configuration.getHost() != null) {
			args.put("host", configuration.getHost());
		}
		if (configuration.getPort() != 0) {
			args.put("port", configuration.getPort());
		}
		if (configuration.getScheme() != null) {
			args.put("scheme", configuration.getScheme());
		}
		if (configuration.getApp() != null) {
			args.put("app", configuration.getApp());
		}
		if (configuration.getOwner() != null) {
			args.put("owner", configuration.getOwner());
		}

		args.put("username", configuration.getUsername());
		args.put("password", configuration.getPassword());
		ExecutorService executor = Executors.newSingleThreadExecutor();

		Future<Service> future = executor.submit(new Callable<Service>() {
			public Service call() throws Exception {
				return Service.connect(args);
			}
		}); 

		try {
			if (configuration.getConnectionTimeout() > 0) {
				service = future.get(configuration.getConnectionTimeout(), TimeUnit.MILLISECONDS);
			} else {
				service = future.get();
			}
		} catch (Exception e) {
			throw new RuntimeException(String.format("could not connect to Splunk Server @ %s:%d - %s",
					configuration.getHost(), configuration.getPort(), e.getMessage()));
		}
	}

	public Args buildSplunkArgs() {
		Args args = new Args();
		if (configuration.getSourceType() != null) {
			args.put("sourcetype", configuration.getSourceType());
		}
		if (configuration.getSource() != null) {
			args.put("source", configuration.getSource());
		}
		return args;
	}

	public SplunkConfiguration getConfiguration() {
		return configuration;
	}
}
