package com.org.rabbitmq.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Send {
	
	 private final static String QUEUE_NAME = "QUEUE_102";

	  public static void main(String[] argv)
	      throws java.io.IOException {
		  ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost("localhost");
		    Connection connection = factory.newConnection();
		    Channel channel = connection.createChannel();
		    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		    String message = "";
		    for(int i=100;i<111;i++)
		    {
		    	message = "Hello World!" + i;
		    	channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_BASIC, message.getBytes());
		    }
		    System.out.println(" [x] Sent '" + message + "'");
		    channel.close();
		    connection.close();
	  }

}
