package com.general.mq.common.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.TreeMap;

import com.general.mq.common.error.ErrorCode;

/**
 * This class will act as a wrapper for all type of checked exceptions, from all
 * the layers of the application.
 */
public class ApplicationException extends Exception {

	private static final long serialVersionUID = 1L;

	public static ApplicationException wrap(Throwable exception, ErrorCode errorCode) {
		if (exception instanceof ApplicationException) {
			ApplicationException ae = (ApplicationException)exception;
			if (errorCode != null && errorCode != ae.getErrorCode()) {
				return new ApplicationException(exception.getMessage(), exception, errorCode);
			}
			return ae;
		} else {
			return new ApplicationException(exception.getMessage(), exception, errorCode);
		}
	}

	public static ApplicationException wrap(Throwable exception) {
		return wrap(exception, null);
	}

	private ErrorCode errorCode;
	private final Map<String,Object> properties = new TreeMap<String,Object>();

	public ApplicationException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}


	public ApplicationException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public ApplicationException(Throwable cause, ErrorCode errorCode) {
		super(cause);
		this.errorCode = errorCode;
	}

	public ApplicationException(String message, Throwable cause, ErrorCode errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}


	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public ApplicationException setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
		return this;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String name) {
		return (T)properties.get(name);
	}

	public ApplicationException set(String name, Object value) {
		properties.put(name, value);
		return this;
	}

	public String printErrorDetails(){
		StringWriter stm = new StringWriter();
		PrintWriter wrt = new PrintWriter(stm);
		synchronized (wrt) {
			for (String key : properties.keySet()) {
				wrt.println(key + "" + properties.get(key)); 
			}
			wrt.flush();
			wrt.checkError();
		}
		return stm.toString();
	}
	
	
	
	public String stringifyStackTrace() { 
		StringWriter stm = new StringWriter();
		PrintWriter wrt = new PrintWriter(stm);
		synchronized (wrt) {
			wrt.println(this);
			wrt.println("\t-------------------------------");
			if (errorCode != null) {
				wrt.println("\t" + errorCode + ":" + errorCode.getClass().getName()); 
			}
			wrt.println("\t-------------------------------");
			StackTraceElement[] trace = getStackTrace();
			for (int i=0; i < trace.length; i++){
				wrt.println("\tat " + trace[i]);
			}

			Throwable ourCause = getCause();
			if (ourCause != null) {
				ourCause.printStackTrace(wrt);
			}
			wrt.flush();
			wrt.checkError();
		}
		return stm.toString();
	}

}