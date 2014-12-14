package com.general.mq.rest.rqrsp;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.general.mq.dto.HistoryDto;
import com.general.mq.monitoring.metric.AllMetrics;
import com.general.mq.rest.rqrsp.common.BaseResponse;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class MonitorResponse extends  BaseResponse {
	

	private HistoryDto historyDto;
	private List<AllMetrics> metrics;

	public HistoryDto getHistoryDto() {
		return historyDto;
	}
	public void setHistoryDto(HistoryDto historyDto) {
		this.historyDto = historyDto;
	}
	public List<AllMetrics> getMetrics() {
		return metrics;
	}
	public void setMetrics(List<AllMetrics> metrics) {
		this.metrics = metrics;
	}
	
}
