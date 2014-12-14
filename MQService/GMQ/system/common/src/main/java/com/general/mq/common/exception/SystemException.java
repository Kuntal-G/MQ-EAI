package com.general.mq.common.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.TreeMap;

import com.general.mq.common.error.ErrorCode;


/**
 * This class will act as a wrapper for all type of unchecked exceptions, from all
 * the layers of the application.
 */
public class SystemException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public static SystemException wrap(Throwable exception, ErrorCode errorCode) {
        if (exception instanceof SystemException) {
            SystemException se = (SystemException)exception;
        	if (errorCode != null && errorCode != se.getErrorCode()) {
                return new SystemException(exception.getMessage(), exception, errorCode);
			}
			return se;
        } else {
            return new SystemException(exception.getMessage(), exception, errorCode);
        }
    }
    
    public static SystemException wrap(Throwable exception) {
    	return wrap(exception, null);
    }
    
    private ErrorCode errorCode;
    private final Map<String,Object> properties = new TreeMap<String,Object>();
    

    public SystemException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public SystemException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public SystemException(Throwable cause, ErrorCode errorCode) {
		super(cause);
		this.errorCode = errorCode;
	}

	public SystemException(String message, Throwable cause, ErrorCode errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}
	
	public ErrorCode getErrorCode() {
        return errorCode;
    }
	
	public SystemException setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
        return this;
    }
	
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public SystemException(Throwable cause) {
		super(cause);
	}
	
    @SuppressWarnings("unchecked")
	public <T> T get(String name) {
        return (T)properties.get(name);
    }
	
    public SystemException set(String name, Object value) {
        properties.put(name, value);
        return this;
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
            for (String key : properties.keySet()) {
            	wrt.println("\t" + key + "=[" + properties.get(key) + "]"); 
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
