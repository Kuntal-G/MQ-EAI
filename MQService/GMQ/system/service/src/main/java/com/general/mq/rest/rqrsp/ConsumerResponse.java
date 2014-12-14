package com.general.mq.rest.rqrsp;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.general.mq.rest.rqrsp.common.BaseResponse;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ConsumerResponse extends BaseResponse{
	
	private String clientId;
	//--processed message details
	private Integer processedCount;
	private Integer successCount;
	private Integer failureCount;
	private List<MessageResponse> msgs;
	private Integer msgCount;

	public Integer getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(Integer msgCount) {
		this.msgCount = msgCount;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String consumerId) {
		this.clientId = consumerId;
	}

	public List<MessageResponse> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<MessageResponse> msgs) {
		this.msgs = msgs;
	}

	public Integer getProcessedCount() {
		return processedCount;
	}

	public void setProcessedCount(Integer processedCount) {
		this.processedCount = processedCount;
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public Integer getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(Integer failureCount) {
		this.failureCount = failureCount;
	}
	
	

}
