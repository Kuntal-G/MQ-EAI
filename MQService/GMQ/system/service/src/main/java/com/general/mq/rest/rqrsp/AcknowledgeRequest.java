package com.general.mq.rest.rqrsp;

import java.util.List;

import com.general.mq.rest.rqrsp.common.BaseRequest;

public class AcknowledgeRequest extends BaseRequest{
	
	private String clientId;
	private List<AcknowledgeDetail> ackDetail;
	
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String consumerId) {
		this.clientId = consumerId;
	}
	public List<AcknowledgeDetail> getAckDetail() {
		return ackDetail;
	}
	public void setAckDetail(List<AcknowledgeDetail> ackDetail) {
		this.ackDetail = ackDetail;
	}
	
	
	
}
