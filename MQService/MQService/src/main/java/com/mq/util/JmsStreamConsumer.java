package com.mq.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.StreamMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

public class JmsStreamConsumer {
	private MessageConsumer consumer;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private static Logger logger = Logger.getLogger(JmsStreamConsumer.class);
	private BufferedOutputStream bos = null;

	private void init(String targetFileName) throws Exception {
		logger.info("--init start--");
		logger.info("--targetFileName--" + targetFileName);
		OutputStream out = new FileOutputStream(targetFileName);
		bos = new BufferedOutputStream(out);
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
				"tcp://localhost:61616");
		connection = factory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createQueue("queue1");
		consumer = session.createConsumer(destination);
		logger.info("--init end--");
	}

	public void receiveFile(String targetFileName) {
		logger.info("--receive file start--");
		try {
			init(targetFileName);
			byte[] buffer = new byte[2048];
			while (true) {
				Message msg = consumer.receive(5000);
				if (msg == null) {
					break;
				}

				if (msg instanceof StreamMessage) {
					StreamMessage smsg = (StreamMessage) msg;
					int c = smsg.readBytes(buffer);

					String tempStr = new String(buffer, 0, c);
					logger.info("Receive str: " + tempStr);
					bos.write((tempStr).getBytes());
				}
			}
			logger.info("--receive file end--");
		} catch (Exception e) {
			logger.error("--sendFile fail--", e);
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
			if (bos != null) {
				bos.close();
			}
			if (connection != null) {
				connection.close();
			}
			logger.info("--consumer close end--");
		} catch (IOException e) {
			logger.error("--close OutputStream fail--", e);
		} catch (JMSException e) {
			logger.error("--close connection fail--", e);
		}
		System.exit(0);
	}

	public static void main(String[] args) {
		new JmsStreamConsumer().receiveFile("E:\\receive.txt");
	}

}
