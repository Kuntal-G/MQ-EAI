package com.general.mq.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.general.mq.common.exception.SystemException;
import com.general.mq.rest.rqrsp.ErrorResponse;
import com.general.mq.service.builder.ResponseBuilder;

/**
 * System error handler for uncaught exceptions thrown for all MQAPI RESTful
 * requests.
 */
@Provider
public class SystemExceptionHandler implements ExceptionMapper<SystemException>  {

	@Override
	public Response toResponse(SystemException exception) {
		ErrorResponse response=ResponseBuilder.buildErrorResponse(exception, new ErrorResponse());
		return Response.status(response.error.code).entity(response).type(MediaType.APPLICATION_JSON).build();
	
	}

}
