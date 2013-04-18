camel-splunk 
============
This Apache Camel component can be used to publish events to Splunk see http://www.splunk.com
Consumer for querying Splunk is not supported right now.

## Setup Instructions

## Clone Repository

*  git clone https://github.com/pax95/camel-splunk.git

## Build

*  navigate to root directory and run mvn clean install

## Install Splunk

*  Download from http://www.splunk.com/download

Uri format:
===========
	splunk://somename[?options]

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
    <th>writerType</th>
    <td>stream</td>
    <td>Producer</td>
    <td>Splunk writer type either stream, tcp or submit</td>
  </tr>
  <tr>
    <th>searchMode</th>
    <td>normal</td>
    <td>Consumer</td>
    <td>Splunk search mode either normal or realtime</td>
  </tr>
   <tr>
    <th>maxRows</th>
    <td>0</td>
    <td>Consumer</td>
    <td>Max. number of rows to return pr. poll</td>
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
  <tr>
    <th>earliestTime</th>
    <td>null</td>
    <td>Consumer</td>
    <td>Earliest time of the time window</td>
  </tr>
  <tr>
    <th>latestTime</th>
    <td>null</td>
    <td>Consumer</td>
    <td>Latest time of the time window</td>
  </tr>
  <tr>
    <th>initEarliestTime</th>
    <td>null</td>
    <td>Consumer</td>
    <td>Initial start offset of the first search</td>
  </tr>
</table>