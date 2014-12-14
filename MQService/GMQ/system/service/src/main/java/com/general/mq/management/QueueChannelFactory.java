package com.general.mq.management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import com.general.mq.common.logger.MQLogger;
import com.general.mq.common.util.conf.MQConfig;
import com.rabbitmq.client.ConnectionFactory;

public class QueueChannelFactory {
	
	
	private static List<QueueConnection> inUseQConnection = null;
	private static List<QueueConnection> unavailQConnection = null;
	private static QueueChannelFactory channelFactory = null;
	private QueueChannel qChannel=null;
	private static ConnectionFactory factory;
	
	private QueueChannelFactory(){
		
	}
	
	public static QueueChannelFactory getChannelFactory() {
		if (channelFactory == null) {
			synchronized (QueueChannelFactory.class) {
				if (channelFactory == null) {
					channelFactory = new QueueChannelFactory();
					inUseQConnection = new ArrayList<QueueConnection>();
					unavailQConnection = new ArrayList<QueueConnection>();
					factory = new ConnectionFactory();
					factory.setHost(MQConfig.MQ_HOST);
					try {
						QueueConnection qConnection = new QueueConnection(factory.newConnection());
						inUseQConnection.add(qConnection);
					}catch (IOException e) {
						MQLogger.l.error("RabbitMQ Channel Factory Error: "+e);
					}					
				}
			}
		}
		return channelFactory;
	}
	
	public QueueChannel getQueueChannel() {
		synchronized (channelFactory){
			QueueConnection qConnection = inUseQConnection.get(0);
			if(qConnection==null){
				try {
					qConnection = new QueueConnection(factory.newConnection());
					inUseQConnection.add(qConnection);
				} catch (IOException e) {
					MQLogger.l.error("RabbitMQ Channel Factory Error: "+e);
				}
			}
			
			qChannel = qConnection.getQueueChannel();
			if(qConnection.getChannelCount()==MQConfig.MQ_CHANNEL_PERCONN)	{
				unavailQConnection.add(qConnection);
				inUseQConnection.remove(0);			
			}	
		}

		return qChannel;
	}
	
	public void closeChannel()	{
		QueueConnection qConnection = null;
		for(Iterator<QueueConnection> itr = unavailQConnection.iterator();itr.hasNext();){
			qConnection = itr.next();
			if(qConnection.getChannelCount()<MQConfig.MQ_CHANNEL_PERCONN)	{
				inUseQConnection.add(qConnection);
				itr.remove();
			}
		}
	}
	
	public int getInUseConnection(){
		int totalConn = 0;
		totalConn = inUseQConnection.size();
		return totalConn;
	}
	
	public int getUnAvailConnection(){
		int totalUnAvailConn = 0;
		totalUnAvailConn = unavailQConnection.size();
		return totalUnAvailConn;
	}
	
	public int getTotalChannels()	{
		int totalChannels = 0;
		for(int i=0;i<inUseQConnection.size();i++)		{
			totalChannels = totalChannels + inUseQConnection.get(i).getChannelCount();
		}
		for(int j=0;j<unavailQConnection.size();j++)		{
			totalChannels = totalChannels + unavailQConnection.get(j).getChannelCount();
		}
		return totalChannels;
	}
	

}
