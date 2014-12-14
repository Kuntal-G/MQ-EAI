package com.general.mq.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.exception.SystemException;
import com.general.mq.rest.rqrsp.HistoryResponse;
import com.general.mq.service.builder.ResponseBuilder;
import com.general.mq.service.impl.HistoryImpl;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HistoryResource {

	private final HistoryImpl historyService=new HistoryImpl();

	@GET
	@Path("/getAllData")
	public HistoryResponse getAllHistoryData(@QueryParam("start") int start,@QueryParam("limit") int limit)throws ApplicationException,SystemException{
		HistoryResponse response = historyService.getAllData(start,limit);
		response.success = ResponseBuilder.buildHeader(true);
		return response;
	}
	
	
}
