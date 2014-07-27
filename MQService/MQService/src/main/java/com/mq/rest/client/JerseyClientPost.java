package com.mq.rest.client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
 
public class JerseyClientPost {

  public static void main(String[] args) throws IOException {
	  JerseyClientPost jc=new JerseyClientPost();
	  jc.getPDFStream();
	
 
	}
  
  
  public InputStream getPDFStream() throws IOException  {
	  Client client = Client.create();
		 
		WebResource webResource = client.resource("http://localhost:8080/MQService/service/consumer/");
	    ClientResponse response = webResource.path("get-blob").type("application/octet_stream").get(ClientResponse.class);
	    System.out.println(response.getEntityInputStream());
	  
	    OutputStream os = new FileOutputStream(new File("D:/Software/Server/pdf-test.pdf"));
	    byte[] buffer = new byte[2048];
		int length = 0;
		InputStream inputStream =response.getEntityInputStream();
		while ((length = inputStream.read(buffer)) !=-1) {
			os.write(buffer, 0, length);
		}
	    return response.getEntityInputStream();
	}
  
  
  public static void postRequest(){
	  
	  try {
		  
			Client client = Client.create();
	 
			WebResource webResource = client
			   .resource("http://localhost:8080/MQService/service/producer/post-publish");
	 
			String input = "{\"clientId\":\"100\",\"message\":\"Hello Post Msg\"}";
	 
			ClientResponse response = webResource.type("application/json")
			   .post(ClientResponse.class, input);
	 
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}
	 
			System.out.println("Output from Server .... \n");
			String output = response.getEntity(String.class);
			System.out.println(output);
	 
		  } catch (Exception e) {
	 
			e.printStackTrace();
	 
		  }  
	  
  }
}