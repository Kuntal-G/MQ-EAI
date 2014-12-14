package com.general.mq.management;

import com.general.mq.common.util.conf.MQConfig;
import com.rabbitmq.client.Connection;

public class QueueConnection {

	private Connection connection;
	private int channelCount;
	private int totalChannelCount = MQConfig.MQ_CHANNEL_PERCONN;
	private QueueChannel qChannel = null;
	
	public QueueConnection(Connection conn) {
		connection = conn;
	}
	
	public QueueConnection(Connection conn, int totalChannels) {
		connection = conn;
		totalChannelCount = totalChannels;
	}
	
	public QueueChannel getQueueChannel(){ 		
		synchronized (this) {
			if (channelCount < totalChannelCount) {
				qChannel = new QueueChannel(this);
				channelCount++;
				
			}
		}
		return qChannel;
	}
	

	
	public synchronized void closeChannel()	{
		synchronized (this) {
				channelCount--;
				if(channelCount<totalChannelCount){
					QueueChannelFactory channelFactory = QueueChannelFactory.getChannelFactory();
					channelFactory.closeChannel();
				}
			}
		
	}

	public int getChannelCount() {
		return channelCount;
	}

	public void setTotalChannelCount(int totalChannelCount) {
		this.totalChannelCount = totalChannelCount;
	}

	public Connection getConnection() {
		return connection;
	}
	
	public boolean canCreateChannel()	{
		return (channelCount<totalChannelCount);
	}
}