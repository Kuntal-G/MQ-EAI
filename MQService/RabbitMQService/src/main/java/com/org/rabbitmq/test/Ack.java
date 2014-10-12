package com.org.rabbitmq.test;

import java.io.IOException;
import java.util.Scanner;

import com.rabbitmq.client.Channel;

public class Ack {
	
	private final static String QUEUE_NAME = "hello";

	  public static void main(String[] argv)
	      throws java.io.IOException,
	             java.lang.InterruptedException {

	    /*ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();*/
		  Channel channel = GetChannel.getInstance().channel;

	    //channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    //System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
	    //QueueingConsumer consumer = new QueueingConsumer(channel);
	    System.out.println(channel);
	    channel.basicAck(10,false);
		//channel.close();
		//connection.close();
	  }
	  
	  public void sendAck()
	  {   Scanner keyboard = new Scanner(System.in);
	  	  Channel channel = GetChannel.getInstance().channel;
	      int dtag = 0;
		while (true) {
			System.out.println("give delivery tag");

			try {
				dtag = keyboard.nextInt();
				System.out.println("dtag is: "+dtag);
				channel.basicAck(dtag, false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//keyboard.close();
	  }

}
