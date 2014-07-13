package com.mq.producer;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.mq.rest.MessageFormat;
import com.mq.util.ConnectionUtil;


public class Producer {
    
	public String getPublishMsg(String clientId, String msg, Integer priority)	throws JMSException, IOException {
		Connection qC = ConnectionUtil.getConnection();
		qC.start();
		Destination destination = null;
		Integer default_priority = 4;
		Session session = qC.createSession(false, Session.AUTO_ACKNOWLEDGE);
		if (clientId.equalsIgnoreCase("100")) {
			destination = session.createQueue("pwp-queue");
		} else if (clientId.equalsIgnoreCase("101")) {
			destination = session.createQueue("pwc-queue");
		} else if (clientId.equalsIgnoreCase("102")) {
			destination = session.createQueue("iship-queue");
		}
		MessageProducer producer = session.createProducer(destination);
		TextMessage message = session.createTextMessage(msg);
		if (priority != null) {
			producer.send(message, DeliveryMode.PERSISTENT, priority, 0);
		} else {
			producer.send(message, DeliveryMode.PERSISTENT, default_priority, 0);
		}
		session.close();
		qC.close();
		return "MSG successfully published";
	}
	
	public String postPublishMsg(MessageFormat msgFormat)	throws JMSException, IOException {
		Connection qC = ConnectionUtil.getConnection();
		qC.start();
		Destination destination = null;
		Integer default_priority = 4;
		Session session = qC.createSession(false, Session.AUTO_ACKNOWLEDGE);
		if (msgFormat.getClientId().equalsIgnoreCase("100")) {
			destination = session.createQueue("pwp-queue");
		} else if (msgFormat.getClientId().equalsIgnoreCase("101")) {
			destination = session.createQueue("pwc-queue");
		} else if (msgFormat.getClientId().equalsIgnoreCase("102")) {
			destination = session.createQueue("iship-queue");
		}
		MessageProducer producer = session.createProducer(destination);
		TextMessage message = session.createTextMessage(msgFormat.getMessage());
		if (msgFormat.getPriority() != null) {
			producer.send(message, DeliveryMode.PERSISTENT, msgFormat.getPriority(), 0);
		} else {
			producer.send(message, DeliveryMode.PERSISTENT, default_priority, 0);
		}
		session.close();
		qC.close();
		return "MSG successfully published";
	}

}
