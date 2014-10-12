package com.org.rabbitmq.test;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class GetChannel {
	
	private static volatile GetChannel instance = null;
	private final static String QUEUE_NAME = "QUEUE_100";
	  public static Channel channel=null;
	  public static ConnectionFactory factory = null;
	  public static Connection connection=null;
	  public static QueueingConsumer consumer=null;
	
	private GetChannel() {
    }
 
    public static GetChannel getInstance() {
        if (instance == null) {
            synchronized (GetChannel.class) {
                instance = new GetChannel();
                factory = new ConnectionFactory();
    		    factory.setHost("localhost");
    		    try {
    				connection = factory.newConnection();
    				channel = connection.createChannel();
    			    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
    			    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    			    
    			    /*consumer = new QueueingConsumer(channel);
    			    channel.basicConsume(QUEUE_NAME, false, consumer);*/
    			    
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
            }
        }
        return instance;
    }
    
    
    public static void main(String[] args) {
		GetChannel.getInstance();
		while(true);
	}
    
    

}
