package com.general.mq.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.dao.HistoryDao;
import com.general.mq.dao.model.History;
import com.general.mq.dao.transform.HistoryTransformer;
import com.general.mq.data.factory.DaoFactory;
import com.general.mq.dto.HistoryDto;
import com.general.mq.rest.rqrsp.HistoryResponse;
import com.general.mq.service.IHistory;

public class HistoryImpl implements IHistory{

	private final HistoryTransformer historyTransformer=new HistoryTransformer();
	private final DaoFactory mySqlFactory = DaoFactory.getDAOFactory();
	private final HistoryDao historyDao = mySqlFactory.getHistoryDao();


	@Override
	public HistoryResponse getAllData(int start,int limit) throws ApplicationException {
		MQLogger.l.info("Entering HistoryImpl.getAllData");
		HistoryResponse response=new HistoryResponse();
		List<HistoryDto> histDtos=new ArrayList<HistoryDto>();
		List<History> histDomains=historyDao.findAllHistory(start,limit);
		historyTransformer.syncToDto(histDomains, histDtos);
		//For Sorting output by Logging Time
		Collections.sort(histDtos, new Comparator<HistoryDto>() {
			public int compare(HistoryDto hDto1, HistoryDto hDto2) {
				return hDto2.getLoggingTime().compareTo(hDto1.getLoggingTime());
			}
		});
		response.setHistoryData(histDtos);
		MQLogger.l.info("Leaving HistoryImpl.getAllData");
		return response;
	}

	
}
