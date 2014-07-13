package com.mq.util;

import java.io.File;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.BlobMessage;
import org.apache.log4j.Logger;

public class BlobMessageProducer {
	private Connection connection = null;
	private ActiveMQSession session = null;
	private Destination destination = null;
	private MessageProducer producer = null;
	private File file;

	private static Logger logger = Logger.getLogger(BlobMessageProducer.class);

	private void init(String fileName) throws Exception {
		file = new File(fileName);
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"tcp://localhost:61616?jms.blobTransferPolicy.defaultUploadUrl=http://localhost:8161/fileserver/");
		connection = connectionFactory.createConnection();
		session = (ActiveMQSession) connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		destination = session.createQueue("File.Transport");
		producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		connection.start();
	}

	public void sendFile(String fileName) {
		try {
			logger.info("--sendFile start--");
			init(fileName);
			BlobMessage blobMessage = session.createBlobMessage(file);
			blobMessage.setStringProperty("FILE.NAME", file.getName());
			blobMessage.setLongProperty("FILE.SIZE", file.length());
			producer.send(blobMessage);
			logger.info("--sendFile end--");
		} catch (Exception e) {
			logger.error("--sendFile fail--", e);
		} finally {
			close();
		}
	}

	private void close() {
		logger.info("--producer close start--");
		try {
			if (connection != null) {
				connection.close();
			}
			logger.info("--producer close end--");
		} catch (JMSException e) {
			logger.error("--close connection fail--", e);
		}
		System.exit(0);
	}

	public static void main(String argv[]) {
		String fileName = "I:\\test\\source\\netty.pdf";
		new BlobMessageProducer().sendFile(fileName);
	}

}
