package com.mq.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.BlobMessage;
import org.apache.log4j.Logger;

public class BlobMessageConsumer {
	private MessageConsumer consumer;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private static Logger logger = Logger.getLogger(BlobMessageConsumer.class);
	private BufferedOutputStream bos;

	private void init() throws Exception {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"tcp://localhost:61616");
		connection = connectionFactory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createQueue("File.Transport");
		consumer = session.createConsumer(destination);
	}

	public void receiveFile(String targetFilePath) {
		try {
			init();
			while (true) {
				Message message = consumer.receive(5000);
				if (message == null) {
					break;
				}

				if (message instanceof BlobMessage) {
					byte[] buffer = new byte[2048];
					int length = 0;
					BlobMessage blobMessage = (BlobMessage) message;
					String fileName = blobMessage
							.getStringProperty("FILE.NAME");
					logger.info("received fileName: " + fileName
							+ ", the size of file: "
							+ blobMessage.getLongProperty("FILE.SIZE"));

					File file = new File(targetFilePath + File.separator
							+ fileName);
					OutputStream os = new FileOutputStream(file);
					bos = new BufferedOutputStream(os);

					InputStream inputStream = blobMessage.getInputStream();
					while ((length = inputStream.read(buffer)) > 0) {
						bos.write(buffer, 0, length);
					}
				}
			}
		} catch (Exception e) {
			logger.error("--receive fail--", e);
		} finally {
			close();
		}
	}

	private void close() {
		logger.info("--consumer close start--");
		try {
			if (bos != null) {
				bos.close();
			}
			if (connection != null) {
				connection.close();
			}
			logger.info("--consumer close end--");
		} catch (IOException e) {
			logger.error("--close BufferedOutputStream fail--", e);
		} catch (JMSException e) {
			logger.error("--close connection fail--", e);
		}
		System.exit(0);
	}

	public static void main(String[] args) {
		String targetFileFolder = "I:\\test\\target";
		new BlobMessageConsumer().receiveFile(targetFileFolder);
	}
}