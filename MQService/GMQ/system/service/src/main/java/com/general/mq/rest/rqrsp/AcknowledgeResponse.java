package com.general.mq.rest.rqrsp;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.general.mq.rest.rqrsp.common.BaseResponse;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class AcknowledgeResponse extends BaseResponse {
	
	List<Acknowledgement> ackProcStatus;

	public List<Acknowledgement> getAckProcStatus() {
		return ackProcStatus;
	}

	public void setAckProcStatus(List<Acknowledgement> ackProcStatus) {
		this.ackProcStatus = ackProcStatus;
	}
	
	

}
