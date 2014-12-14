package com.general.mq.management;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import com.general.mq.common.logger.MQLogger;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class QueueChannel {

	private QueueConnection qConnection;
	private Channel channel = null;
	public ReentrantLock lock = new ReentrantLock();
	
	public QueueChannel(QueueConnection qConn)	{
		qConnection = qConn;
		Connection conn = qConnection.getConnection();
		try {
			channel = conn.createChannel();
		} catch (IOException e) {
			MQLogger.l.error("RabbitMQ Queue Channel Error: "+e);
		}
	}
	
	public Channel getChannel()	{
		return channel;
	}
	
	public void closeChannel()	{
		try {
			if(channel!=null){
				channel.close();
			}
			qConnection.closeChannel();
		} catch (IOException e) {
			MQLogger.l.error("RabbitMQ Queue Channel Error: "+e);
		}
	}
}
