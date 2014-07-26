package com.mq.rest.service;

import java.io.FileInputStream;
import java.io.IOException;

import javax.jms.JMSException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mq.consumer.BlobMessageConsumer;
import com.mq.consumer.Consumer;
import com.mq.rest.BatchMessageFormat;



@Path("/consumer")
public class ConsumerService {
	
	@GET
	@Path("/get-consume")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getConsume(@QueryParam("clientId") String clientId,@QueryParam("batchSize") Integer batchSize) throws Exception {
		BatchMessageFormat output = null ;
		Consumer cn= new Consumer();
		if(null!=clientId){
	    output = cn.getConsumeMsg(clientId,batchSize);
		}else{
			return Response.status(200).entity("ClientId  is mandatory").build();
		} 
		return Response.status(200).entity(output).build();
 	}
	
	@GET
	@Path("/get-msgcount")
	public Response getMsgCount(@QueryParam("clientId") String clientId,@QueryParam("batchSize") Integer batchSize) throws JMSException, IOException {
		String output ="Number of Messages : ";
		Consumer cn= new Consumer();
		if(null!=clientId){
			if(null!=batchSize){
	         output =output+ cn.getMsgCount(clientId,batchSize).toString();
			}else{
				output =output+ cn.getMsgCount(clientId,null).toString();
			}
		}else{
			output="clientId is mandatory";
		} 
		return Response.status(200).entity(output).build();
 	}
	
	
	@GET
	@Path("/get-blob")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getFile() throws JMSException, IOException {
		
		BlobMessageConsumer consumer=new BlobMessageConsumer();
		FileInputStream fileStream=(FileInputStream) consumer.receiveFile();
		return Response.status(200).entity(fileStream).build();
 	}
	
	
	

}
