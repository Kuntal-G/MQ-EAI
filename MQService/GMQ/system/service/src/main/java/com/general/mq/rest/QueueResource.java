package com.general.mq.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.exception.SystemException;
import com.general.mq.common.util.StringUtils;
import com.general.mq.common.util.ValidationUtils;
import com.general.mq.rest.rqrsp.QueueRequest;
import com.general.mq.rest.rqrsp.QueueResponse;
import com.general.mq.service.builder.ResponseBuilder;
import com.general.mq.service.impl.QueueImpl;
import com.general.mq.validation.QueueValidation;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class QueueResource {


	private final QueueImpl queueService= new QueueImpl();


	@POST
	@Path("/createQueue")
	public QueueResponse createQueue(QueueRequest request) throws ApplicationException,SystemException {
		QueueResponse response=null;
		//if Queue is validated then it will proceed further.
		QueueValidation.validateQueue(request.getQueueDetail());
		response = queueService.createQueue(request);					
		response.success = ResponseBuilder.buildHeader(true);
		return response;
	}

	@GET
	@Path("/queueMsgCount")
	public QueueResponse queueMsgCount(@QueryParam("clientId") String clientId,@QueryParam("queueName") String queueName,@QueryParam("routingKey") String routingKey) throws ApplicationException,SystemException{
		QueueResponse response=null;	
		if(ValidationUtils.isValidClientId(clientId)){
			response = queueService.queueMsgCount(StringUtils.trimDoubleQuotes(clientId),null,null);
		}else if (ValidationUtils.isValidQName(queueName) && ValidationUtils.isValidRoutingKey(routingKey)){
			response = queueService.queueMsgCount(null,queueName,routingKey);
		}
		response.success = ResponseBuilder.buildHeader(true);
		return response;
	}


	@GET
	@Path("/loadAllQueue")
	public QueueResponse loadAllQueue()throws ApplicationException,SystemException {
		QueueResponse response=null;
		response = queueService.loadAllQueue();
		response.success = ResponseBuilder.buildHeader(true);
		return response;
	}

	@GET
	@Path("/loadAllExchange")
	public QueueResponse loadAllExchange() throws ApplicationException,SystemException{
		QueueResponse response = queueService.loadAllExchange();
		response.success = ResponseBuilder.buildHeader(true);
		return response;

	}

	@GET
	@Path("/getKeysByExchange")
	public QueueResponse getKeysByExchange(@QueryParam("exchangeName") String exchangeName) throws ApplicationException,SystemException{
		QueueResponse response = queueService.getKeysByExchange(StringUtils.trimDoubleQuotes(exchangeName));
		response.success = ResponseBuilder.buildHeader(true);
		return response;
	}



	@GET
	@Path("/getQueue")
	public QueueResponse getQueueByName(@QueryParam("queueName") String queueName,@QueryParam("routingKey") String routingKey)throws ApplicationException,SystemException {
		QueueResponse response=null;

		if(ValidationUtils.isValidQName(queueName) && ValidationUtils.isValidRoutingKey(routingKey)){
			response = queueService.getQueueByName(queueName,routingKey);
		}
		response.success = ResponseBuilder.buildHeader(true);
		return response;
	}



}
