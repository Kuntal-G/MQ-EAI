package com.general.mq.rest.rqrsp;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class AcknowledgeDetail {
	
	private Integer ackStatus;
	private String messageId;
	private Boolean retriable = true;
	private Integer nextAttmptDelay;
	private String remark;
	
	public Integer getAckStatus() {
		return ackStatus;
	}
	public void setAckStatus(Integer ackStatus) {
		this.ackStatus = ackStatus;
	}
	
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public Boolean getRetriable() {
		return retriable;
	}
	public void setRetriable(Boolean retriable) {
		if(retriable!=null){	
		this.retriable = retriable;
		}else{
			this.retriable = true;
		}
	}
	public Integer getNextAttmptDelay() {
		return nextAttmptDelay;
	}
	public void setNextAttmptDelay(Integer nextAttmptDelay) {
		this.nextAttmptDelay = nextAttmptDelay;
	}
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	


}
