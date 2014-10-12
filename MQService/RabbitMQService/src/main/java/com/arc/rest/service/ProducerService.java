package com.arc.rest.service;

import java.io.IOException;

import javax.management.JMException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.arc.producer.Producer;
import com.arc.util.PublisherMsgFormat;
import com.google.gson.Gson;

@Path("/producer")
public class ProducerService {
	
	@POST
	@Path("/publish")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response publishmsg(final String publishmsgs) throws IOException, JMException {
		Gson gson = new Gson();
		PublisherMsgFormat pmf = gson.fromJson(publishmsgs, PublisherMsgFormat.class);
		Producer producer = new Producer();
		boolean ispublished = producer.publishMsg(pmf);
	    if(ispublished)
	    {
	    	return Response.status(200).entity("Msgs published").build();
	    }else{
	    	return Response.status(400).entity("Failed").build();
	    }
	}
	
	@GET
	@Path("/publish")
	public Response publishmsg(@QueryParam("queueId") String queueId,@QueryParam("msg") String msg) throws IOException, JMException {
	
		Producer producer = new Producer();
		boolean ispublished = producer.publishMsg(queueId,msg);
	    if(ispublished)
	    {
	    	return Response.status(200).entity("Msgs published").build();
	    }else{
	    	return Response.status(400).entity("Failed").build();
	    }	}

}
