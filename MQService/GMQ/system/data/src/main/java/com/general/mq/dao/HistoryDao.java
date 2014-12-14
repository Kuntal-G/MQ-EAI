package com.general.mq.dao;

import java.util.List;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.dao.model.History;

public interface HistoryDao {

	public  void saveHistory(History history) throws ApplicationException ;
	public  void saveBatchHistory(List<History> histories) throws ApplicationException ;
	public  List<History> findAllHistory(int start,int limit) throws ApplicationException ;
   }
