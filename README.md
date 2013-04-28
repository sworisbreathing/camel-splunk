camel-splunk 
============
This Apache Camel component can be used to publish and query events in Splunk.
see http://www.splunk.com

## Setup Instructions

## Clone Repository

*  git clone https://github.com/pax95/camel-splunk.git

## Build

*  navigate to root directory and run mvn clean install

## Install Splunk

*  Download from http://www.splunk.com/download

## Example project

* https://github.com/pax95/camel-splunk-example

Uri format:
===========
	splunk://[endpoint]?[options]

Producer endpoints:
===================
<table>
  <thead>
    <th>Endpoint</th>
    <th>Description</th>
  </thead>
  <tr>
    <td>stream</td>
    <td>Splunk stream mode.</td>
  </tr>
  <tr>
    <td>submit</td>
    <td>Splunk submit mode.</td>
  </tr>
  <tr>
    <td>tcp</td>
    <td>Splunk tcp mode. Requires a open receiver port in Splunk.</td>
  </tr>
</table>


Consumer endpoints:
===================
<table>
  <thead>
    <th>Endpoint</th>
    <th>Description</th>
  </thead>
  <tr>
    <td>normal</td>
    <td>Performs normal search and requires a search query in the search option.</td>
  </tr>
  <tr>
    <td>realtime</td>
    <td>Performs realtime search in Splunk and requires a search query in the search option.</td>
  </tr>
  <tr>
    <td>savedsearch</td>
    <td>Performs search based on a search query saved in splunk and requires the name of the query in the savedSearch option.</td>
  </tr>
</table>


URI options:
============
<table>
  <tr>
    <td>Name</td>
    <th>Default value</th>
    <th>Context</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>host</td>
    <td>localhost</td>
    <td>Shared</td>
    <td>The Splunk host.</td>
  </tr>
  <tr>
    <td>port</td>
    <td>8089</td>
    <td>Shared</td>
    <td>Splunk port</td>
  </tr>
  <tr>
    <td>username</td>
    <td>null</td>
    <td>Shared</td>
    <td>Splunk username</td>
  </tr>
  <tr>
    <td>password</td>
    <td>null</td>
    <td>Shared</td>
    <td>Splunk password</td>
  </tr>
  <tr>
    <td>connectionTimeout</td>
    <td>5000</td>
    <td>Shared</td>
    <td>Timeout when connecting to Splunk server</td>
  </tr>
  <tr>
    <td>index</td>
    <td>null</td>
    <td>Producer</td>
    <td>Splunk index to write to</td>
  </tr>
  <tr>
    <td>sourceType</td>
    <td>null</td>
    <td>Producer</td>
    <td>Splunk SourceType arguement</td>
  </tr>
  <tr>
    <td>source</td>
    <td>null</td>
    <td>Producer</td>
    <td>Splunk Source arguement</td>
  </tr>
  <tr>
    <td>tcpRecieverPort</td>
    <td>0</td>
    <td>Producer</td>
    <td>Splunk tcp reciever port when using tcp connection</td>
  </tr>
  <tr>
    <td>initEarliestTime</td>
    <td>null</td>
    <td>Consumer</td>
    <td>Initial start offset of the first search. Required</td>
  </tr>
  <tr>
    <td>earliestTime</td>
    <td>null</td>
    <td>Consumer</td>
    <td>Earliest time of the time window.</td>
  </tr>
  <tr>
    <td>latestTime</td>
    <td>null</td>
    <td>Consumer</td>
    <td>Latest time of the time window</td>
  </tr>
   <tr>
    <td>maxRows</td>
    <td>0</td>
    <td>Consumer</td>
    <td>Max. number of rows to query splunk for when polling</td>
  </tr>
  <tr>
    <td>fieldList</td>
    <td>null</td>
    <td>Consumer</td>
    <td>Comma separated list of fields to return</td>
  </tr>
  <tr>
    <td>search</td>
    <td>null</td>
    <td>Consumer</td>
    <td>The Splunk query to run</td>
  </tr>
</table>

## Producer example
```java
from("direct:start")
.to("splunk://submit?username=user&password=123&index=myindex&sourceType=someSourceType&source=mySource");
```

## Consumer example

```java
from("splunk://realtime?delay=5s&username=user&password=123&initEarliestTime=rt-10s&search=search index=myindex sourcetype=someSourcetype")
.to("direct:search-result");
```
