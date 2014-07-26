package com.mq.rest;

import java.io.Serializable;
import java.util.List;

public class BatchACKFormat implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<ACKFormat> ackFormat;

	public List<ACKFormat> getAckFormat() {
		return ackFormat;
	}

	public void setAckFormat(List<ACKFormat> ackFormat) {
		this.ackFormat = ackFormat;
	}
	
	

}
