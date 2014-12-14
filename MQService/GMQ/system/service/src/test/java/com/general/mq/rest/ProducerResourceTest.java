package com.general.mq.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.exception.SystemException;
import com.general.mq.rest.ProducerResource;
import com.general.mq.rest.rqrsp.ProducerRequest;
import com.general.mq.rest.rqrsp.ProducerResponse;
import com.general.mq.test.util.FileUtils;

public class ProducerResourceTest {
	//common
	private final ProducerResource producer=new ProducerResource();
	private  List<String> messages;
	private ProducerRequest proReq;
	private ProducerResponse proResp;


	//for publish
	private final String message1="{\"employees\":[{\"firstName\":\"John\", \"lastName\":\"Doe\"},	{\"firstName\":\"Anna\", \"lastName\":\"Smith\"},	{\"firstName\":\"Peter\", \"lastName\":\"Jones\"}]}";
	private final String message2="<employees><employee><firstName>John</firstName><lastName>Doe</lastName></employee><employee><firstName>Anna</firstName><lastName>Smith</lastName></employee><employee><firstName>Peter</firstName><lastName>Jones</lastName></employee></employees>";
	private final String message3="This is simple string 	message";
	private final String attributeName="test AttributeName";
	private final String attributeValue="test attributeValue";
	private final String parentId="test ParentId";
	private int highPriority=20;



	// Register Producer
	public ProducerResponse registerProducerDefaultQ() throws SystemException, ApplicationException{
		Properties queueProps=FileUtils.getAllProperty("DefaultQueue.properties");
		Properties prodProps=FileUtils.getAllProperty("DefaultProducer.properties");
		String routingKey;
		proReq=new ProducerRequest();
		proReq.setQueueName(queueProps.getProperty("queueName"));
		if(prodProps.getProperty("routingKey")!=null){
			routingKey=prodProps.getProperty("routingKey");
			proReq.setRoutingKey(routingKey);
		}else if(queueProps.getProperty("routingKey")!=null){
			routingKey=queueProps.getProperty("routingKey");
			proReq.setRoutingKey(routingKey);
		}else{
			routingKey="Default";
			proReq.setRoutingKey(routingKey);
		}	
		proResp=producer.registerProducer(proReq);			
		prodProps.setProperty("routingKey", routingKey);
		prodProps.setProperty("producerId", proResp.getClientId());
		FileUtils.storeProperty("DefaultProducer.properties", prodProps);
		return proResp;
	}
	public ProducerResponse registerProducertoDelayQ() throws ApplicationException{
		Properties queueProps=FileUtils.getAllProperty("DelayQueue.properties");
		Properties prodProps=FileUtils.getAllProperty("ProducerDelay.properties");
		proReq=new ProducerRequest();
		String routingKey;
		proReq.setQueueName(queueProps.getProperty("queueName"));
		if(prodProps.getProperty("routingKey")!=null){
			routingKey=prodProps.getProperty("routingKey");
			proReq.setRoutingKey(routingKey);
		}else if(queueProps.getProperty("routingKey")!=null){
			routingKey=queueProps.getProperty("routingKey");
			proReq.setRoutingKey(routingKey);
		}else{
			routingKey="Default";
			proReq.setRoutingKey(routingKey);
		}	
		proResp=producer.registerProducer(proReq);			
		prodProps.setProperty("routingKey", routingKey);
		prodProps.setProperty("producerId", proResp.getClientId());
		FileUtils.storeProperty("ProducerDelay.properties", prodProps);
		return proResp;

	}


	public ProducerResponse registerProducerWithNoRK(){
		Properties queueProps=FileUtils.getAllProperty("DefaultQueue.properties");
		Properties prodProps=FileUtils.getAllProperty("producer.properties");
		String routingKey;
		proReq=new ProducerRequest();
		proReq.setQueueName(queueProps.getProperty("queueName"));
		if(prodProps.getProperty("routingKey")!=null){
			routingKey=prodProps.getProperty("routingKey");
			proReq.setRoutingKey(routingKey);
		}else{
			routingKey=queueProps.getProperty("routingKey");
			proReq.setRoutingKey(routingKey);
		}

		try {
			proResp=producer.registerProducer(proReq);			
			prodProps.setProperty("routingKey", routingKey);
			prodProps.setProperty("clientId", proResp.getClientId());
			FileUtils.storeProperty("producer.properties", prodProps);
		}catch (ApplicationException e) {

		}
		return proResp;
	}


	// Publish Message Section

	public ProducerResponse publisgSingleMessage(String propertyFile){
		Properties prodProps=FileUtils.getAllProperty(propertyFile);
		messages= new ArrayList<String>();
		messages.add(message1);
		proReq=new ProducerRequest();
		proReq.setClientId(prodProps.getProperty("producerId"));
		proReq.setMessages(messages);
		try {
			proResp=producer.publishMessage(proReq);
		} catch (SystemException | ApplicationException e) {
			e.printStackTrace();
		}

		return proResp;
	}

	public ProducerResponse publisgBatchMessage(String propertyFile){
		Properties prodProps=FileUtils.getAllProperty(propertyFile);
		messages= new ArrayList<String>();
		messages.add(message1);
		messages.add(message2);
		messages.add(message3);
		proReq=new ProducerRequest();
		proReq.setClientId(prodProps.getProperty("clientId"));
		proReq.setMessages(messages);
		try {
			proResp=producer.publishMessage(proReq);
		} catch (SystemException | ApplicationException e) {
			e.printStackTrace();
		}

		return proResp;

	}


	public ProducerResponse publisgMessageWithAttribute(String propertyFile){
		Properties prodProps=FileUtils.getAllProperty(propertyFile);
		messages= new ArrayList<String>();
		messages.add(message1);
		messages.add(message2);
		proReq=new ProducerRequest();
		proReq.setClientId(prodProps.getProperty("clientId"));
		proReq.setMessages(messages);
		proReq.setAttributeName(attributeName);
		proReq.setAttributeValue(attributeValue);
		try {
			proResp=producer.publishMessage(proReq);
		} catch (SystemException | ApplicationException e) {
			e.printStackTrace();
		}

		return proResp;

	}

	public ProducerResponse publisgMessageWithAttributeNParent(String propertyFile){
		Properties prodProps=FileUtils.getAllProperty(propertyFile);
		messages= new ArrayList<String>();
		messages.add(message1);
		proReq=new ProducerRequest();
		proReq.setClientId(prodProps.getProperty("clientId"));
		proReq.setMessages(messages);
		proReq.setAttributeName(attributeName);
		proReq.setAttributeValue(attributeValue);
		proReq.setParentId(parentId);
		try {
			proResp=producer.publishMessage(proReq);
		} catch (SystemException | ApplicationException e) {
			e.printStackTrace();
		}

		return proResp;

	}


	public ProducerResponse publisgMessageWithAttributeNHighPriority(String propertyFile){
		Properties prodProps=FileUtils.getAllProperty(propertyFile);
		messages= new ArrayList<String>();
		messages.add(message1);
		proReq=new ProducerRequest();
		proReq.setClientId(prodProps.getProperty("clientId"));
		proReq.setMessages(messages);
		proReq.setAttributeName(attributeName);
		proReq.setAttributeValue(attributeValue);
		proReq.setPriority(highPriority);
		try {
			proResp=producer.publishMessage(proReq);
		} catch (SystemException | ApplicationException e) {
			e.printStackTrace();
		}

		return proResp;

	}
	
}
