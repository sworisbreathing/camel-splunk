package org.apache.camel.component.splunk.event;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@SuppressWarnings("serial")
public class SplunkEvent implements Serializable {

	/**
	 * Contents of the event message
	 */
	private StringBuffer eventMessage;

	/**
	 * Whether or not to put quotes around values
	 */
	protected boolean quoteValues = true;

	/**
	 * Whether or not to add a date to the event string
	 */
	protected boolean useInternalDate = true;

	/**
	 * default key value delimiter
	 */
	protected static final String KVDELIM = "=";
	/**
	 * default pair delimiter
	 */
	protected static final String PAIRDELIM = " ";
	/**
	 * default quote char
	 */
	protected static final char QUOTE = '"';
	/**
	 * default date format is using internal generated date
	 */
	protected static final String DATEFORMATPATTERN = "yyyy-MM-dd\tHH:mm:ss:SSSZ";
	/**
	 * Date Formatter
	 */
	protected static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern(DATEFORMATPATTERN);

	/**
	 * Event prefix fields
	 */
	protected static final String PREFIX_NAME = "name";
	protected static final String PREFIX_EVENT_ID = "event_id";

	/**
	 * Java Throwable type fields
	 */
	protected static final String THROWABLE_CLASS = "throwable_class";
	protected static final String THROWABLE_MESSAGE = "throwable_message";
	protected static final String THROWABLE_STACKTRACE_ELEMENTS = "stacktrace_elements";

	protected static final String LINEBREAK = "\n";

	// ----------------------------------
	// Common event fields
	// ----------------------------------

	/**
	 * A device-specific classification provided as part of the event.
	 */
	public static String COMMON_CATEGORY = "category";
	/**
	 * A device-specific classification provided as part of the event.
	 */
	public static String COMMON_COUNT = "count";
	/**
	 * The free-form description of a particular event.
	 */
	public static String COMMON_DESC = "desc";
	/**
	 * The name of a given DHCP pool on a DHCP server.
	 */
	public static String COMMON_DHCP_POOL = "dhcp_pool";
	/**
	 * The amount of time the event lasted.
	 */
	public static String COMMON_DURATION = "duration";
	/**
	 * The fully qualified domain name of the device transmitting or recording
	 * the log record.
	 */
	public static String COMMON_DVC_HOST = "dvc_host";
	/**
	 * The IPv4 address of the device reporting the event.
	 */
	public static String COMMON_DVC_IP = "dvc_ip";
	/**
	 * The IPv6 address of the device reporting the event.
	 */
	public static String COMMON_DVC_IP6 = "dvc_ip6";
	/**
	 * The free-form description of the device's physical location.
	 */
	public static String COMMON_DVC_LOCATION = "dvc_location";
	/**
	 * The MAC (layer 2) address of the device reporting the event.
	 */
	public static String COMMON_DVC_MAC = "dvc_mac";
	/**
	 * The Windows NT domain of the device recording or transmitting the event.
	 */
	public static String COMMON_DVC_NT_DOMAIN = "dvc_nt_domain";
	/**
	 * The Windows NT host name of the device recording or transmitting the
	 * event.
	 */
	public static String COMMON_DVC_NT_HOST = "dvc_nt_host";
	/**
	 * Time at which the device recorded the event.
	 */
	public static String COMMON_DVC_TIME = "dvc_time";
	/**
	 * The event's specified end time.
	 */
	public static String COMMON_END_TIME = "end_time";
	/**
	 * A unique identifier that identifies the event. This is unique to the
	 * reporting device.
	 */
	public static String COMMON_EVENT_ID = "event_id";
	/**
	 * The length of the datagram, event, message, or packet.
	 */
	public static String COMMON_LENGTH = "length";
	/**
	 * The log-level that was set on the device and recorded in the event.
	 */
	public static String COMMON_LOG_LEVEL = "log_level";
	/**
	 * The name of the event as reported by the device. The name should not
	 * contain information that's already being parsed into other fields from
	 * the event, such as IP addresses.
	 */
	public static String COMMON_NAME = "name";
	/**
	 * An integer assigned by the device operating system to the process
	 * creating the record.
	 */
	public static String COMMON_PID = "pid";
	/**
	 * An environment-specific assessment of the event's importance, based on
	 * elements such as event severity, business function of the affected
	 * system, or other locally defined variables.
	 */
	public static String COMMON_PRIORITY = "priority";
	/**
	 * The product that generated the event.
	 */
	public static String COMMON_PRODUCT = "product";
	/**
	 * The version of the product that generated the event.
	 */
	public static String COMMON_PRODUCT_VERSION = "product_version";
	/**
	 * The result root cause, such as connection refused, timeout, crash, and so
	 * on.
	 */
	public static String COMMON_REASON = "reason";
	/**
	 * The action result. Often is a binary choice: succeeded and failed,
	 * allowed and denied, and so on.
	 */
	public static String COMMON_RESULT = "result";
	/**
	 * The severity (or priority) of an event as reported by the originating
	 * device.
	 */
	public static String COMMON_SEVERITY = "severity";
	/**
	 * The event's specified start time.
	 */
	public static String COMMON_START_TIME = "start_time";
	/**
	 * The transaction identifier.
	 */
	public static String COMMON_TRANSACTION_ID = "transaction_id";
	/**
	 * A uniform record locator (a web address, in other words) included in a
	 * record.
	 */
	public static String COMMON_URL = "url";
	/**
	 * The vendor who made the product that generated the event.
	 */
	public static String COMMON_VENDOR = "vendor";
	// ----------------------------------
	// Update
	// ----------------------------------

