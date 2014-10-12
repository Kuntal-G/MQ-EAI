package com.arc.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class PublisherMsgFormat {
	
    private String queueid;
    private List<PublisherMsg> msgs;
    
	public PublisherMsgFormat(String queueid, List<PublisherMsg> msgs) {
		super();
		this.queueid = queueid;
		this.msgs = msgs;
	}
	public String getQueueid() {
		return queueid;
	}
	public void setQueueid(String queueid) {
		this.queueid = queueid;
	}
	public List<PublisherMsg> getMsgs() {
		return msgs;
	}
	public void setMsgs(List<PublisherMsg> msgs) {
		this.msgs = msgs;
	}
	
	public static void main(String[] args) {
		String queueid = "500";
		PublisherMsg msg1 = new PublisherMsg("first msg from publisher");
		PublisherMsg msg2 = new PublisherMsg("second msg from publisher");
		PublisherMsg msg3 = new PublisherMsg("third msg from publisher");
		
		List<PublisherMsg> msglist = new ArrayList<>();
		msglist.add(msg1);
		msglist.add(msg2);
		msglist.add(msg3);
		
		PublisherMsgFormat pmf = new PublisherMsgFormat(queueid, msglist);
		Gson gson = new Gson();
		String json = gson.toJson(pmf);
		System.out.println(json);
		
	}
}
