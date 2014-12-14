package com.general.mq.rest.rqrsp;

import com.general.mq.dto.QueueDetailDto;
import com.general.mq.rest.rqrsp.common.BaseRequest;

public class QueueRequest extends BaseRequest{
	
	private QueueDetailDto queueDetail;
	
	public QueueDetailDto getQueueDetail() {
		return queueDetail;
	}

	public void setQueueDetail(QueueDetailDto queueDetail) {
		this.queueDetail = queueDetail;
	}

	
}
