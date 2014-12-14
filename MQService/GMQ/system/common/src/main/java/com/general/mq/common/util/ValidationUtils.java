package com.general.mq.common.util;

import com.general.mq.common.error.ValidationCode;
import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.exception.ValidationException;
import com.general.mq.common.util.conf.MQConfig;
import com.general.mq.common.util.conf.StatusConfig;


public class ValidationUtils {

	public static boolean isValidQName(String qName) throws ApplicationException{

		boolean valid = false;
		if(qName!=null){
			String pattern = "([a-zA-Z])+(\\w)*([a-zA-Z0-9])+";
			valid = qName.matches(pattern);
			if(!valid){
				throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.REASON, StatusConfig.QNAME_NOT_VALID); 
			}
		}else{
			throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.REASON, StatusConfig.QNAME_NOT_VALID); 
		}
		return valid;
	}

	public static boolean isValidRoutingKey(String routingKey) throws ApplicationException	{
		boolean valid = false;
		if(routingKey!=null){
			String pattern = "([a-zA-Z])+(\\w)*([a-zA-Z0-9])+";
			valid = routingKey.matches(pattern);
			if(!valid){
				throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.REASON, StatusConfig.RK_NOT_VALID); 
			}
		}else{
			throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.REASON, StatusConfig.RK_NOT_VALID); 
		}
		return valid;
	}

	public static boolean isValidClientId(String clientId)throws ApplicationException	{
		boolean valid = false;
		if(clientId!=null){
			String pattern = "CID-[0-9]{1,3}-[0-9]+";
			valid=clientId.matches(pattern);
			if(!valid){
				throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.REASON, StatusConfig.CLIENTID_NOT_VALID); 
			}
		}else{
			throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.REASON, StatusConfig.CLIENTID_NOT_VALID); 
		}
		return valid;
	}

	public static boolean isValidTimeUnit(int timeUnit)throws ApplicationException	{
		boolean valid = false;
		if(timeUnit==1||timeUnit==2||timeUnit==3){
			valid=true;
		}else{
			throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.REASON, StatusConfig.TIMEUNIT_NOT_VALID); 
		}	
		return valid;
	}

	public static boolean isValidMsgId(String msgId)throws ApplicationException	{
		boolean valid = false;
		if(msgId!=null){
			if(msgId.contains(MQConfig.CACHE_KEY_SEPARATOR)){
				valid=true;
			}else{
				throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.REASON, StatusConfig.MSGID_NOT_VALID);
			}
		}else{
			throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.REASON, StatusConfig.MSGID_NOT_VALID);
		}
		return valid;
	}

	public static boolean isValidParentId(String parentId)throws ApplicationException	{
		boolean valid = false;
		if(parentId!=null){
			String pattern = "([a-zA-Z])+[a-zA-Z0-9-_]+";
			valid = parentId.matches(pattern);		
			if(!valid){
				throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.REASON, StatusConfig.PARENTID_NOT_VALID);
			}
		}else{
			throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.REASON, StatusConfig.PARENTID_NOT_VALID);
		}
		return valid;
	}
	
	public static boolean isValidMsgAtmptPriority(Integer val)throws ApplicationException	{
		boolean valid = false;
		if(val!=null){
			if(val>=1 && val<=3){
				valid = true;
			}else{
				throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.REASON, StatusConfig.MSG_PRIORITY_ATMPT_NOT_VALID);
			}
		}else{
			throw new ValidationException(ValidationCode.INVALID_VALUE).set(StatusConfig.REASON, StatusConfig.MSG_PRIORITY_ATMPT_NOT_VALID);
		}
		return valid;
	}


}