	/**
	 * The name of the installed update.
	 */
	public static String UPDATE_PACKAGE = "package";

	/**
	 * A Constructor to load data from a Map
	 * 
	 * @param data
	 *            the map
	 */
	public SplunkEvent(Map<String, String> data) {
		this.eventMessage = new StringBuffer();
		for (String key : data.keySet()) {
			this.addPair(key, data.get(key));
		}
	}

	/**
	 * A Copy constructor
	 * 
	 * @param splunkEvent
	 */
	public SplunkEvent(SplunkEvent splunkEvent) {
		this.eventMessage = splunkEvent.eventMessage;
		this.quoteValues = splunkEvent.quoteValues;
		this.useInternalDate = splunkEvent.useInternalDate;
	}

	/**
	 * Constructor to create a generic event
	 * 
	 * @param eventName
	 *            the event name
	 * @param eventID
	 *            the event id
	 * @param useInternalDate
	 *            whether or not to add a date to the event string
	 * @param quoteValues
	 *            whether or not to put quotes around values
	 */
	public SplunkEvent(String eventName, String eventID, boolean useInternalDate, boolean quoteValues) {

		this.eventMessage = new StringBuffer();
		this.quoteValues = quoteValues;
		this.useInternalDate = useInternalDate;

		addPair(PREFIX_NAME, eventName);
		addPair(PREFIX_EVENT_ID, eventID);
	}

	/**
	 * Constructor to create a generic event with the default format
	 * 
	 * @param eventName
	 *            the event name
	 * @param eventID
	 *            the event ID
	 */
	public SplunkEvent(String eventName, String eventID) {

		this(eventName, eventID, true, true);
	}

	/**
	 * Default constructor
	 */
	public SplunkEvent() {
		this.eventMessage = new StringBuffer();
	}

	public Map<String, String> getEventData() {
		Map<String, String> eventData = new HashMap<String, String>();
		String eventEntries = eventMessage.toString();

		String[] entries = eventEntries.split(PAIRDELIM);

		String quote = new String(new char[] { QUOTE });

		for (String entry : entries) {
			String[] pair = entry.split(KVDELIM);

			if (pair.length != 2) {
				throw new UnsupportedOperationException(String.format("invalid event data [%s]", entry));
			}

			String key = pair[0].replaceAll(quote, "");
			String value = pair[1].replaceAll(quote, "");
			if ("null".equals(value)) {
				value = null;
			}

			eventData.put(key, value);
		}
		return eventData;
	}

