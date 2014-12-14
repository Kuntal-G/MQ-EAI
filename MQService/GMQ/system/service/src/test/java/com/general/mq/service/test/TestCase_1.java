package com.general.mq.service.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.general.mq.base.test.BaseTestCase1;
import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.exception.SystemException;
import com.general.mq.rest.ConsumerResourceTest;
import com.general.mq.rest.ProducerResourceTest;
import com.general.mq.rest.rqrsp.AcknowledgeDetail;
import com.general.mq.rest.rqrsp.AcknowledgeRequest;
import com.general.mq.rest.rqrsp.AcknowledgeResponse;
import com.general.mq.rest.rqrsp.Acknowledgement;
import com.general.mq.rest.rqrsp.MessageResponse;
import com.general.mq.test.util.FileUtils;

public class TestCase_1 extends BaseTestCase1{

	@Test
	public void singleMsg_Pub_Con_Ack() {
		
		ConsumerResourceTest crt = new ConsumerResourceTest();
		ProducerResourceTest prt = new ProducerResourceTest();
		AcknowledgeRequest ackRequest = new AcknowledgeRequest();
		try {
			//Publish Msg.
			prt.publisgSingleMessage("DefaultProducer.properties");
			//Consume Msg
			List<MessageResponse> msgListResp = crt.getMessages(1,"DefaultConsumer.properties");
			if(msgListResp.size()>0)
			{
				List<AcknowledgeDetail> ackList = new ArrayList<AcknowledgeDetail>();
				for(int i=0;i<msgListResp.size();i++)
				{
					AcknowledgeDetail ack = new AcknowledgeDetail();
					ack.setAckStatus(1);
					ack.setMessageId(msgListResp.get(i).getMsgid());
					ackList.add(ack);
				}
				String consumerId = FileUtils.getProperty("DefaultConsumer.properties","consumerId");
				ackRequest.setClientId(consumerId);
				ackRequest.setAckDetail(ackList);
				AcknowledgeResponse ackResp = crt.sendAck(ackRequest);
				List<Acknowledgement> ackStatusList = ackResp.getAckProcStatus();
				int i;
				for(i=0;i<ackStatusList.size();i++){
					if(ackStatusList.get(i).getAckSuccess()==true)
						continue;
					else
						break;
				}
				if(i==ackStatusList.size())
				{
					Assert.assertTrue(true);
				}else{
					Assert.assertTrue(false);
				}
				
			}
		} catch (SystemException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		} catch (ApplicationException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}
	
	
	@Test
	public void batchMsg_Pub_Con_Nack() {
		
		ConsumerResourceTest crt = new ConsumerResourceTest();
		ProducerResourceTest prt = new ProducerResourceTest();
		AcknowledgeRequest ackRequest = new AcknowledgeRequest();
		try {
			//Publish Msg.
			prt.publisgBatchMessage("DefaultProducer.properties");
			//Consume Msg
			List<MessageResponse> msgListResp = crt.getMessages(4,"DefaultConsumer.properties");
			if(msgListResp.size()>0)
			{
				List<AcknowledgeDetail> ackList = new ArrayList<AcknowledgeDetail>();
				for(int i=0;i<msgListResp.size();i++)
				{
					AcknowledgeDetail ack = new AcknowledgeDetail();
					ack.setAckStatus(0);
					ack.setMessageId(msgListResp.get(i).getMsgid());
					ack.setRetriable(true);
					ackList.add(ack);
				}
				String consumerId = FileUtils.getProperty("DefaultConsumer.properties","consumerId");
				ackRequest.setClientId(consumerId);
				ackRequest.setAckDetail(ackList);
				AcknowledgeResponse ackResp = crt.sendAck(ackRequest);
				List<Acknowledgement> ackStatusList = ackResp.getAckProcStatus();
				int i;
				for(i=0;i<ackStatusList.size();i++){
					if(ackStatusList.get(i).getNackSuccess()==true)
						continue;
					else
						break;
				}
				if(i==ackStatusList.size())
				{
					Assert.assertTrue(true);
				}else{
					Assert.assertTrue(false);
				}
				
			}
		} catch (SystemException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		} catch (ApplicationException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}
	
	@Test
	public void batchMsg_Pub_Con_MixAckNack() {
		
		ConsumerResourceTest crt = new ConsumerResourceTest();
		ProducerResourceTest prt = new ProducerResourceTest();
		AcknowledgeRequest ackRequest = new AcknowledgeRequest();
		try {
			//Publish Msg.
			prt.publisgBatchMessage("DefaultProducer.properties");
			//Consume Msg
			List<MessageResponse> msgListResp = crt.getMessages(4,"DefaultConsumer.properties");
			if(msgListResp.size()>0)
			{
				List<AcknowledgeDetail> ackList = new ArrayList<AcknowledgeDetail>();
				for(int i=0;i<msgListResp.size();i++)
				{
					AcknowledgeDetail ack = new AcknowledgeDetail();
					if(i%2==0)
						ack.setAckStatus(1);
					else
						ack.setAckStatus(0);
					ack.setMessageId(msgListResp.get(i).getMsgid());
					ackList.add(ack);
				}
				String consumerId = FileUtils.getProperty("DefaultConsumer.properties","consumerId");
				ackRequest.setClientId(consumerId);
				ackRequest.setAckDetail(ackList);
				AcknowledgeResponse ackResp = crt.sendAck(ackRequest);
				List<Acknowledgement> ackStatusList = ackResp.getAckProcStatus();
				int i;
				for(i=0;i<ackStatusList.size();i++){
					
					if(i%2==0 && ackStatusList.get(i).getAckSuccess()==true){
						continue;
					}else if(i%2!=0 && ackStatusList.get(i).getNackSuccess()==true){
						continue;
					}else{
						break;
					}
				}
				if(i==ackStatusList.size())
				{
					Assert.assertTrue(true);
				}else{
					Assert.assertTrue(false);
				}
				
			}
		} catch (SystemException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		} catch (ApplicationException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}

}
