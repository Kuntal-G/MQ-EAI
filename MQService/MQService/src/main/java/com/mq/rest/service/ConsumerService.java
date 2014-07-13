package com.mq.rest.service;

import java.io.IOException;

import javax.jms.JMSException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mq.consumer.Consumer;
import com.mq.rest.MessageFormat;



@Path("/consumer")
public class ConsumerService {
	
	@GET
	@Path("/get-consume")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getConsume(@QueryParam("clientId") String clientId) throws Exception {
		MessageFormat output = null ;
		Consumer cn= new Consumer();
		if(clientId!=null){
	    output = cn.getConsumeMsg(clientId);
		}else{
			output.setMessage("Client-ID is mandatory");;
		} 
		return Response.status(200).entity(output).build();
 	}
	
	@GET
	@Path("/get-msgcount")
	public Response getMsgCount(@QueryParam("clientId") String clientId) throws JMSException, IOException {
		String output ="";
		Consumer cn= new Consumer();
		if(clientId!=null){
	    output = cn.getMsgCount(clientId);
		}else{
			output="clientId is mandatory";
		} 
		return Response.status(200).entity(output).build();
 	}
	
	
	@GET
	@Path("/get-ack")
	public Response getAcknowlege(@QueryParam("clientId") String clientId,@QueryParam("msgId") String msgId,@QueryParam("ack") String ack) throws JMSException, IOException {
		String output ="";
		Consumer cn= new Consumer();
		if(msgId!=null && ack!=null){
	    output = cn.getACK(clientId,msgId, ack);
	    if(output!=null){
	    	if(output.equalsIgnoreCase("Accepted")){
	    		output="Thanks for your Acknowledgement";
	    	return Response.status(200).entity(output).build();
	    	}else{
	    		output="Acknowledgement Rejected";
	    		return Response.status(401).entity(output).build();
	    		
	    	}
	    }
	    
		}else{
			output="ClientId and Acknowlegement are mandatory..";
		} 
		return Response.status(200).entity(output).build();
 	}
	

}
