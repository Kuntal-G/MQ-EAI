package com.mq.producer;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.mq.rest.PostFormat;
import com.mq.util.ConnectionUtil;


public class Producer {
    
	public String getPublishMsg(String clientId, String msg, Integer priority)	throws JMSException, IOException {
		Connection qC = ConnectionUtil.getConnection();
		qC.start();
		Integer default_priority = 4;
		Session session = qC.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue(clientId+"-queue");
		MessageProducer producer = session.createProducer(destination);
		TextMessage message = session.createTextMessage(msg);
		if (null!= priority) {
			producer.send(message, DeliveryMode.PERSISTENT, priority, 0);
		} else {
			producer.send(message, DeliveryMode.PERSISTENT, default_priority, 0);
		}
		session.close();
		qC.close();
		return "MSG successfully published";
	}
	
	public String postPublishMsg(PostFormat msgFormat)	throws JMSException, IOException {
		Connection qC = ConnectionUtil.getConnection();
		qC.start();
		Integer default_priority = 4;
		Session session = qC.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue(msgFormat.getClientId()+"-queue");
		MessageProducer producer = session.createProducer(destination);
		TextMessage message = session.createTextMessage(msgFormat.getMessage());
		if (null!= msgFormat.getPriority() ) {
			producer.send(message, DeliveryMode.PERSISTENT, msgFormat.getPriority(), 0);
		} else {
			producer.send(message, DeliveryMode.PERSISTENT, default_priority, 0);
		}
		session.close();
		qC.close();
		return "MSG successfully published";
	}

}
