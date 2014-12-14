package com.general.mq.rest.rqrsp;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.general.mq.rest.rqrsp.common.BaseResponse;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Acknowledgement extends BaseResponse{
	
	private String messageId;
	private String status;
	private Boolean ackSuccess;
	private Boolean nackSuccess;
	
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Boolean getAckSuccess() {
		return ackSuccess;
	}
	public void setAckSuccess(Boolean ackSuccess) {
		this.ackSuccess = ackSuccess;
	}
	public Boolean getNackSuccess() {
		return nackSuccess;
	}
	public void setNackSuccess(Boolean nackSuccess) {
		this.nackSuccess = nackSuccess;
	}
	
}
