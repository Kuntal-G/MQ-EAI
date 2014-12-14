package com.general.mq.rest;

import java.util.List;
import java.util.Properties;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.exception.SystemException;
import com.general.mq.rest.ConsumerResource;
import com.general.mq.rest.rqrsp.AcknowledgeRequest;
import com.general.mq.rest.rqrsp.AcknowledgeResponse;
import com.general.mq.rest.rqrsp.ConsumerRequest;
import com.general.mq.rest.rqrsp.ConsumerResponse;
import com.general.mq.rest.rqrsp.MessageResponse;
import com.general.mq.test.util.FileUtils;

public class ConsumerResourceTest{

	private final ConsumerResource consumer=new ConsumerResource();
	private ConsumerRequest conReq;
	private ConsumerResponse conResp;


	// Register Consumer
	public void registerConsumerDefaultQ() throws SystemException, ApplicationException {
		Properties consumerProps = FileUtils.getAllProperty("DefaultConsumer.properties");
		Properties producerProps = FileUtils.getAllProperty("DefaultProducer.properties");
		Properties queueProps = FileUtils.getAllProperty("DefaultQueue.properties");
		String queueName = queueProps.getProperty("queueName");
		String routingKey=null;
		if(producerProps.getProperty("routingKey")!=null){
			routingKey = producerProps.getProperty("routingKey");
		}else if(queueProps.getProperty("routingKey")!=null){
			routingKey = queueProps.getProperty("routingKey");
		}		
		long processingTime = 5;
		int timeUnit = 3;
		if(consumerProps.getProperty("processingTime")!=null)
			processingTime = Long.parseLong(consumerProps.getProperty("processingTime"));
		if(consumerProps.getProperty("timeUnit")!=null)
			timeUnit = Integer.parseInt(consumerProps.getProperty("timeUnit"));
		conReq=new ConsumerRequest();
		conReq.setQueueName(queueName);
		conReq.setRoutingKey(routingKey);
		conReq.setProcessTime(processingTime);
		conReq.setTimeUnit(timeUnit);	
		conResp=consumer.registerConsumer(conReq);
		if(conResp!=null){
			consumerProps.setProperty("consumerId", conResp.getClientId());
			FileUtils.storeProperty("DefaultConsumer.properties", consumerProps);
		}
	}

	public void registerConsumerDelayQ() throws SystemException, ApplicationException {
		Properties consumerProps = FileUtils.getAllProperty("ConsumerDelay.properties");
		Properties producerProps = FileUtils.getAllProperty("ProducerDelay.properties");
		Properties queueProps = FileUtils.getAllProperty("DelayQueue.properties");
		String queueName = queueProps.getProperty("queueName");String routingKey=null;
		if(producerProps.getProperty("routingKey")!=null){
			routingKey = producerProps.getProperty("routingKey");
		}else if(queueProps.getProperty("routingKey")!=null){
			routingKey = queueProps.getProperty("routingKey");
		}	
		long processingTime = 5;
		int timeUnit = 3;
		if(consumerProps.getProperty("processingTime")!=null)
			processingTime = Long.parseLong(consumerProps.getProperty("processingTime"));
		if(consumerProps.getProperty("timeUnit")!=null)
			timeUnit = Integer.parseInt(consumerProps.getProperty("timeUnit"));
		conReq=new ConsumerRequest();
		conReq.setQueueName(queueName);
		conReq.setRoutingKey(routingKey);
		conReq.setProcessTime(processingTime);
		conReq.setTimeUnit(timeUnit);	
		conResp=consumer.registerConsumer(conReq);
		if(conResp!=null){
			consumerProps.setProperty("consumerId", conResp.getClientId());
			FileUtils.storeProperty("ConsumerDelay.properties", consumerProps);
		}
	}

	public void registerConsumerWithNoRK() throws SystemException, ApplicationException {
		Properties consumerProps = FileUtils.getAllProperty("consumer.properties");
		Properties queueProps = FileUtils.getAllProperty("DefaultQueue.properties");
		String queueName = queueProps.getProperty("queueName");
		String routingKey = queueProps.getProperty("routingKey");
		long processingTime = 5;
		int timeUnit = 3;
		if(consumerProps.getProperty("processingTime")!=null)
			processingTime = Long.parseLong(consumerProps.getProperty("processingTime"));
		if(consumerProps.getProperty("timeUnit")!=null)
			timeUnit = Integer.parseInt(consumerProps.getProperty("timeUnit"));
		conReq=new ConsumerRequest();
		conReq.setQueueName(queueName);
		conReq.setRoutingKey(routingKey);
		conReq.setProcessTime(processingTime);
		conReq.setTimeUnit(timeUnit);	
		conResp=consumer.registerConsumer(conReq);
		if(conResp!=null){
			consumerProps.setProperty("consumerId", conResp.getClientId());
			FileUtils.storeProperty("consumer.properties", consumerProps);
		}
	}
	
		


	/*******************Messages Consume***********
	 * @throws ApplicationException 
	 * @throws SystemException 
	 * ********************************************/
	public List<MessageResponse> getMessages(int batchSize,String propertyFile) throws SystemException, ApplicationException 
	{
		Properties consumerProps = FileUtils.getAllProperty(propertyFile);
		String clientId = (String)consumerProps.get("consumerId");
		ConsumerResource cResource=new ConsumerResource();
		ConsumerResponse conResp = cResource.consumeMsg(clientId,batchSize);
		//System.out.println("*************** MsgCount is: **********:: "+conResp.getMsgCount());
		List<MessageResponse> msgList = conResp.getMsgs();
		return msgList;

	}

	/*************** Acknowledgement Request ***********
	 * @throws ApplicationException 
	 * @throws SystemException 
	 * *************************************************/

	public AcknowledgeResponse sendAck(AcknowledgeRequest ackRequest) throws SystemException, ApplicationException{
		ConsumerResource cResource=new ConsumerResource();
		AcknowledgeResponse ackResp = cResource.msgAcknowledge(ackRequest);
		return ackResp;

	}

	public void updateMsgProcessTime(ConsumerRequest conReq2) throws SystemException, ApplicationException {
		ConsumerResource cResource=new ConsumerResource();
		cResource.updateMsgProcessingTime(conReq2);		
	}



}
