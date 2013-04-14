package org.apache.camel.component.splunk.consumer;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.component.splunk.SplunkEndpoint;
import org.apache.camel.component.splunk.event.SplunkEvent;
import org.apache.camel.util.ObjectHelper;
import org.apache.log4j.Logger;

import com.splunk.Args;
import com.splunk.Job;
import com.splunk.JobArgs.SearchMode;
import com.splunk.ResultsReader;
import com.splunk.ResultsReaderXml;
import com.splunk.SavedSearch;
import com.splunk.SavedSearchCollection;
import com.splunk.Service;

public class SplunkDataReader {
	private static final String DATE_FORMAT = "MM/dd/yy HH:mm:ss:SSS";

	private static final String SPLUNK_TIME_FORMAT = "%m/%d/%y %H:%M:%S:%3N";

	private static final Logger logger = Logger.getLogger(SplunkDataReader.class);



	private transient Calendar lastSuccessfulReadTime;
	
	private SplunkEndpoint endpoint;

	public SplunkDataReader(SplunkEndpoint endpoint) {
		this.endpoint = endpoint;
	}

	public SearchMode getSearchMode() {
		return endpoint.getConfiguration().getSearchMode();
	}

	public int getCount() {
		return endpoint.getConfiguration().getCount();
	}

	public String getFieldList() {
		return endpoint.getConfiguration().getFieldList();
	}

	public String getSearch() {
		return endpoint.getConfiguration().getSearch();
	}

	public String getEarliestTime() {
		return endpoint.getConfiguration().getEarliestTime();
	}

	public String getLatestTime() {
		return endpoint.getConfiguration().getLatestTime();
	}

	public String getSavedSearch() {
		return endpoint.getConfiguration().getSavedSearch();
	}


	public String getInitEarliestTime() {
		return endpoint.getConfiguration().getInitEarliestTime();
	}

	public List<SplunkEvent> read() throws Exception {
		logger.debug("mode:" + getSearchMode());
		switch (getSearchMode()) {
			case NORMAL: {
				return nonBlockingSearch();
			}
			case REALTIME: {
				return realtimeSearch();
			}
		}
		throw new RuntimeException("Unknown search mode " + getSearchMode());
	}

	/**
	 * Get the earliestTime of range search.
	 *
	 * @param startTime the time where search start
	 * @param realtime if this is realtime search
	 *
	 * @return The time of last successful read if not realtime;
	 *         Time difference between last successful read and start time;
	 */
	private String calculateEarliestTime(Calendar startTime, boolean realtime) {
		String result = null;
		if (realtime) {
			result = calculateEarliestTimeForRealTime(startTime);
		}
		DateFormat df = new SimpleDateFormat(DATE_FORMAT);
		result = df.format(lastSuccessfulReadTime.getTime());
		return result;
	}

	/**
	 * get earliest time for realtime search
	 *
	 * @param startTime
	 * @return
	 */
	private String calculateEarliestTimeForRealTime(Calendar startTime) {
		String result = null;
		long diff = startTime.getTimeInMillis() - lastSuccessfulReadTime.getTimeInMillis();
		result = "-" + diff / 1000 + "s";
		return result;
	}

	private void populateArgs(Args queryArgs, Calendar startTime, boolean realtime) {
		String earliestTime = getEarliestTime(startTime, realtime);
		if (ObjectHelper.isNotEmpty(earliestTime)) {
			queryArgs.put("earliest_time", earliestTime);
		}

		String latestTime = getLatestTime(startTime, realtime);
		if (ObjectHelper.isNotEmpty(latestTime)) {
			queryArgs.put("latest_time", latestTime);
		}

		queryArgs.put("time_format", SPLUNK_TIME_FORMAT);

		if (ObjectHelper.isNotEmpty(getFieldList())) {
			queryArgs.put("field_list", getFieldList());
		}
	}

	private String getLatestTime(Calendar startTime, boolean realtime) {
		String lTime = null;
		if (ObjectHelper.isNotEmpty(getLatestTime())) {
			lTime = getLatestTime();
		} else {
			if (realtime) {
				lTime = "rt";
			} else {
				DateFormat df = new SimpleDateFormat(DATE_FORMAT);
				lTime = df.format(startTime.getTime());
			}
		}
		return lTime;
	}

	private String getEarliestTime(Calendar startTime, boolean realtime) {
		String eTime = null;

		if (lastSuccessfulReadTime == null) {
			eTime = getInitEarliestTime();
		} else {
			if (ObjectHelper.isNotEmpty(getEarliestTime())) {
				eTime = getEarliestTime();
			} else {
				String calculatedEarliestTime = calculateEarliestTime(startTime, realtime);
				if (calculatedEarliestTime != null) {
					if (realtime) {
						eTime = "rt" + calculatedEarliestTime;
					} else {
						eTime = calculatedEarliestTime;
					}
				}
			}
		}
		return eTime;
	}

