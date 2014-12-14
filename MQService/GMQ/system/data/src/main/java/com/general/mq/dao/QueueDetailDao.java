package com.general.mq.dao;

import java.util.List;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.dao.model.QueueDetail;

public interface QueueDetailDao {

	public  void createQueue(QueueDetail queue) throws ApplicationException ;
	public  QueueDetail findQueue(QueueDetail queueId) throws ApplicationException ;
    public  List<QueueDetail> findAllQueue()throws ApplicationException ;
    public  List<QueueDetail> findExchange(QueueDetail exchangeName) throws ApplicationException ;

	
}
