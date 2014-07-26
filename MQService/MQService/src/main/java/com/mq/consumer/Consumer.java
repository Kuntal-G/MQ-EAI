package com.mq.consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQQueue;

import com.mq.rest.BatchMessageFormat;
import com.mq.rest.MessageFormat;
import com.mq.util.ConnectionUtil;



public class Consumer {
	
 
	public BatchMessageFormat getConsumeMsg(final String clientId, final Integer batchSize)	throws Exception {
		BatchMessageFormat batchmsgFormat = new BatchMessageFormat();
		List<MessageFormat> msgdetails = new ArrayList<MessageFormat>();
		List<Future<MessageFormat>> futuremsgdetails = new ArrayList<Future<MessageFormat>>();
		  
		if (batchSize != null) {
			Integer msgCount = getMsgCount(clientId, batchSize);
			for (int batchconnect = 0; batchconnect <msgCount; batchconnect++) {
				FutureTask<MessageFormat> task = new FutureTask<MessageFormat>(new Callable<MessageFormat>() {
					@Override
					public MessageFormat call() throws Exception {
						MessageFormat msg=consumeBatchMsg(clientId,batchSize);
						return msg;
						
					}
				});
				
				futuremsgdetails.add(task);
				Thread t = new Thread(task);
				t.start();
			}	
			
			for(Future<MessageFormat> msg:futuremsgdetails){
				if(msg.get()!=null && msg.get().getMessageID()!=null){
				msgdetails.add(msg.get());
				}else{
					MessageFormat dummyMsg= new MessageFormat();
					dummyMsg.setMessageID("000000");
					dummyMsg.setMessage("No Message in Queue");
					msgdetails.add(dummyMsg);
					break;
				}
			}
			batchmsgFormat.setMsgDetails(msgdetails);
				
		}else{
			MessageFormat msgFormat = new MessageFormat();
			msgFormat.setMessage("NO Message in MQ");
			Connection qC = ConnectionUtil.getConnection();
			qC.start();
			Session session = qC.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = createQueue(clientId, session);
			MessageConsumer consumer = session.createConsumer(destination);

			Message message = consumer.receive(2000);
			if (message!=null || message instanceof TextMessage)  {
				TextMessage textMessage = (TextMessage) message;
				msgFormat.setMessageID(textMessage.getJMSMessageID());
				msgFormat.setMessage(textMessage.getText());
				msgdetails.add(msgFormat);
			
				
				
			}else{
				MessageFormat dummyMsg= new MessageFormat();
				dummyMsg.setMessageID("000000");
				dummyMsg.setMessage("No Message in Queue");
				msgdetails.add(dummyMsg);
			
			}
			batchmsgFormat.setMsgDetails(msgdetails);
			consumer.close();
		}
		
		return batchmsgFormat;
	}

    public  Integer getMsgCount(final String clientId,final Integer batchMsgCount)	throws JMSException, IOException {
  		Connection qC = ConnectionUtil.getConnection();
  		qC.start();
  		Session session = qC.createSession(false,Session.AUTO_ACKNOWLEDGE);
  		ActiveMQQueue queue=checkQueue(clientId);
  		QueueBrowser queueBrowser = session.createBrowser(queue);
		Enumeration<?> e = queueBrowser.getEnumeration();
		int numMsgs = 0;
		while (e.hasMoreElements()) {
			numMsgs++;
			if (batchMsgCount != null && numMsgs == batchMsgCount) {
				break;
			}
		}
		queueBrowser.close();
		session.close();
		qC.close();
		return numMsgs;
	}
    
	

  private  MessageFormat consumeBatchMsg(final String clientId, final Integer batchSize) throws JMSException, IOException{
		MessageFormat msgFormat= new MessageFormat();
		Connection qC = ConnectionUtil.getBatchConsumerConnection();
		qC.start();
		Session session = qC.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = createQueue(clientId, session);
		MessageConsumer consumer = session.createConsumer(destination);

		Message message = consumer.receive(2000);
		if (message!=null || message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			msgFormat.setMessageID(textMessage.getJMSMessageID());
			msgFormat.setMessage(textMessage.getText());
		}
		consumer.close();						

	return msgFormat;
    }
    
   
  
  public Destination createQueue(final String clientId,final Session session) throws JMSException{
       	Destination destination = session.createQueue(clientId+"-queue");
		return destination;	
     }
    
 private  ActiveMQQueue checkQueue(final String clientId) throws JMSException{
    	ActiveMQQueue queue = new ActiveMQQueue();
		queue.setPhysicalName(clientId+"-queue");
		return queue;	
     }
 
 
}


