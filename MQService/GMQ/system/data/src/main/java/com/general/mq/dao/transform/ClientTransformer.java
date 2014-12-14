package com.general.mq.dao.transform;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.common.util.NumberUtils;
import com.general.mq.dao.model.Client;
import com.general.mq.dto.ClientDto;

public class ClientTransformer extends BaseTransformer<ClientDto, Client>{

	@Override
	public void syncToDto(Client domain, ClientDto dto)	throws ApplicationException {
		MQLogger.l.info("Entering ClientTransformer.syncToDto");	
		if(domain.getClientId()!=null){
			dto.clientId=domain.getClientId();
			dto.clientType=domain.getClientType();
			dto.queueId=domain.getQueueId();
			dto.isActive=domain.getIsActive();
			if(domain.getRoutingKey()!=null){
				dto.routingKey=domain.getRoutingKey();
			}
			dto.timeToLive=domain.getTimeToLive();
			MQLogger.l.info("Leaving ClientTransformer.syncToDto");
		}

	}

	@Override
	public void syncToDomain(ClientDto dto, Client domain)throws ApplicationException {
		MQLogger.l.info("Entering ClientTransformer.syncToDomain");
		long ttl = dto.timeToLive;
		int timeUnit = dto.timeUnit;
		ttl = NumberUtils.toMiliSeconds(ttl, timeUnit);
		if(dto.clientId!=null){
			domain.setClientId(dto.clientId);
			domain.setClientType(dto.clientType);
			domain.setIsActive(dto.isActive);
			domain.setQueueId(dto.queueId);
			if(dto.routingKey!=null){
				domain.setRoutingKey(dto.routingKey);
			}
			domain.setTimeToLive(ttl);
			MQLogger.l.info("Leaving ClientTransformer.syncToDomain");
		}

	}

	@Override
	protected boolean similar(ClientDto dto, Client domain) {
		return (dto != null && domain != null) ? dto.clientId.equals(domain.getClientId()) : false;
	}

	@Override
	protected void preDomainCreation(Client domain) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void preDomainDeletion(Client domain) {
		// TODO Auto-generated method stub

	}

}
