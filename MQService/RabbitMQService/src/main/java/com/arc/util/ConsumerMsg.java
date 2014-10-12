package com.arc.util;

public class ConsumerMsg {
	
	private long evaltag;
	private String msg;
	
	public ConsumerMsg()
	{
		
	}
	
	public ConsumerMsg(long evaltag, String msg) {
		super();
		this.evaltag = evaltag;
		this.msg = msg;
	}
	public long getEvaltag() {
		return evaltag;
	}
	public void setEvaltag(int evaltag) {
		this.evaltag = evaltag;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}
