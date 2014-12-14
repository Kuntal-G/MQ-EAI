package com.general.mq.rest;

import java.util.Properties;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.dto.QueueDetailDto;
import com.general.mq.rest.rqrsp.QueueRequest;
import com.general.mq.rest.rqrsp.QueueRequest_Dummy;
import com.general.mq.rest.rqrsp.QueueResponse;
import com.general.mq.test.util.FileUtils;

public class QueueResourceTest{

	private final QueueResource resource=new QueueResource();
	QueueRequest request;
	QueueRequest_Dummy modQReq;
	QueueResponse response;


	//Create Queue sequence	

	public QueueResponse testCreateQueue(String fileName) throws ApplicationException{
		Properties queueProps=FileUtils.getAllProperty(fileName);
		request=new QueueRequest();
		QueueDetailDto queueDetail=new QueueDetailDto();
		if(queueProps.getProperty("queueName")!=null)
			queueDetail.queueName=queueProps.getProperty("queueName");
		if(queueProps.getProperty("routingKey")!=null)
			queueDetail.setRoutingKey(queueProps.getProperty("routingKey"));
		if(queueProps.getProperty("maxAttmpt")!=null)
			queueDetail.maxAttmpt=Integer.parseInt(queueProps.getProperty("maxAttmpt"));
		if(queueProps.getProperty("nxtAttmptDly")!=null)
			queueDetail.nxtAttmptDly=Integer.parseInt(queueProps.getProperty("nxtAttmptDly"));
		if(queueProps.getProperty("timeUnit")!=null)
			queueDetail.setTimeUnit(Integer.parseInt(queueProps.getProperty("timeUnit")));
		if(queueProps.getProperty("msgPriorityAttmpt")!=null)
			queueDetail.msgPriorityAttmpt=Integer.parseInt(queueProps.getProperty("msgPriorityAttmpt"));
		request.setQueueDetail(queueDetail);
		response = resource.createQueue(request);
		return response;

	}
	// Get Message Count
	public QueueResponse testGetMessageCount(String QueuePropertyFile,String ClientPropertyFile)throws  ApplicationException{
		Properties queueProps=FileUtils.getAllProperty(QueuePropertyFile);
		Properties consumerProps=FileUtils.getAllProperty(ClientPropertyFile);
		response=resource.queueMsgCount(consumerProps.getProperty("consumerId"), queueProps.getProperty("queueName"), queueProps.getProperty("routingKey"));
		return response;
	}


	// Bad Input Exception Check Sequence	
	public QueueResponse testCreateQueueBadPriority() throws ApplicationException{

		request=new QueueRequest();
		QueueDetailDto queueDetail=new QueueDetailDto();
		queueDetail.queueName="QNAME";
		queueDetail.setRoutingKey("priorityRkey");
		queueDetail.setTimeUnit(2);
		queueDetail.msgPriorityAttmpt=6;
		request.setQueueDetail(queueDetail);	
		response = resource.createQueue(request);
		return response;

	}
	public QueueResponse testCreateQueueBadTimeUnitInput() throws ApplicationException{

		request=new QueueRequest();
		QueueDetailDto queueDetail=new QueueDetailDto();
		queueDetail.queueName="QNAME";
		queueDetail.setRoutingKey("tUnitRkey");
		queueDetail.setTimeUnit(6);
		queueDetail.msgPriorityAttmpt=2;
		request.setQueueDetail(queueDetail);	
		response = resource.createQueue(request);
		return response;

	}
	public QueueResponse testCreateQueueAllBadInput() throws ApplicationException{
		Properties queueProps=FileUtils.getAllProperty("queue.properties");
		request=new QueueRequest();
		QueueDetailDto queueDetail=new QueueDetailDto();
		queueDetail.queueName=queueProps.getProperty("badQueueName");
		queueDetail.setRoutingKey(queueProps.getProperty("badRoutingKey"));
		queueDetail.setTimeUnit(6);
		queueDetail.msgPriorityAttmpt=6;
		request.setQueueDetail(queueDetail);	
		response = resource.createQueue(request);
		return response;

	}


}
