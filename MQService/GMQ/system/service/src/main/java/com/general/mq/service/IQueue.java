package com.general.mq.service;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.rest.rqrsp.QueueRequest;
import com.general.mq.rest.rqrsp.QueueResponse;

public interface IQueue {
	
	QueueResponse createQueue(QueueRequest queueReq) throws ApplicationException ;
	QueueResponse loadAllQueue() throws ApplicationException ;
	QueueResponse queueMsgCount(String clientId,String queueName,String routingKey) throws ApplicationException ;
	QueueResponse loadAllExchange() throws ApplicationException;
	QueueResponse getKeysByExchange(String exchangeName) throws ApplicationException;
	QueueResponse getQueueByName(String queueName, String routingKey) throws ApplicationException;

}
