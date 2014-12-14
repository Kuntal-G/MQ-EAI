package com.general.mq.dao.model;

import java.io.Serializable;
import java.sql.Timestamp;


public class MsgChannel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String clientId;
	private String channelId;
	private Timestamp createTime;
	private int channelStatus;
	private Long maxUpdatedTTL;
	private Timestamp lastUpdatedTime;
	private Timestamp lastMsgDlvrdTime;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public int getChannelStatus() {
		return channelStatus;
	}
	public void setChannelStatus(int channelStatus) {
		this.channelStatus = channelStatus;
	}
	public Long getMaxUpdatedTTL() {
		return maxUpdatedTTL;
	}
	public void setMaxUpdatedTTL(Long maxUpdatedTTL) {
		this.maxUpdatedTTL = maxUpdatedTTL;
	}
	public Timestamp getLastUpdatedTime() {
		return lastUpdatedTime;
	}
	public void setLastUpdatedTime(Timestamp lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}
	public Timestamp getLastMsgDlvrdTime() {
		return lastMsgDlvrdTime;
	}
	public void setLastMsgDlvrdTime(Timestamp lastMsgDlvrdTime) {
		this.lastMsgDlvrdTime = lastMsgDlvrdTime;
	}
	
	
}