	private List<SplunkEvent> runQuery(Args queryArgs) throws Exception {
		Service service = endpoint.getService();
		Job job = service.getJobs().create(getSearch(), queryArgs);
		while (!job.isDone()) {
			Thread.sleep(2000);
		}
		return extractData(job);

	}


	private List<SplunkEvent> nonBlockingSearch() throws Exception {
		logger.debug("non block search start");

		Args queryArgs = new Args();
		queryArgs.put("exec_mode", "normal");
		Calendar startTime = Calendar.getInstance();
		populateArgs(queryArgs, startTime, false);

		List<SplunkEvent> data = runQuery(queryArgs);
		lastSuccessfulReadTime = startTime;
		return data;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	private List<SplunkEvent> realtimeSearch() throws Exception {
		logger.debug("realtime search start");

		Args queryArgs = new Args();
		queryArgs.put("search_mode", "realtime");
		Calendar startTime = Calendar.getInstance();
		populateArgs(queryArgs, startTime, true);

		List<SplunkEvent> data = runQuery(queryArgs);
		lastSuccessfulReadTime = startTime;
		return data;
	}

	/**
	 * @throws Exception
	 *
	 */
	private List<SplunkEvent> exportSearch() throws Exception {
		logger.debug("export start");
		List<SplunkEvent> result = new ArrayList<SplunkEvent>();
		HashMap<String, String> data;
		SplunkEvent splunkData;

		Args queryArgs = new Args();
		Calendar startTime = Calendar.getInstance();
		populateArgs(queryArgs, startTime, false);
		queryArgs.put("output_mode", "xml");

		Service service = endpoint.getService();
		InputStream os = service.export(getSearch(), queryArgs);
		ResultsReaderXml resultsReader = new ResultsReaderXml(os);
		while ((data = resultsReader.getNextEvent()) != null) {
			splunkData = new SplunkEvent(data);
			result.add(splunkData);
		}
		//TODO os and reader should be closed
		return result;
	}

	private List<SplunkEvent> savedSearch() throws Exception {
		logger.debug("saved search start");

		Args queryArgs = new Args();
		queryArgs.put("app", "search");
		if (ObjectHelper.isNotEmpty(endpoint.getConfiguration().getOwner())) {
			queryArgs.put("owner", endpoint.getConfiguration().getOwner());
		}
		if (ObjectHelper.isNotEmpty(endpoint.getConfiguration().getApp())) {
			queryArgs.put("app", endpoint.getConfiguration().getApp());
		}

		Calendar startTime = Calendar.getInstance();

			SavedSearch search = null;
			Job job = null;
			String latestTime = getLatestTime(startTime, false);
			String earliestTime = getEarliestTime(startTime, false);

			Service service = endpoint.getService();
			SavedSearchCollection savedSearches = service.getSavedSearches(queryArgs);
			for (SavedSearch s : savedSearches.values()) {
				if (s.getName().equals(getSavedSearch())) {
					search = s;
				}
			}
			if (search != null) {
				Map<String, String> args = new HashMap<String, String>();
				args.put("force_dispatch", "true");
				args.put("dispatch.earliest_time", earliestTime);
				args.put("dispatch.latest_time", latestTime);
				job = search.dispatch(args);
			}
			while (!job.isDone()) {
				Thread.sleep(2000);
			}
			List<SplunkEvent> data = extractData(job);
			this.lastSuccessfulReadTime = startTime;
			return data;

	}

	private List<SplunkEvent> extractData(Job job) throws Exception {
		List<SplunkEvent> result = new ArrayList<SplunkEvent>();
		HashMap<String, String> data;
		SplunkEvent splunkData;
		ResultsReader resultsReader;
		int total = job.getResultCount();

		if (getCount() == 0 || total < getCount()) {
			InputStream stream = null;
			Args outputArgs = new Args();
			outputArgs.put("output_mode", "xml");
			stream = job.getResults(outputArgs);

			resultsReader = new ResultsReaderXml(stream);
			while ((data = resultsReader.getNextEvent()) != null) {
				splunkData = new SplunkEvent(data);
				result.add(splunkData);
			}
		} else {
			int offset = 0;
			while (offset < total) {
				InputStream stream = null;
				Args outputArgs = new Args();
				outputArgs.put("output_mode", "xml");
				outputArgs.put("count", getCount());
				outputArgs.put("offset", offset);
				stream = job.getResults(outputArgs);
				resultsReader = new ResultsReaderXml(stream);
				while ((data = resultsReader.getNextEvent()) != null) {
					splunkData = new SplunkEvent(data);
					result.add(splunkData);
				}
				offset += getCount();
			}
		}
		return result;
	}

}
