package com.arc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;

public class ConsumerMsgFormat {
	
	private UUID channelId;
	
	private List<ConsumerMsg> consumermsg;
	
	public ConsumerMsgFormat(){}

	public ConsumerMsgFormat(UUID channelId, List<ConsumerMsg> consumermsg) {
		super();
		this.channelId = channelId;
		this.consumermsg = consumermsg;
	}

	public UUID getChannelId() {
		return channelId;
	}

	public void setChannelId(UUID channelId) {
		this.channelId = channelId;
	}

	public List<ConsumerMsg> getConsumermsg() {
		return consumermsg;
	}

	public void setConsumermsg(List<ConsumerMsg> consumermsg) {
		this.consumermsg = consumermsg;
	}
	
	public static void main(String[] args) {
		UUID channelid = java.util.UUID.randomUUID();
		System.out.println("channelid: "+channelid);
		Gson gson = new Gson();
		ConsumerMsg msg1 = new ConsumerMsg(100,"this is my msg");
		ConsumerMsg msg2 = new ConsumerMsg(101,"this is my msg");
		List<ConsumerMsg> msglist = new ArrayList<ConsumerMsg>();
		msglist.add(msg1);
		msglist.add(msg2);
		ConsumerMsgFormat conmsgformat = new ConsumerMsgFormat(channelid, msglist);
		System.out.println(gson.toJson(conmsgformat));
	}

}
