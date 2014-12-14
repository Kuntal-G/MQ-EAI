package com.general.mq.cache.management;

import java.io.IOException;

import redis.clients.jedis.JedisPubSub;

import com.general.mq.common.error.SystemCode;
import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.common.util.StringUtils;
import com.general.mq.common.util.conf.RabbitConfig;
import com.general.mq.management.QueueChannel;
import com.general.mq.service.cache.adaptor.ChannelObjAdaptor;
import com.rabbitmq.client.Channel;


public class KeyExpiredListener extends JedisPubSub {

	@Override
	public void onMessage(String channel, String message) {	
		MQLogger.l.info("onMessage Pattern");	
	}


	@Override
	public void onSubscribe(String channel, int subscribedChannels) {	
		MQLogger.l.info("onSubscribe Pattern");	
	} 

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) { 
		MQLogger.l.info("onUnsubscribe Pattern");	
	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		MQLogger.l.info("onPSubscribe Pattern: "+pattern+" Number of Subscribed Channels: "+subscribedChannels);
	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		MQLogger.l.info("onPUnsubscribe Pattern: "+pattern+" Number of Subscribed Channels: "+subscribedChannels);
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
		//Will work only with following setting in redis.conf-> notify-keyspace-events Ex
		MQLogger.l.info("onPMessage Key Expired is: "+message);
		try {
			QueueChannel qChannelObj=ChannelObjAdaptor.instance().getChannelObjById(StringUtils.extractID(message, 0));
			if(qChannelObj==null){
				throw new ApplicationException(SystemCode.RESOURCE_NOT_FOUND);
			}else{
				qChannelObj.lock.lock();
				Channel channelObj=qChannelObj.getChannel();
				String dTag=StringUtils.extractID(message, 2);
				requeueMessage(channelObj, dTag);
				qChannelObj.lock.unlock();
			}
		} catch (ApplicationException e) {
			MQLogger.l.info("onPMessage exception while retriving Channel Object from Cache and requeue message: "+e);
		}
	}


	/**
	 * Method for re-queue message immediately in RMQ on Key expired.
	 * @param channelObj
	 * @param dTag
	 */
	private void requeueMessage(Channel channelObj,String dTag) {

		try {
			channelObj.basicNack(Long.parseLong(dTag), false, RabbitConfig.NACK_REQUEUE_IMMEDIATE);
		} catch (NumberFormatException | IOException e) {
			MQLogger.l.info("Key Expired Requeing failed due to : "+e);

		}


	}
}