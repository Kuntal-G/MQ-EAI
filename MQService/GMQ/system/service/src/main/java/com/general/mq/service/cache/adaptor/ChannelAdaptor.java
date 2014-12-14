package com.general.mq.service.cache.adaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.common.util.conf.StatusConfig;
import com.general.mq.dao.MsgChannelDao;
import com.general.mq.dao.model.MsgChannel;
import com.general.mq.dao.transform.MsgChannelTransformer;
import com.general.mq.data.factory.DaoFactory;
import com.general.mq.dto.MsgChannelDto;

public class ChannelAdaptor {

	private static final ChannelAdaptor INSTANCE = new ChannelAdaptor();
	private final Map<String, List<MsgChannelDto>> channelDBMap;
	private final ReadWriteLock channelLock = new ReentrantReadWriteLock();
	private final MsgChannelTransformer channelTransformer = new MsgChannelTransformer();
	DaoFactory mySqlFactory = DaoFactory.getDAOFactory();

	// Create a MsgChannel DAO
	MsgChannelDao channelDao = mySqlFactory.getChannelDao();

	private ChannelAdaptor() {
		channelDBMap = new ConcurrentHashMap<String, List<MsgChannelDto>>(16, 0.90f, 1);
	}

	public static ChannelAdaptor instance() {
		return INSTANCE;
	}


	public List<MsgChannelDto> findChannelsByClientId(final String clientId) throws ApplicationException {
	MQLogger.l.debug("Entering ChannelAdaptor.findChannelsByClientId");
		List<MsgChannelDto> channels=null;
		try{
			channelLock.readLock().lock();
			if (channelDBMap.isEmpty()) {
				findAllChannels();
			}
		} finally {
			channelLock.readLock().unlock();
		}
		if(channelDBMap.containsKey(clientId)){
			channels=channelDBMap.get(clientId);
		}
		MQLogger.l.debug("Leaving ChannelAdaptor.findChannelsByClientId");
		return channels;
	}


	public MsgChannelDto findActiveChannelByClientId(final String clientId) throws ApplicationException {
		MQLogger.l.debug("Entering ChannelAdaptor.findActiveChannelByClientId with Client Id: "+clientId);
		MsgChannelDto channelDto=null;
		try{
			channelLock.readLock().lock();			
			if (channelDBMap.isEmpty()) {				
				findAllChannels();
			}			
		} finally {
			channelLock.readLock().unlock();
		}
		if(channelDBMap.containsKey(clientId)){
			List<MsgChannelDto> channels=channelDBMap.get(clientId);		
			if(!channels.isEmpty()){				
				for (MsgChannelDto channel:channels){					
					if(channel.channelStatus==StatusConfig.ACTIVE_CHANNEL_STATUS) {						
						channelDto = channel;
						break;
					}
				}				
			}		
		}
		MQLogger.l.debug("Leaving ChannelAdaptor.findActiveChannelByClientId");
		return channelDto;
	}



	public void refreshChannels(final MsgChannelDto channel) throws ApplicationException {
		MQLogger.l.debug("Entering ChannelAdaptor.refreshChannels");
		try {
			channelLock.readLock().lock();
			if(channelDBMap.isEmpty()){
				findAllChannels();
				}else if(channelDBMap.containsKey(channel.clientId)){
				updateChannels(channel);
				}else{
				updateChannels(channel);
				}
		} finally {
			channelLock.readLock().unlock();
		}	
		MQLogger.l.debug("Leaving ChannelAdaptor.refreshChannels");
	}
	
	public void findAllChannels() throws ApplicationException {
		MQLogger.l.debug("Entering ChannelAdaptor.findAllChannels");
		List<MsgChannelDto> dtos = new ArrayList<MsgChannelDto>();
		List<MsgChannel> domains=channelDao.findAllChannels();
		channelTransformer.syncToDto(domains, dtos);
		indexChannels(dtos);
		MQLogger.l.debug("Leaving ChannelAdaptor.findAllChannels");
	}



	private void updateChannels(MsgChannelDto dto)throws ApplicationException {
		MQLogger.l.debug("Entering ChannelAdaptor.updateChannels");
		List<MsgChannelDto> channelDtos=new ArrayList<MsgChannelDto>();
		MsgChannel domain=new MsgChannel();
		channelTransformer.syncToDomain(dto, domain);
		List<MsgChannel>  channelDomains=channelDao.findAllChannelByFilter(domain);
		channelTransformer.syncToDto(channelDomains, channelDtos);
		updateIndexChannels(channelDtos);
		MQLogger.l.debug("Leaving ChannelAdaptor.updateChannels");
	}


	private void indexChannels(List<MsgChannelDto> channels) {
		MQLogger.l.debug("Entering ChannelAdaptor.indexChannels");
		for (MsgChannelDto channel : channels) {
			if (channel.clientId!=null && channel.channelId!=null) {
				if(channelDBMap.containsKey(channel.clientId)){
					List<MsgChannelDto> tempDtoList=new ArrayList<MsgChannelDto>();	
					tempDtoList=channelDBMap.get(channel.clientId);
					tempDtoList.add(channel);
					channelDBMap.put(channel.clientId, tempDtoList);	

				}else{
					List<MsgChannelDto> tempDtoList=new ArrayList<MsgChannelDto>();		
					tempDtoList.add(channel);
					channelDBMap.put(channel.clientId, tempDtoList);	
				}

			}
		}
		MQLogger.l.debug("Leaving ChannelAdaptor.indexChannels");
	}


	private void updateIndexChannels(List<MsgChannelDto> channels) {
		MQLogger.l.debug("Entering ChannelAdaptor.updateIndexChannels");
		for (MsgChannelDto channel : channels) {
			if (channel.clientId!=null && channel.channelId!=null) {
				if(channelDBMap.containsKey(channel.clientId)){
					channelDBMap.remove(channel.clientId);
					channelDBMap.put(channel.clientId, channels);	

				}else{				
					channelDBMap.put(channel.clientId, channels);	
				}

			}
		}
		MQLogger.l.debug("Leaving ChannelAdaptor.updateIndexChannels");
	}
	
	public void resetChannels() throws ApplicationException{
		findAllChannels();
	}
}
