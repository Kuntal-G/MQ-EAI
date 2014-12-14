package com.general.mq.service.cache.adaptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.management.QueueChannel;

public class ChannelObjAdaptor {

	private static final ChannelObjAdaptor INSTANCE = new ChannelObjAdaptor();
	private final Map<String, QueueChannel> channelOBJMap;


	private ChannelObjAdaptor() {
		channelOBJMap= new ConcurrentHashMap<String, QueueChannel>(16, 0.90f, 1);
	}

	public static ChannelObjAdaptor instance() {
		return INSTANCE;
	}


	public void storeChannelObj(String channelId, QueueChannel qChannelObj) throws ApplicationException {
		MQLogger.l.debug("Entering ChannelObjAdaptor.storeChannelObj");
		channelOBJMap.put(channelId, qChannelObj);
		MQLogger.l.debug("Leaving ChannelObjAdaptor.storeChannelObj");
	}


	public QueueChannel getChannelObjById(String channelId) throws ApplicationException {
		MQLogger.l.debug("Entering ChannelObjAdaptor.getChannelObjById");
		QueueChannel qChannel=null;
		if(channelOBJMap.containsKey(channelId)){
			qChannel=channelOBJMap.get(channelId);
		}
		MQLogger.l.debug("Leaving ChannelObjAdaptor.getChannelObjById");
		return qChannel;

	}
	
	public void removeChannelObjById(String channelId) throws ApplicationException {
		MQLogger.l.debug("Entering ChannelObjAdaptor.removeChannelObjById");
		if(channelOBJMap.containsKey(channelId)){
			channelOBJMap.remove(channelId);
		}
		MQLogger.l.debug("Leaving ChannelObjAdaptor.removeChannelObjById");
	}


}
