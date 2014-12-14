package com.general.mq.rest.rqrsp;

import com.general.mq.dto.HistoryDto;
import com.general.mq.rest.rqrsp.common.BaseRequest;

public class MonitorRequest extends BaseRequest  {
	
	private int limit;
	private HistoryDto historyDto;
	
	
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public HistoryDto getHistoryDto() {
		return historyDto;
	}
	public void setHistoryDto(HistoryDto historyDto) {
		this.historyDto = historyDto;
	}
	
	

}
