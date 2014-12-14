package com.general.mq.monitoring.impl;

import com.general.mq.management.QueueChannelFactory;
import com.general.mq.monitoring.IHealth;
import com.general.mq.monitoring.MonitorCollector;
import com.general.mq.monitoring.metric.QueueMetrics;

public class MonitorQueue implements IHealth{
	
	private static MonitorQueue singleton = null;
	
	
	public static MonitorQueue getInstance() {
		if (singleton!=null){return singleton;}
		synchronized (MonitorQueue.class.getName()) {
			if (singleton!=null){return singleton;}
			singleton = new MonitorQueue();
			}
		return singleton;
	}
	

	@Override
	public MonitorCollector<QueueMetrics> collect() {
		MonitorCollector<QueueMetrics> collector = new MonitorCollector<QueueMetrics>();
		QueueChannelFactory channelFactory = QueueChannelFactory.getChannelFactory();
		if(channelFactory!=null){
			QueueMetrics qMetrics =new QueueMetrics();
			qMetrics.setName("QUEUE INFO");
			qMetrics.setInUseConnSize(channelFactory.getInUseConnection());
			qMetrics.setUnAvailConnSize(channelFactory.getUnAvailConnection());
			qMetrics.setChnlPerConn(channelFactory.getTotalChannels());
			collector.addMetrics(qMetrics);			
		}
		return collector;

}
}
