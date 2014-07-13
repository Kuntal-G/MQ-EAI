package com.mq.util;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Session;

public class CacheObject implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Connection  connection;
	private Session session;
	private Destination jmsQueue;
	
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public Destination getJmsQueue() {
		return jmsQueue;
	}
	public void setJmsQueue(Destination jmsQueue) {
		this.jmsQueue = jmsQueue;
	}
	
	
}
