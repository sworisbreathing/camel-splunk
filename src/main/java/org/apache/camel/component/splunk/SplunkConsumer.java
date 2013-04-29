package org.apache.camel.component.splunk;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.splunk.event.SplunkEvent;
import org.apache.camel.component.splunk.support.SplunkDataReader;
import org.apache.camel.impl.ScheduledBatchPollingConsumer;
import org.apache.camel.util.CastUtils;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.splunk.HttpException;

/**
 * The Splunk consumer.
 */
public class SplunkConsumer extends ScheduledBatchPollingConsumer {
    private final static Logger LOG = LoggerFactory.getLogger(SplunkConsumer.class);
    private SplunkDataReader dataReader;
    private SplunkEndpoint endpoint;

    public SplunkConsumer(SplunkEndpoint endpoint, Processor processor, ConsumerType consumerType) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        dataReader = new SplunkDataReader(endpoint, consumerType);
    }

    @Override
    protected int poll() throws Exception {
        try {
            List<SplunkEvent> events = dataReader.read();
            Queue<Exchange> exchanges = createExchanges(events);
            return processBatch(CastUtils.cast(exchanges));
        } catch (HttpException e) {
            if (e.getStatus() == 401) {
                log.info("Got response 401 (call not properly authenticated) from Splunk. Trying to reconnect");
                endpoint.reconnect();
                return 0;
            } else {
                throw e;
            }
        }
    }

    protected Queue<Exchange> createExchanges(List<SplunkEvent> splunkEvents) {
        LOG.trace("Received {} messages in this poll", splunkEvents.size());
        Queue<Exchange> answer = new LinkedList<Exchange>();
        for (SplunkEvent splunkEvent : splunkEvents) {
            Exchange exchange = getEndpoint().createExchange();
            Message message = exchange.getIn();
            message.setBody(splunkEvent);
            answer.add(exchange);
        }
        return answer;
    }

    @Override
    public int processBatch(Queue<Object> exchanges) throws Exception {
        int total = exchanges.size();

        for (int index = 0; index < total && isBatchAllowed(); index++) {
            Exchange exchange = ObjectHelper.cast(Exchange.class, exchanges.poll());
            exchange.setProperty(Exchange.BATCH_INDEX, index);
            exchange.setProperty(Exchange.BATCH_SIZE, total);
            exchange.setProperty(Exchange.BATCH_COMPLETE, index == total - 1);
            try {
                LOG.trace("Processing exchange [{}]...", exchange);
                getProcessor().process(exchange);
            } finally {
                if (exchange.getException() != null) {
                    getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
                }
            }
        }
        return total;
    }
}
