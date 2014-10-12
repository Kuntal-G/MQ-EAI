package com.arc.util;

public class Ack {
	
	int evaltag;
	int status;
	
	public Ack(){}
	public Ack(int evaltag, int status) {
		super();
		this.evaltag = evaltag;
		this.status = status;
	}
	public int getEvaltag() {
		return evaltag;
	}
	public void setEvaltag(int evaltag) {
		this.evaltag = evaltag;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
