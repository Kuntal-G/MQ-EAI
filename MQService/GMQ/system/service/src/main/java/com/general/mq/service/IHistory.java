package com.general.mq.service;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.rest.rqrsp.HistoryResponse;

public interface IHistory {
	
	HistoryResponse getAllData(int start,int limit) throws ApplicationException;
}
