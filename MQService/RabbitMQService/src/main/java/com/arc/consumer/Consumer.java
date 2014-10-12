package com.arc.consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.arc.util.Ack;
import com.arc.util.AckFormat;
import com.arc.util.CacheObject;
import com.arc.util.ConsumerMsg;
import com.arc.util.ConsumerMsgFormat;
import com.arc.util.MQChannelUtil;
import com.arc.util.QueueName;
import com.mq.ehcache.MQCache;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.GetResponse;

public class Consumer {
	
	public ConsumerMsgFormat getMessage(String queueId, int batchSize)
	{
		ConsumerMsgFormat consumerMsgFormat = new ConsumerMsgFormat();
		MQChannelUtil channeUtil = new MQChannelUtil();
		Channel channel = channeUtil.getchannel();
		if(channel!=null)
		{
			try {
				channel.queueDeclare(QueueName.getQueueName(queueId), true, false, false, null);
				GetResponse response = null;
				List<ConsumerMsg> msglist  = new ArrayList<ConsumerMsg>();
				for(int i = 1; i<=batchSize;i++)
				{
					response = channel.basicGet(QueueName.getQueueName(queueId), false);
					
					if(response!=null)
					{
						String msg = new String(response.getBody());
						long deliveryTag = response.getEnvelope().getDeliveryTag();
						ConsumerMsg csmmsg = new ConsumerMsg(deliveryTag,msg);
						msglist.add(csmmsg);
						/*if in Queue no more messages are available break the loop.
						 * */
						if(response.getMessageCount()==0)
							break;
					}else{						
						if(msglist.size()==0)
						{
							Connection conn = channel.getConnection();
							channel.close();
							conn.close();
						}
						break;
					}
				}
				/** if the msgs are availabe in rabitMQ then channel object would be stored 
				 *  in cache. Otherwise simply an empty json would be returned with channelID
				 **/
				if(msglist.size()>0)
				{
					UUID channelid = UUID.randomUUID();
					consumerMsgFormat.setChannelId(channelid);
					consumerMsgFormat.setConsumermsg(msglist);
					//here channel would be cached with channelid....
					CacheObject cacheObj = new CacheObject(channel);
					MQCache.instance().add(channelid, cacheObj);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				consumerMsgFormat = null;
			}
		}
		
		return consumerMsgFormat;
	}
	
	public boolean acknowledgeMsg(AckFormat ackFormat)
	{  
		boolean isAck = true;
		UUID channelId = ackFormat.getChannelid();
		List<Ack> list = ackFormat.getAcks();
		
		Channel channel = MQCache.instance().get(channelId).getChannel(); //channel from cache here.?????????
		try {
			for (int i = 0; i < list.size(); i++) {
				int dtag = list.get(i).getEvaltag();

				if (list.get(i).getStatus() == 1)
					channel.basicAck(dtag, false);
				else
					channel.basicNack(dtag, false, true);

			}
            Connection conn = channel.getConnection();
            if(channel!=null)
				channel.close();
			if (conn != null)
				conn.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isAck = false;
		}
		
		MQCache.instance().evictCache(channelId);
		
		return isAck;
	}

}
