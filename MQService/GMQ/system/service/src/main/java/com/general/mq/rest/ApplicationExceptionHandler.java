package com.general.mq.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.rest.rqrsp.ErrorResponse;
import com.general.mq.service.builder.ResponseBuilder;

/**
 * Application error handler for  exceptions thrown for all MQAPI RESTful
 * requests.
 */
@Provider
public class ApplicationExceptionHandler implements ExceptionMapper<ApplicationException> {

	@Override
	public Response toResponse(ApplicationException exception) {
		ErrorResponse response=ResponseBuilder.buildErrorResponse(exception, new ErrorResponse());
		return Response.status(response.error.code).entity(response).type(MediaType.APPLICATION_JSON).build();
	}
}




