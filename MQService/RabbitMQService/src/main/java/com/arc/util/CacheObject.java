package com.arc.util;

import java.io.Serializable;

import com.rabbitmq.client.Channel;



public class CacheObject implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Channel channel;
	
	public CacheObject(Channel channel) {
		super();
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	
	
	
}
