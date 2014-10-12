package com.arc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;

public class AckFormat {
	
	UUID channelid;
	List<Ack> acks;
	
	public AckFormat(){}
	
	public AckFormat(UUID channelid, List<Ack> acks) {
		super();
		this.channelid = channelid;
		this.acks = acks;
	}

	public UUID getChannelid() {
		return channelid;
	}

	public void setChannelid(UUID channelid) {
		this.channelid = channelid;
	}

	public List<Ack> getAcks() {
		return acks;
	}

	public void setAcks(List<Ack> acks) {
		this.acks = acks;
	}
	
	public static void main(String[] args) {
		UUID channelid = java.util.UUID.randomUUID();
		System.out.println("channelid: "+channelid);
		Gson gson = new Gson();
		Ack ack1 = new Ack(100,1);
		Ack ack2 = new Ack(101,0);
		List<Ack> acklist = new ArrayList<Ack>();
		acklist.add(ack1);
		acklist.add(ack2);
		AckFormat conmsgformat = new AckFormat(channelid, acklist);
		System.out.println(gson.toJson(conmsgformat));
	}
	
	

}
