package com.general.mq.rest.rqrsp;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.general.mq.dto.QueueDetailDto;
import com.general.mq.rest.rqrsp.common.BaseResponse;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class QueueResponse extends  BaseResponse {
	
	private Integer totCount;
	private List<QueueDetailDto> queueDetails;
	private QueueDetailDto queueDetail;
	
	public Integer getTotCount() {
		return totCount;
	}

	public void setTotCount(Integer totCount) {
		this.totCount = totCount;
	}

	public List<QueueDetailDto> getQueueDetails() {
		return queueDetails;
	}

	public void setQueueDetails(List<QueueDetailDto> queueDetails) {
		this.queueDetails = queueDetails;
	}

	public QueueDetailDto getQueueDetail() {
		return queueDetail;
	}

	public void setQueueDetail(QueueDetailDto queueDetail) {
		this.queueDetail = queueDetail;
	}
}
