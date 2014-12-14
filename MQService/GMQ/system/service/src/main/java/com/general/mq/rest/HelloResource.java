package com.general.mq.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
//TODO: remove this class later on after checking resting Rest resource functionality

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HelloResource {

	@GET
	@Path("/greeting")
	public String greeting(){
		
		return "Rest service working fine";
	}
	

}
