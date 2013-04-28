camel-splunk 
============
This Apache Camel component can be used to publish and query events in Splunk.
see http://www.splunk.com
Some search features has been left out right now eg. saved search, but might be added later.

## Setup Instructions

## Clone Repository

*  git clone https://github.com/pax95/camel-splunk.git

## Build

*  navigate to root directory and run mvn clean install

## Install Splunk

*  Download from http://www.splunk.com/download

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
    <th>Name</th>
    <th>Default value</th>
    <th>Context</th>
    <th>Description</th>
  </tr>
  <tr>
    <th>host</th>
    <td>localhost</td>
    <td>Shared</td>
    <td>The Splunk host.</td>
  </tr>
  <tr>
    <th>port</th>
    <td>8089</td>
    <td>Shared</td>
    <td>Splunk port</td>
  </tr>
  <tr>
    <th>username</th>
    <td>null</td>
    <td>Shared</td>
    <td>Splunk username</td>
  </tr>
  <tr>
    <th>password</th>
    <td>null</td>
    <td>Shared</td>
    <td>Splunk password</td>
  </tr>
  <tr>
    <th>connectionTimeout</th>
    <td>5000</td>
    <td>Shared</td>
    <td>Timeout when connecting to Splunk server</td>
  </tr>
  <tr>
    <th>index</th>
    <td>null</td>
    <td>Producer</td>
    <td>Splunk index to write to</td>
  </tr>
  <tr>
    <th>sourceType</th>
    <td>null</td>
    <td>Producer</td>
    <td>Splunk SourceType arguement</td>
  </tr>
  <tr>
    <th>source</th>
    <td>null</td>
    <td>Producer</td>
    <td>Splunk Source arguement</td>
  </tr>
  <tr>
    <th>tcpRecieverPort</th>
    <td>0</td>
    <td>Producer</td>
    <td>Splunk tcp reciever port when using tcp connection</td>
  </tr>
  <tr>
    <th>initEarliestTime</th>
    <td>null</td>
    <td>Consumer</td>
    <td>Initial start offset of the first search. Required</td>
  </tr>
  <tr>
    <th>earliestTime</th>
    <td>null</td>
    <td>Consumer</td>
    <td>Earliest time of the time window.</td>
  </tr>
  <tr>
    <th>latestTime</th>
    <td>null</td>
    <td>Consumer</td>
    <td>Latest time of the time window</td>
  </tr>
   <tr>
    <th>maxRows</th>
    <td>0</td>
    <td>Consumer</td>
    <td>Max. number of rows to query splunk for when polling</td>
  </tr>
  <tr>
    <th>fieldList</th>
    <td>null</td>
    <td>Consumer</td>
    <td>Comma separated list of fields to return</td>
  </tr>
  <tr>
    <th>search</th>
    <td>null</td>
    <td>Consumer</td>
    <td>The Splunk query to run</td>
  </tr>
</table>
