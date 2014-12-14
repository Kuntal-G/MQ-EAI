package com.general.mq.dao.transform;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.common.util.DateUtils;
import com.general.mq.dao.model.MsgChannel;
import com.general.mq.dto.MsgChannelDto;

public class MsgChannelTransformer extends BaseTransformer<MsgChannelDto, MsgChannel>{

	@Override
	public void syncToDto(MsgChannel domain, MsgChannelDto dto)throws ApplicationException {
		if(domain.getClientId()!=null && domain.getChannelId()!=null){
			MQLogger.l.info("Entering MsgChannelTransformer.syncToDto");
			dto.clientId=domain.getClientId();
			dto.channelId=domain.getChannelId();
			dto.channelStatus=domain.getChannelStatus();
			dto.createTime=DateUtils.sqlToUtil(domain.getCreateTime());
			dto.lastUpdatedTime=DateUtils.sqlToUtil(domain.getLastUpdatedTime());
			dto.maxUpdatedTTL=domain.getMaxUpdatedTTL();
			dto.lastMsgDlvrdTime=DateUtils.sqlToUtil(domain.getLastMsgDlvrdTime());
			MQLogger.l.info("Leaving MsgChannelTransformer.syncToDto");
	}
		
	}

	@Override
	public void syncToDomain(MsgChannelDto dto, MsgChannel domain)throws ApplicationException {
	
		if(dto.clientId!=null && dto.channelId!=null){
		MQLogger.l.info("Entering MsgChannelTransformer.syncToDomain");
		domain.setClientId(dto.clientId);
		domain.setChannelId(dto.channelId);
		domain.setChannelStatus(dto.channelStatus);
		domain.setCreateTime(DateUtils.utilToSql(dto.createTime));
		domain.setLastMsgDlvrdTime(DateUtils.utilToSql(dto.lastMsgDlvrdTime));
		domain.setLastUpdatedTime(DateUtils.utilToSql(dto.lastUpdatedTime));
		domain.setMaxUpdatedTTL(dto.maxUpdatedTTL);
		MQLogger.l.info("Leaving MsgChannelTransformer.syncToDomain");
	
		}
		
	}

	@Override
	protected boolean similar(MsgChannelDto dto, MsgChannel domain) {
		return (dto != null && domain != null) ? dto.channelId.equalsIgnoreCase(domain.getChannelId()) : false;
	}

	@Override
	protected void preDomainCreation(MsgChannel domain) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void preDomainDeletion(MsgChannel domain) {
		// TODO Auto-generated method stub
		
	}

}
