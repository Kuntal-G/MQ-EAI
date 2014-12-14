package com.general.mq.service.builder;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.exception.SystemException;
import com.general.mq.common.exception.ValidationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.rest.rqrsp.ErrorResponse;
import com.general.mq.rest.rqrsp.common.BaseResponse;



public abstract class ResponseBuilder {
	

	public static <T extends ErrorResponse> T buildErrorResponse(SystemException exception, T instance) {
		logException("SystemException occured :",exception);
		instance.error = new com.general.mq.rest.rqrsp.common.Error();
		if(exception.getErrorCode()!=null){
		instance.error.code =exception.getErrorCode().getNumber();
		instance.error.message = exception.getErrorCode().getMessage();
		}
		instance.success=buildHeader(false);
		return instance;
	}
	

	public static <T extends ErrorResponse> T buildErrorResponse(ApplicationException exception, T instance){
		logException("ApplicationException occured :",exception);
		instance.error = new com.general.mq.rest.rqrsp.common.Error();
		if(exception.getErrorCode()!=null){
		instance.error.code =exception.getErrorCode().getNumber();
		instance.error.message = exception.getErrorCode().getMessage();
		}
		instance.error.errorDetail=exception.printErrorDetails();
		instance.success=buildHeader(false);
		return instance;
	}
	
	
	public static <T extends ErrorResponse> T buildErrorResponse(ValidationException exception, T instance) {
		logException("ValidationException occured :",exception);
		instance.error = new com.general.mq.rest.rqrsp.common.Error();
		if(exception.getErrorCode()!=null){
		instance.error.code =exception.getErrorCode().getNumber();
		instance.error.message = exception.getErrorCode().getMessage();
		}
		instance.error.errorDetail=exception.printErrorDetails();
		instance.success=buildHeader(false);
		return instance;
	}
	
	
	public static <T extends BaseResponse> T buildResponse(SystemException exception, T instance) {
		logException("Exception occured :"+instance.status,exception);
		instance.success=buildHeader(false);
		return instance;
	}
	

	public static <T extends BaseResponse> T buildResponse(ApplicationException exception, T instance){
		logException("Exception occured :"+instance.status,exception);
		instance.success=buildHeader(false);
		return instance;
	}
	
	
	
	public static boolean  buildHeader(boolean isSuccess) {
	return isSuccess;
	}

	
	private static void logException(String exceptionType,Exception exception) {
		MQLogger.l.error(exceptionType, exception);
	}

	
}
