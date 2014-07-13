package com.mq.consumer;

import java.io.IOException;
import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQQueue;

import com.mq.ehcache.MQCache;
import com.mq.rest.MessageFormat;
import com.mq.util.CacheObject;
import com.mq.util.ConnectionUtil;
import com.mq.util.PropertyUtil;


public class Consumer {
	
 
    public MessageFormat getConsumeMsg(String clientId)	throws Exception {
		MessageFormat msgFormat = new MessageFormat();
		msgFormat.setMessage("NO Message in MQ");
		Connection qC = ConnectionUtil.getConnection();
		qC.start();
		Destination destination = null;
		Session session = qC.createSession(true,-1);
		if (clientId.equalsIgnoreCase("100")) {
			destination = session.createQueue("pwp-queue");
		} else if (clientId.equalsIgnoreCase("101")) {
			destination = session.createQueue("pwc-queue");
		} else if (clientId.equalsIgnoreCase("102")) {
			destination = session.createQueue("iship-queue");
		}else if (clientId.equalsIgnoreCase("103")) {
			destination = session.createQueue("solr-queue");
		}else if (clientId.equalsIgnoreCase("104")) {
			destination = session.createQueue("kettle-queue");
		}else if (clientId.equalsIgnoreCase("105")) {
			destination = session.createQueue("abacus-queue");
		}else if (clientId.equalsIgnoreCase("106")) {
			destination = session.createQueue("bigdata-queue");
		}else{
			destination = session.createQueue("test-queue");
		}
		MessageConsumer consumer = session.createConsumer(destination);
		Message message = consumer.receive();
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			msgFormat.setMessageID(textMessage.getJMSMessageID());
			msgFormat.setMessage(textMessage.getText());
			
			CacheObject cacheValue = new CacheObject();
			cacheValue.setConnection(qC);
			cacheValue.setSession(session);
			cacheValue.setJmsQueue(destination);
			
			MQCache.instance().add(textMessage.getJMSMessageID(), cacheValue);
			//CacheAdaptor.instance().loadCache(textMessage.getJMSMessageID(), cacheValue);
			return msgFormat;
		}		
		consumer.close();
		return msgFormat;
	}

    public String getMsgCount(String clientId)	throws JMSException, IOException {
  		String numMsg="NONE FOUND";
    	Connection qC = ConnectionUtil.getConnection();
  		qC.start();
  		Session session = qC.createSession(false,Session.AUTO_ACKNOWLEDGE);
  		ActiveMQQueue queue=new ActiveMQQueue();
  		if (clientId.equalsIgnoreCase("100")) {
  			queue.setPhysicalName("pwp-queue");
  		} else if (clientId.equalsIgnoreCase("101")) {
  			queue.setPhysicalName("pwc-queue");
  		} else if (clientId.equalsIgnoreCase("102")) {
  			queue.setPhysicalName("iship-queue");
  		}
		QueueBrowser queueBrowser = session.createBrowser(queue);
		Enumeration e = queueBrowser.getEnumeration();
		int numMsgs = 0;
		while (e.hasMoreElements()) {
			Message message = (Message) e.nextElement();
			numMsgs++;
		}
		numMsg = "FOUND : " + numMsgs;
		queueBrowser.close();
		session.close();
		qC.close();
		return numMsg;
	}
    
    public String getACK(String clientId,String msgId,String ack)throws JMSException{
    	
    	if(MQCache.instance().get(msgId)!=null){
      	Connection connection = MQCache.instance().get(msgId).getConnection();
      	Session session = MQCache.instance().get(msgId).getSession();
		MessageConsumer consumer = session.createConsumer(MQCache.instance().get(msgId).getJmsQueue());
		if (ack.equalsIgnoreCase("SUCCESS")) {
			Message message = consumer.receive();
			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				PropertyUtil.writeToFile(clientId, textMessage.getJMSMessageID(), textMessage.getText());
			}
			session.commit();
		} else {
			session.rollback();
		}		
		session.close();
		connection.close();
		MQCache.instance().evictCache(msgId);
		return "Accepted";
    	}else{
    		return "Rejected";
    	}
    	
    }
}


