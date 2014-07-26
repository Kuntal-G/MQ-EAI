package com.mq.rest.service;
 
import java.io.IOException;
import java.io.InputStream;

import javax.jms.JMSException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mq.producer.BlobMessageProducer;
import com.mq.producer.Producer;
import com.mq.rest.PostFormat;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

 
@Path("/producer")
public class ProducerService {
 
	@GET
	@Path("/get-publish")
	public Response getPublish(@QueryParam("clientId") String clientId,@QueryParam("msg") String msg,@QueryParam("priority") Integer priority) throws JMSException, IOException {
		String output ="";
		if(null!=clientId && null!= msg){
		Producer pd= new Producer();
	    output = pd.getPublishMsg(clientId, msg, priority);
		}else{
			output="clientId and msg parameters are mandatory";
		} 
		return Response.status(200).entity(output).build();
 	}
	
	@POST
	@Path("/post-publish")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postPublish(PostFormat msgFormat) throws JMSException, IOException {
		String output ="";
		if(null!= msgFormat.getClientId() && null!= msgFormat.getMessage()){
		Producer pd= new Producer();
	    output = pd.getPublishMsg(msgFormat.getClientId(), msgFormat.getMessage(), msgFormat.getPriority());
		}else{
			output="clientId and msg parameters are mandatory";
		} 
		return Response.status(200).entity(output).build();
 	}
	
	
	@POST
	@Path("/post-blob")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response postBlob(
			@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition contentDispositionHeader) throws JMSException, IOException {

		BlobMessageProducer producer =new BlobMessageProducer();
		String output=producer.postFile(fileInputStream,contentDispositionHeader.getFileName(),contentDispositionHeader.getSize());
		return Response.status(200).entity(output).build();

	}
	
	
	
}