package com.mq.util;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ConnectionUtil {
	public static Connection getConnection() throws JMSException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection connection = connectionFactory.createConnection();
		return connection;

	}
	
	public static Connection getProducerConnection() throws JMSException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616?jms.useAsyncSend=true");
		Connection connection = connectionFactory.createConnection();
		return connection;

	}

		public static Connection getBatchConsumerConnection() throws JMSException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616?jms.prefetchPolicy.queuePrefetch=0");
		Connection connection = connectionFactory.createConnection();
		return connection;

	}
		
		public static Connection getBlobConnection() throws JMSException {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616?jms.blobTransferPolicy.defaultUploadUrl=http://localhost:8161/fileserver/");
			Connection connection = connectionFactory.createConnection();
			return connection;

		}
		
}
