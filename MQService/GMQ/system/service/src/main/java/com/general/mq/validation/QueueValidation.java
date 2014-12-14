package com.general.mq.validation;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.util.ValidationUtils;
import com.general.mq.dto.QueueDetailDto;

public class QueueValidation {
	
	public static boolean validateQueue(QueueDetailDto qDto) throws ApplicationException{
			boolean validQName = ValidationUtils.isValidQName(qDto.queueName);
			boolean validRKey = ValidationUtils.isValidRoutingKey(qDto.getRoutingKey());
			boolean validMsgPriorityAttmpt = ValidationUtils.isValidMsgAtmptPriority(qDto.msgPriorityAttmpt);
			boolean validTimeUnit = ValidationUtils.isValidTimeUnit(qDto.getTimeUnit());
			return (validQName) && (validRKey) && (validMsgPriorityAttmpt) && (validTimeUnit);
				
	}

}
