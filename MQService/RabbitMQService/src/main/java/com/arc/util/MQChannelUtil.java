package com.arc.util;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MQChannelUtil {
	
	public Channel getchannel()
	{
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection;
	    Channel channel = null;
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
		    //channel.queueDeclare(queueName, false, false, false, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return channel;
	}
	

}
