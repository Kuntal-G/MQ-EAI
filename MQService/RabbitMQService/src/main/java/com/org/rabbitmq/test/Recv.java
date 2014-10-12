package com.org.rabbitmq.test;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.QueueingConsumer;

public class Recv {

	 private final static String QUEUE_NAME = "QUEUE_500";
	  public static Channel channel;
	 /* public static ConnectionFactory factory = null;
	  public static Connection connection;
	  public static QueueingConsumer consumer;*/
	  
	  public void receiveMsg() 
	  {
		  /*ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost("localhost");
		    Connection connection = factory.newConnection();*/
		    //Channel channel = connection.createChannel();
		    Channel channel = GetChannel.getInstance().channel;
		    //channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		   // System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		    //QueueingConsumer consumer = new QueueingConsumer(channel);
		    //channel.basicConsume(QUEUE_NAME, false, consumer);
		    GetResponse response ;
		    for(int i = 1;i<=5;i++)
		    {
		    	System.out.println("i:" +i);
		    	try {
					response = channel.basicGet(QUEUE_NAME, false);
					System.out.println("MessageCount:" + response.getMessageCount());
					if(response!=null)
			    	{
			    	System.out.println(new String(response.getBody()));
			    	System.out.println("Properties: "+response.getEnvelope());
			    	
			    	if(i==7)
			    	{
			    		channel.basicAck(response.getEnvelope().getDeliveryTag(), false);
			    	}
			    	}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    }
		    //channel.close();
		    

		    /*while (true) {
		      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		      String message = new String(delivery.getBody());
		      System.out.println(" [x] Received '" + message + "'");
		    }*/
	  }

	  public static void main(String[] argv)throws java.io.IOException,
      java.lang.InterruptedException
	       {
		  Recv receiver =new Recv();
		  receiver.receiveMsg();
		 // Ack ack = new Ack();
		 // ack.sendAck();
	    
	    }
	  
	  /*public static void createChannel() throws java.io.IOException,
      java.lang.InterruptedException*/
	  /*static 
	  {
		  if(factory == null)
		  {
		  factory = new ConnectionFactory();
		    factory.setHost("localhost");
		    try {
				connection = factory.newConnection();
				channel = connection.createChannel();
			    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
			    consumer = new QueueingConsumer(channel);
			    channel.basicConsume(QUEUE_NAME, false, consumer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		    
	  }
	  
	  public static Channel getChannel()
	  {
		  return channel;
	  }*/
	}
