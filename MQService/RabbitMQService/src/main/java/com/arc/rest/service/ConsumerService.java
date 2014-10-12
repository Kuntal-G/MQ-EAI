package com.arc.rest.service;

import java.io.IOException;

import javax.management.JMException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.arc.consumer.Consumer;
import com.arc.util.AckFormat;
import com.arc.util.ConsumerMsgFormat;
import com.google.gson.Gson;




@Path("/consumer")
public class ConsumerService {
	
	@GET
	@Path("/getmsgs")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getConsume(@QueryParam("queueId") String queueId,@QueryParam("batchSize") int batchSize) throws Exception {
		Consumer cn= new Consumer();
		String msgJson = "";
		if(queueId!=null){
			ConsumerMsgFormat cmf = cn.getMessage(queueId,batchSize);
			Gson gson = new Gson();
			msgJson = gson.toJson(cmf);
		}else{
			return Response.status(200).entity("<queueId> is mandatory").build();
		} 
		return Response.status(200).entity(msgJson).build();
 	}
	
	
	
	
	@POST
	@Path("/get-ack")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAcknowlege(final String ackJson) throws IOException, JMException {
		String output ="";
		Gson gs = new Gson();
		AckFormat acks = gs.fromJson(ackJson, AckFormat.class);
		Consumer cn= new Consumer();
		boolean isAcknowledged = cn.acknowledgeMsg(acks);

		if (isAcknowledged == true) {
			output = "Thanks for your Acknowledgement";
			return Response.status(200).entity(output).build();
		} else {
			output = "Acknowledgement Rejected";
			return Response.status(404).entity(output).build();

		}

 	}
	
	
	
	

}
