package com.arc.producer;

import java.io.IOException;
import java.util.List;

import com.arc.util.MQChannelUtil;
import com.arc.util.PublisherMsg;
import com.arc.util.PublisherMsgFormat;
import com.arc.util.QueueName;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

public class Producer {
	
	public boolean publishMsg(String queueId, String msg)
	{
		MQChannelUtil channeUtil = new MQChannelUtil();
		Channel channel = channeUtil.getchannel();
		if (channel != null) {

			try {
				channel.queueDeclare(QueueName.getQueueName(queueId), true,false, false, null);
				channel.basicPublish("",QueueName.getQueueName(queueId), MessageProperties.PERSISTENT_BASIC, msg.getBytes());
				Connection conn = channel.getConnection();
				channel.close();
				conn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
		}
		return true;
	}
	
	public boolean publishMsg(PublisherMsgFormat pmf)
	{
		MQChannelUtil channeUtil = new MQChannelUtil();
		String queueId = pmf.getQueueid();
		List<PublisherMsg> msglist = pmf.getMsgs();
		if (queueId != null) {
			Channel channel = channeUtil.getchannel();
			if (channel != null) {

				try {
					channel.queueDeclare(QueueName.getQueueName(queueId), true,
							false, false, null);
					String msg="";
					for(int i=0;i<msglist.size();i++)
					{
						msg = msglist.get(i).getMsg();
						channel.basicPublish("", QueueName.getQueueName(queueId),
							MessageProperties.PERSISTENT_BASIC, msg.getBytes());
					}
					Connection conn = channel.getConnection();
					channel.close();
					conn.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}

			}
		}
		return true;
	}
	

}
