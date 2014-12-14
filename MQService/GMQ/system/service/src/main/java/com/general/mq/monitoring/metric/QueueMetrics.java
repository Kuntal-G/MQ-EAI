package com.general.mq.monitoring.metric;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "name", "ping" })
public class QueueMetrics extends BaseMetrics{
	
	private int inUseConnSize;
	private int unAvailConnSize;
	private int chnlPerConn;

	
	public int getInUseConnSize() {
		return inUseConnSize;
	}
	public void setInUseConnSize(int inUseConnSize) {
		this.inUseConnSize = inUseConnSize;
	}
	public int getUnAvailConnSize() {
		return unAvailConnSize;
	}
	public void setUnAvailConnSize(int unAvailConnSize) {
		this.unAvailConnSize = unAvailConnSize;
	}
	public int getChnlPerConn() {
		return chnlPerConn;
	}
	public void setChnlPerConn(int chnlPerConn) {
		this.chnlPerConn = chnlPerConn;
	}
	
}
