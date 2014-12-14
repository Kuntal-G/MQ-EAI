package com.general.mq.rest;

import com.general.mq.base.test.BaseTestCase;
import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.exception.SystemException;
import com.general.mq.rest.HistoryResource;
import com.general.mq.rest.rqrsp.HistoryResponse;

public class HistoryResourceTest extends BaseTestCase{

	private final HistoryResource history=new HistoryResource();
	HistoryResponse histResp;
	
	//@Test
	public void getAllHistoryData(){
		try {
			System.out.println(history.getAllHistoryData(0,10));
			
		} catch (SystemException | ApplicationException e) {
			
			e.printStackTrace();
		}
	}
	
	//@Test
	public void getFilteredHistoryData(){
		try {
		//	System.out.println(history.getFilteredHistoryData("status", "1", "Dec 1 2014 00:00:00 GMT+0530 (IST)", "Nov 25 2014 00:00:00 GMT+0530 (IST)"));
			
		} catch (SystemException e) {
			
			e.printStackTrace();
		}
	}
	
}
