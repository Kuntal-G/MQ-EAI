package com.mq.util;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ConnectionUtil {
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

	public static Connection getConnection() throws JMSException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		//((ActiveMQConnectionFactory)connectionFactory).setUseAsyncSend(true);
		Connection connection = connectionFactory.createConnection();
		return connection;

	}

}
