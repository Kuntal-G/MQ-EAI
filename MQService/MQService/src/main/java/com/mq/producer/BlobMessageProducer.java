package com.mq.producer;

import java.io.IOException;
import java.io.InputStream;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.BlobMessage;

import com.mq.util.ConnectionUtil;

public class BlobMessageProducer {

	public String postFile(InputStream blobStream,String fileName,Long size) throws JMSException,IOException{
		Connection qC = ConnectionUtil.getBlobConnection();
		qC.start();
	
		ActiveMQSession session = (ActiveMQSession)qC.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("blob-queue");
		MessageProducer producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		BlobMessage blobMessage = session.createBlobMessage(blobStream);
		blobMessage.setStringProperty("FILE.NAME", fileName);
		blobMessage.setLongProperty("FILE.SIZE", size);
		producer.send(blobMessage);
		session.close();
		qC.close();
		return "BLOB MSG successfully published";
		
	}
	
}
