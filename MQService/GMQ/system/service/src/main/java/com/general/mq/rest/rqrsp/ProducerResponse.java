package com.general.mq.rest.rqrsp;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.general.mq.rest.rqrsp.common.BaseResponse;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ProducerResponse  extends  BaseResponse{

	private String clientId;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String producerId) {
		this.clientId = producerId;
	}
		
}
