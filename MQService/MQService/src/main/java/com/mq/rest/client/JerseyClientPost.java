package com.mq.rest.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
 
public class JerseyClientPost {
 
  public static void main(String[] args) {
 
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