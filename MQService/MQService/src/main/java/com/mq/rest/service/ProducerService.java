package com.mq.rest.service;
 
import java.io.IOException;

import javax.jms.JMSException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mq.producer.Producer;
import com.mq.rest.MessageFormat;

 
@Path("/producer")
public class ProducerService {
 
	@GET
	@Path("/get-publish")
	public Response getPublish(@QueryParam("clientId") String clientId,@QueryParam("msg") String msg,@QueryParam("priority") Integer priority) throws JMSException, IOException {
		String output ="";
		Producer pd= new Producer();
		if(clientId!=null && msg !=null){
	    output = pd.getPublishMsg(clientId, msg, priority);
		}else{
			output="clientId and msg parameters are mandatory";
		} 
		return Response.status(200).entity(output).build();
 	}
	
	@POST
	@Path("/post-publish")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postPublish(MessageFormat msgFormat) throws JMSException, IOException {
		System.out.println("Message Coming through Post:: "+msgFormat);
		System.out.println("......... "+msgFormat.getClientId()+"...."+msgFormat.getMessage());
		String output ="";
		Producer pd= new Producer();
		if(msgFormat.getClientId()!=null && msgFormat.getMessage() !=null){
	    output = pd.getPublishMsg(msgFormat.getClientId(), msgFormat.getMessage(), msgFormat.getPriority());
		}else{
			output="clientId and msg parameters are mandatory";
		} 
		return Response.status(200).entity(output).build();
 	}
	
	
	
}