package com.mq.consumer;

import java.io.IOException;
import java.io.InputStream;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.BlobMessage;

import com.mq.util.ConnectionUtil;

public class BlobMessageConsumer {

	public InputStream receiveFile() throws JMSException,IOException {
		InputStream inputStream=null;
		Connection qC = ConnectionUtil.getConnection();
		qC.start();
		Session session = qC.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("blob-queue");
		MessageConsumer consumer = session.createConsumer(destination);
		while (true) {
			Message message = consumer.receive(5000);
			if (null == message) {
				break;
			}

			if (message instanceof BlobMessage) {
				BlobMessage blobMessage = (BlobMessage) message;
				inputStream = blobMessage.getInputStream();
				
			}
		}
		return inputStream;

	}

	
}