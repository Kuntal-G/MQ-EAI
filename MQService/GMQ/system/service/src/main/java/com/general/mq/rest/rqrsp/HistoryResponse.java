package com.general.mq.rest.rqrsp;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.general.mq.dto.HistoryDto;
import com.general.mq.rest.rqrsp.common.BaseResponse;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class HistoryResponse extends BaseResponse{
	
	private List<HistoryDto> historyData;

	public List<HistoryDto> getHistoryData() {
		return historyData;
	}

	public void setHistoryData(List<HistoryDto> historyData) {
		this.historyData = historyData;
	}
}
