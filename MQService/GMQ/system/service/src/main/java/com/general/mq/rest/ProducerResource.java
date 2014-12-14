package com.general.mq.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.general.mq.common.error.ValidationCode;
import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.exception.SystemException;
import com.general.mq.common.exception.ValidationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.common.util.ValidationUtils;
import com.general.mq.rest.rqrsp.ProducerRequest;
import com.general.mq.rest.rqrsp.ProducerResponse;
import com.general.mq.service.builder.ResponseBuilder;
import com.general.mq.service.impl.ProducerImpl;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProducerResource {

	private final ProducerImpl producerService= new ProducerImpl();

	@POST
	@Path("/register")
	public ProducerResponse registerProducer(ProducerRequest request)throws ApplicationException,SystemException{
		MQLogger.l.info("Entering ProducerResource.registerProducer");
		MQLogger.l.debug("Capturing ProducerResource.registerProducer.InputParams : QueueName="+request.getQueueName()+", RoutingKey="+request.getRoutingKey());
		ProducerResponse response=null;
		if(ValidationUtils.isValidQName(request.getQueueName())&&ValidationUtils.isValidRoutingKey(request.getRoutingKey())){
			response = producerService.registerProducer(request);
		}			
		response.success = ResponseBuilder.buildHeader(true);
		MQLogger.l.info("Leaving ProducerResource.registerProducer");
		return response;
	}


	

	@POST
	@Path("/publish")
	public ProducerResponse publishMessage(ProducerRequest request)throws ApplicationException,SystemException {
		MQLogger.l.info("Entering ProducerResource.publishMessage");
		MQLogger.l.debug("Capturing ProducerResource.publishMessage.InputParam : Client Id="+request.getClientId()+", 1st Message : "+request.getMessages().get(0));
		ProducerResponse response=null;
		if(ValidationUtils.isValidClientId(request.getClientId())){
			if(request.getMessages()!=null && !request.getMessages().isEmpty()){
				response = producerService.publishMessage(request);
			}else{
				throw new ValidationException(ValidationCode.MANDATORY_REQUIRED).set("Reason", "Please write Msg in correct format");
			}
		}
		response.success = ResponseBuilder.buildHeader(true);
		MQLogger.l.info("Leaving ProducerResource.publishMessage");
		return response;

	}


}