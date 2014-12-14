package com.general.mq.common.exception;

import com.general.mq.common.error.ErrorCode;

public class ValidationException extends ApplicationException{

	private static final long serialVersionUID = 1L;

	public ValidationException(Throwable cause) {
		super(cause);

	}
	public ValidationException(ErrorCode errorCode) {
		super(errorCode);

	}

	public ValidationException(String message, ErrorCode errorCode) {
		super(message,errorCode);

	}

	public ValidationException(Throwable cause, ErrorCode errorCode) {
		super(cause,errorCode);

	}

	public ValidationException(String message, Throwable cause, ErrorCode errorCode) {
		super(message, cause,errorCode);

	}


}
