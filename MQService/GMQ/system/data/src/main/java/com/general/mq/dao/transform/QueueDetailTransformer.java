package com.general.mq.dao.transform;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.common.util.NumberUtils;
import com.general.mq.common.util.StringUtils;
import com.general.mq.dao.model.QueueDetail;
import com.general.mq.dto.QueueDetailDto;

public class QueueDetailTransformer extends BaseTransformer<QueueDetailDto, QueueDetail> {

	@Override
	public void syncToDto(QueueDetail domain, QueueDetailDto dto)throws ApplicationException {
		if(domain!=null){
			MQLogger.l.info("Entering QueueDetailTransformer.syncToDto");
			if(domain.getQueueId()!=null){
			dto.queueId=domain.getQueueId();
			}
			dto.qName=domain.getQueueName();
			dto.queueName=domain.getExchangeName();
			dto.maxAttmpt=domain.getMaxAttmpt();
			dto.nxtAttmptDly=domain.getNxtAttmptDly();
			dto.msgPriorityAttmpt=domain.getMsgPriorityAttmpt();
			dto.setRoutingKey(domain.getRoutingKey());
			MQLogger.l.info("Leaving QueueDetailTransformer.syncToDto");
		}

	}

	@Override
	public void syncToDomain(QueueDetailDto dto, QueueDetail domain)throws ApplicationException {
		if(dto!=null){
			MQLogger.l.info("Entering QueueDetailTransformer.syncToDomain");
			domain.setQueueName(StringUtils.getQueueName(dto.getRoutingKey(),dto.queueName));
			domain.setExchangeName(dto.queueName);
			domain.setMaxAttmpt(dto.maxAttmpt);
			long ttl = NumberUtils.toMiliSeconds(dto.nxtAttmptDly, dto.getTimeUnit());
			domain.setNxtAttmptDly(ttl);
			domain.setMsgPriorityAttmpt(dto.msgPriorityAttmpt);
			domain.setRoutingKey(dto.getRoutingKey());
			MQLogger.l.info("Leaving QueueDetailTransformer.syncToDomain");
			}
	}

	@Override
	protected boolean similar(QueueDetailDto dto, QueueDetail domain) {
		return (dto != null && domain != null) ? dto.qName.equalsIgnoreCase(String.valueOf(domain.getQueueName())) : false;

	}

	@Override
	protected void preDomainCreation(QueueDetail domain) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void preDomainDeletion(QueueDetail domain) {
		// TODO Auto-generated method stub

	}

}
