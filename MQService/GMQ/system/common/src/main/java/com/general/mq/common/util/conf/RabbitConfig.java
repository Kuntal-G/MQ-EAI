package com.general.mq.common.util.conf;

public class RabbitConfig {
	
	public static final boolean NACK_REQUEUE_IMMEDIATE=true;
	public static final String MAX_PRIORITY="x-max-priority";
	public static final String X_DEAD_LETTER_EXCHANGE="x-dead-letter-exchange";
	public static final String X_MESSAGE_TTL="x-message-ttl";
	public static final String X_DEAD_LETTER_ROUTING_KEY="x-dead-letter-routing-key";
	public static final String EXCHANGE_TYPE="direct";

	
	

}