	/**
	 * Add a key value pair
	 * 
	 * @param key
	 * @param value
	 */
	public void addPair(String key, char value) {
		addPair(key, String.valueOf(value));
	}

	/**
	 * Add a key value pair
	 * 
	 * @param key
	 * @param value
	 */
	public void addPair(String key, boolean value) {
		addPair(key, String.valueOf(value));
	}

	/**
	 * Add a key value pair
	 * 
	 * @param key
	 * @param value
	 */
	public void addPair(String key, double value) {
		addPair(key, String.valueOf(value));
	}

	/**
	 * Add a key value pair
	 * 
	 * @param key
	 * @param value
	 */
	public void addPair(String key, long value) {
		addPair(key, String.valueOf(value));
	}

	/**
	 * Add a key value pair
	 * 
	 * @param key
	 * @param value
	 */
	public void addPair(String key, int value) {
		addPair(key, String.valueOf(value));
	}

	/**
	 * Add a key value pair
	 * 
	 * @param key
	 * @param value
	 */
	public void addPair(String key, Object value) {
		addPair(key, value.toString());
	}

	/**
	 * Utility method for formatting Throwable,Error,Exception objects in a more
	 * linear and Splunk friendly manner than printStackTrace
	 * 
	 * @param throwable
	 *            the Throwable object to add to the event
	 */
	public void addThrowable(Throwable throwable) {

		addThrowableObject(throwable, -1);
	}

	/**
	 * Utility method for formatting Throwable,Error,Exception objects in a more
	 * linear and Splunk friendly manner than printStackTrace
	 * 
	 * @param throwable
	 *            the Throwable object to add to the event
	 * @param stackTraceDepth
	 *            maximum number of stacktrace elements to log
	 */
	public void addThrowable(Throwable throwable, int stackTraceDepth) {

		addThrowableObject(throwable, stackTraceDepth);
	}

	/**
	 * Internal private method for formatting Throwable,Error,Exception objects
	 * in a more linear and Splunk friendly manner than printStackTrace
	 * 
	 * @param throwable
	 *            the Throwable object to add to the event
	 * @param stackTraceDepth
	 *            maximum number of stacktrace elements to log, -1 for all
	 */

	private void addThrowableObject(Throwable throwable, int stackTraceDepth) {

		addPair(THROWABLE_CLASS, throwable.getClass().getCanonicalName());
		addPair(THROWABLE_MESSAGE, throwable.getMessage());
		StackTraceElement[] elements = throwable.getStackTrace();
		StringBuffer sb = new StringBuffer();
		int depth = 0;
		for (StackTraceElement element : elements) {
			depth++;
			if (stackTraceDepth == -1 || stackTraceDepth >= depth)
				sb.append(element.toString()).append(",");
			else
				break;

		}
		addPair(THROWABLE_STACKTRACE_ELEMENTS, sb.toString());
	}

	/**
	 * Add a key value pair
	 * 
	 * @param key
	 * @param value
	 */
	public void addPair(String key, String value) {
		if (quoteValues)
			this.eventMessage.append(key).append(KVDELIM).append(QUOTE).append(value).append(QUOTE).append(PAIRDELIM);
		else
			this.eventMessage.append(key).append(KVDELIM).append(value).append(PAIRDELIM);

	}

	@Override
	/**
	 * return the completed event message
	 */
	public String toString() {

		String event = "";

		if (useInternalDate) {
			StringBuffer clonedMessage = new StringBuffer();
			clonedMessage.append(DATE_FORMATTER.print(new Date().getTime())).append(PAIRDELIM)
					.append(this.eventMessage);
			event = clonedMessage.toString();
		} else
			event = eventMessage.toString();
		// trim off trailing pair delim char(s)
		String result = event.substring(0, event.length() - PAIRDELIM.length()) + LINEBREAK;
		return result;
	}

