package com.general.mq.dao.transform;

import java.util.Date;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.common.util.DateUtils;
import com.general.mq.dao.model.History;
import com.general.mq.dto.HistoryDto;



public class HistoryTransformer extends BaseTransformer<HistoryDto, History>{

	@Override
	public void syncToDto(History domain, HistoryDto dto)throws ApplicationException {
		if(domain.getId()!=null){
			MQLogger.l.info("Entering HistoryTransformer.syncToDto");
			dto.id=domain.getId();
			dto.clientId=domain.getClientId();
			dto.queueName=domain.getQueueName();
			dto.message=domain.getMessage();
			dto.msgAttrName=domain.getMsgAttrName();
			dto.msgAttrValue=domain.getMsgAttrValue();
			dto.status=domain.getStatus();
			dto.parentId=domain.getParentId();
			dto.setLoggingTime(DateUtils.sqlToUtil(domain.getLoggingTime()));
			dto.remark=domain.getReason();
			MQLogger.l.info("Leaving HistoryTransformer.syncToDto");
			
	}
		
	}

	@Override
	public void syncToDomain(HistoryDto dto, History domain)throws ApplicationException {
		if(dto.queueName!=null && dto.clientId!=null){
			MQLogger.l.info("Entering HistoryTransformer.syncToDomain");
			domain.setQueueName(dto.queueName);
			domain.setClientId(dto.clientId);
			domain.setMessage(dto.message);
			domain.setParentId(dto.parentId);
			domain.setMsgAttrName(dto.msgAttrName);
			domain.setMsgAttrValue(dto.msgAttrValue);
			domain.setStatus(dto.status);
			domain.setParentId(dto.parentId);
			
			if(dto.toTime!=null && dto.fromTime!=null){
			domain.setToTime(DateUtils.utilToSql(dto.toTime));
			domain.setFromTime(DateUtils.utilToSql(dto.fromTime));
			}
			
			if(dto.getLoggingTime()!=null){
			domain.setLoggingTime(DateUtils.utilToSql(dto.getLoggingTime()));
			}else{
				domain.setLoggingTime(DateUtils.utilToSql(new Date()));
			}
			domain.setReason(dto.remark);
			MQLogger.l.info("Leaving HistoryTransformer.syncToDomain");
			}
		
	}

	@Override
	protected boolean similar(HistoryDto dto, History domain) {
		return (dto != null && domain != null) ? dto.id==domain.getId() : false;
	}

	@Override
	protected void preDomainCreation(History domain) {
			
	}

	@Override
	protected void preDomainDeletion(History domain) {
		// TODO Auto-generated method stub
		
	}

}
