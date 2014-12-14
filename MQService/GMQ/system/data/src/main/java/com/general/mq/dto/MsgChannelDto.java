package com.general.mq.dto;

import java.util.Date;

public class MsgChannelDto {
	
	public String clientId;
	public String channelId;
	public Date createTime = new Date();
	public int channelStatus = 1;
	public Long maxUpdatedTTL ;
	public Date lastUpdatedTime = new Date();
	public Date lastMsgDlvrdTime = new Date();

}
