package com.mq.rest;

import java.io.Serializable;
import java.util.List;

public class BatchMessageFormat implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<MessageFormat> msgDetails;

	public List<MessageFormat> getMsgDetails() {
		return msgDetails;
	}

	public void setMsgDetails(List<MessageFormat> msgDetails) {
		this.msgDetails = msgDetails;
	}
	
	

}
