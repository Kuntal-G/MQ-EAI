package com.general.mq.rest;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;

import com.general.mq.common.error.ApplicationCode;
import com.general.mq.common.error.ValidationCode;
import com.general.mq.common.util.StringUtils;
import com.general.mq.common.util.conf.ErrorConfig;
import com.general.mq.rest.rqrsp.ErrorResponse;
import com.general.mq.rest.rqrsp.common.Error;
import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.api.ParamException;

/**
 * General error handler for uncaught exceptions thrown for all MQAPI RESTful
 * requests.
 */
@Provider
public class GenericExceptionHandler implements ExceptionMapper<Throwable> {

	/**
	 * Paths that should be ignored by this handler.
	 */
	static final String[] IgnorePaths = {
		"/v1/rest/", "/rest/"
	};

	/**
	 * Examines the exception to see if it is a false alarm. False alarms are
	 * cases where Jersey tries to handle URL paths that are not meant for
	 * MQAPI (anything that doesn't start with "/v1/"). When that occurs, a
	 * 404 response is returned. Since, Jersey is setup to forward 404s (via
	 * web.xml) to the next handler in the filter chain, these responses just
	 * need to be forwarded.
	 * 
	 * @param exception
	 *            Exception to examine
	 * @return null if this exception needs to be processed by toResponse.
	 */
	Response shouldHandle(Throwable exception) {
		if ((exception != null) && (exception instanceof NotFoundException)) {
			final NotFoundException nfe = (NotFoundException) exception;
			final URI nfUri = nfe.getNotFoundUri();
			if (nfUri != null) {
				final String path = nfUri.getPath();

				boolean ignore = true;
				for (final String ignorePath : IgnorePaths) {

					if (StringUtils.startsWith(path, ignorePath)) {
						ignore = false;
						break;
					}
				}

				if (ignore) {
					String message="<h1>"+ErrorConfig.CODE_404+"</h1>"
							+ "\n\n"
							+ nfe.getMessage().replaceFirst("null","");
					return  Response.serverError().entity(message).type(MediaType.TEXT_HTML).build();

				}
			}
		}

		return null;

	}

	@Override
	public Response toResponse(Throwable exception) {
		ErrorResponse response=null;
		Error error=null;
		Response r = null;
		r = shouldHandle(exception);

		if (r != null) {
			return r;
		}

		if(exception instanceof JsonParseException) {
			response=new ErrorResponse();
			response.success=false;
			error=new Error();
			error.code=ValidationCode.INVALID_JSON_MSG_FORMAT.getNumber();
			error.message=ValidationCode.INVALID_JSON_MSG_FORMAT.getMessage();
			response.error=error;
			r = Response.status(response.error.code).entity(response).type(MediaType.APPLICATION_JSON).build();	

		}else if (exception instanceof JsonMappingException) {
			response=new ErrorResponse();
			error=new Error();
			response.success=false;
			error.code=ValidationCode.INVALID_JSON_MAPPING.getNumber();
			error.message=ValidationCode.INVALID_JSON_MAPPING.getMessage();
			response.error=error;
			r = Response.status(response.error.code).entity(response).type(MediaType.APPLICATION_JSON).build();

		}else if (exception instanceof JsonProcessingException) {
			response=new ErrorResponse();
			response.success=false;
			error=new Error();
			error.code=ApplicationCode.DATA_PROCESS_FAIL.getNumber();
			error.message=ApplicationCode.DATA_PROCESS_FAIL.getMessage();
			response.error=error;
			r = Response.status(response.error.code).entity(response).type(MediaType.APPLICATION_JSON).build();	

		}else if (exception instanceof NumberFormatException) {
			response=new ErrorResponse();
			response.success=false;
			error=new Error();
			error.code=ValidationCode.INVALID_NUMBER_FORMAT.getNumber();
			error.message=ValidationCode.INVALID_NUMBER_FORMAT.getMessage();
			response.error=error;
			r = Response.status(response.error.code).entity(response).type(MediaType.APPLICATION_JSON).build();

		} else if (exception instanceof ParamException) {
			response=new ErrorResponse();
			response.success=false;
			error=new Error();
			error.code=ValidationCode.INVALID_PARAM.getNumber();
			error.message=ValidationCode.INVALID_PARAM.getMessage();
			response.error=error;
			r = Response.status(response.error.code).entity(response).type(MediaType.APPLICATION_JSON).build();

		} else{
			String message =" "+ErrorConfig.CODE_101+""
					+ "\n\n"
					+ exception.getMessage();
			r = Response.serverError().entity(message).type(MediaType.TEXT_PLAIN).build();
		}

		return r;
	}

}