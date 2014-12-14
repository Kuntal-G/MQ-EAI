package com.general.mq.dao;

import java.util.List;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.dao.model.MsgChannel;

public interface MsgChannelDao {
	
		public  void createChannel(MsgChannel channel) throws ApplicationException ;
		public  void updateChannel(MsgChannel channel,boolean channelStatus, boolean maxTTL, boolean lastUpdateTime, boolean lastDelveredTime)throws ApplicationException  ;
	    public  MsgChannel findChannel(MsgChannel id) throws ApplicationException ;
	    public  List<MsgChannel> findAllChannelByFilter(MsgChannel filter) throws ApplicationException ;
	    public  List<MsgChannel> findAllChannels() throws ApplicationException ;
	    public void deleteChannel(MsgChannel channel) throws ApplicationException;
	   

}