	public void setCommonCategory(String commonCategory) {
		addPair(COMMON_CATEGORY, commonCategory);
	}

	public void setCommonCount(String commonCount) {
		addPair(COMMON_COUNT, commonCount);
	}

	public void setCommonDesc(String commonDesc) {
		addPair(COMMON_DESC, commonDesc);
	}

	public void setCommonDhcpPool(String commonDhcpPool) {
		addPair(COMMON_DHCP_POOL, commonDhcpPool);
	}

	public void setCommonDuration(long commonDuration) {
		addPair(COMMON_DURATION, commonDuration);
	}

	public void setCommonDvcHost(String commonDvcHost) {
		addPair(COMMON_DVC_HOST, commonDvcHost);
	}

	public void setCommonDvcIp(String commonDvcIp) {
		addPair(COMMON_DVC_IP, commonDvcIp);
	}

	public void setCommonDvcIp6(String commonDvcIp6) {
		addPair(COMMON_DVC_IP6, commonDvcIp6);
	}

	public void setCommonDvcLocation(String commonDvcLocation) {
		addPair(COMMON_DVC_LOCATION, commonDvcLocation);
	}

	public void setCommonDvcMac(String commonDvcMac) {
		addPair(COMMON_DVC_MAC, commonDvcMac);
	}

	public void setCommonDvcNtDomain(String commonDvcNtDomain) {
		addPair(COMMON_DVC_NT_DOMAIN, commonDvcNtDomain);
	}

	public void setCommonDvcNtHost(String commonDvcNtHost) {
		addPair(COMMON_DVC_NT_HOST, commonDvcNtHost);
	}

	public void setCommonDvcTime(long commonDvcTime) {
		addPair(COMMON_DVC_TIME, commonDvcTime);
	}

	public void setCommonEndTime(long commonEndTime) {
		addPair(COMMON_END_TIME, commonEndTime);
	}

	public void setCommonEventId(long commonEventId) {
		addPair(COMMON_EVENT_ID, commonEventId);
	}

	public void setCommonLength(long commonLength) {
		addPair(COMMON_LENGTH, commonLength);
	}

	public void setCommonLogLevel(String commonLogLevel) {
		addPair(COMMON_LOG_LEVEL, commonLogLevel);
	}

	public void setCommonName(String commonName) {
		addPair(COMMON_NAME, commonName);
	}

	public void setCommonPid(long commonPid) {
		addPair(COMMON_PID, commonPid);
	}

	public void setCommonPriority(long commonPriority) {
		addPair(COMMON_PRIORITY, commonPriority);
	}

	public void setCommonProduct(String commonProduct) {
		addPair(COMMON_PRODUCT, commonProduct);
	}

	public void setCommonProductVersion(long commonProductVersion) {
		addPair(COMMON_PRODUCT_VERSION, commonProductVersion);
	}

	public void setCommonReason(String commonReason) {
		addPair(COMMON_REASON, commonReason);
	}

	public void setCommonResult(String commonResult) {
		addPair(COMMON_RESULT, commonResult);
	}

	public void setCommonSeverity(String commonSeverity) {
		addPair(COMMON_SEVERITY, commonSeverity);
	}

	public void setCommonStartTime(long commonStartTime) {
		addPair(COMMON_START_TIME, commonStartTime);
	}

	public void setCommonTransactionId(String commonTransactionId) {
		addPair(COMMON_TRANSACTION_ID, commonTransactionId);
	}

	public void setCommonUrl(String commonUrl) {
		addPair(COMMON_URL, commonUrl);
	}

	public void setCommonVendor(String commonVendor) {
		addPair(COMMON_VENDOR, commonVendor);
	}

	public void setUpdatePackage(String updatePackage) {
		addPair(UPDATE_PACKAGE, updatePackage);
	}

}
