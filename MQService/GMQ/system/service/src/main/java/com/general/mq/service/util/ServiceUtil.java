package com.general.mq.service.util;

import com.general.mq.common.error.ApplicationCode;
import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.util.conf.MQConfig;
import com.general.mq.dto.QueueDetailDto;
import com.general.mq.service.cache.adaptor.QueueAdaptor;

public class ServiceUtil {

	public static String getQueueName(QueueDetailDto qDto)	{
		String queueName = qDto.getRoutingKey()+MQConfig.QUEUE_PREFIX+qDto.queueName;
		return queueName;
	}


	public static String getDelayQueueName(QueueDetailDto qDto)	{
		String delayQueueName = MQConfig.DELAY_PREFIX+qDto.getRoutingKey()+MQConfig.QUEUE_PREFIX+qDto.queueName;
		return delayQueueName;
	}
	
	public static int getRecalculatePriority(int qID, int curntPriority) throws ApplicationException	{
		int reCalPriority = 0;
		try {
			QueueDetailDto qDto = QueueAdaptor.instance().findByQueueId(qID);
			int reAttmptPriority = qDto.msgPriorityAttmpt;
			reCalPriority = reAttmptPriority;
			switch(reCalPriority)
			{
				case 1:
					reCalPriority = reAttmptPriority - 1;
					break;
				case 3:
					reCalPriority = reCalPriority + 1; 
					
			}
		} catch (ApplicationException e) {
			throw new ApplicationException(e, ApplicationCode.DATA_ACCESS);
		}
		return reCalPriority;
	}

}
