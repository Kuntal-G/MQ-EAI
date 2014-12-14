package com.general.mq.service.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.general.mq.base.test.BaseTestCase2;
import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.exception.SystemException;
import com.general.mq.rest.ConsumerResourceTest;
import com.general.mq.rest.ProducerResourceTest;
import com.general.mq.rest.rqrsp.AcknowledgeDetail;
import com.general.mq.rest.rqrsp.AcknowledgeRequest;
import com.general.mq.rest.rqrsp.AcknowledgeResponse;
import com.general.mq.rest.rqrsp.Acknowledgement;
import com.general.mq.rest.rqrsp.ConsumerRequest;
import com.general.mq.rest.rqrsp.MessageResponse;
import com.general.mq.test.util.FileUtils;

public class TestCase_2 extends BaseTestCase2{

	/*	@Test
	public void singleMsg_Pub_Con_Ack() {

		ConsumerResourceTest crt = new ConsumerResourceTest();
		ProducerResourceTest prt = new ProducerResourceTest();		
		AcknowledgeRequest ackRequest = new AcknowledgeRequest();
		try {
			//Publish Msg.
			prt.publisgSingleMessage("ProducerDelay.properties");
			//Consume Msg
			List<MessageResponse> msgListResp = crt.getMessages(1,"ConsumerDelay.properties");
			if(msgListResp.size()>0)
			{
				List<AcknowledgeDetail> ackList = new ArrayList<AcknowledgeDetail>();
				for(int i=0;i<msgListResp.size();i++)
				{
					AcknowledgeDetail ack = new AcknowledgeDetail();
					ack.setAckStatus(0);
					ack.setMessageId(msgListResp.get(i).getMsgid());
					ackList.add(ack);
				}
				String consumerId = FileUtils.getProperty("ConsumerDelay.properties","consumerId");
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
	public void batchMsg_Pub_Con_Nack() {

		ConsumerResourceTest crt = new ConsumerResourceTest();
		ProducerResourceTest prt = new ProducerResourceTest();
		AcknowledgeRequest ackRequest = new AcknowledgeRequest();
		try {
			//Publish Msg.
			prt.publisgBatchMessage("ProducerDelay.properties");
			//Consume Msg
			List<MessageResponse> msgListResp = crt.getMessages(4,"ConsumerDelay.properties");
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
				String consumerId = FileUtils.getProperty("ConsumerDelay.properties","consumerId");
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
			prt.publisgBatchMessage("ProducerDelay.properties");
			//Consume Msg
			List<MessageResponse> msgListResp = crt.getMessages(4,"ConsumerDelay.properties");
			if(msgListResp.size()>0)
			{
				List<AcknowledgeDetail> ackList = new ArrayList<AcknowledgeDetail>();
				for(int i=0;i<msgListResp.size();i++)
				{
					AcknowledgeDetail ack = new AcknowledgeDetail();
					if(i%2==0){
						ack.setAckStatus(1);
					}else{
						ack.setAckStatus(0);
						ack.setRetriable(true);
					}
					ack.setMessageId(msgListResp.get(i).getMsgid());
					ackList.add(ack);
				}
				String consumerId = FileUtils.getProperty("ConsumerDelay.properties","consumerId");
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
	}*/

	// Update Processing Time for single message
	@Test
	public void testUpdateProcessTime() {
		ConsumerResourceTest crt = new ConsumerResourceTest();
		ProducerResourceTest prt = new ProducerResourceTest();		
		AcknowledgeRequest ackRequest = new AcknowledgeRequest();
		ConsumerRequest conReq=new ConsumerRequest();
		try {
			//Publish Msg.
			prt.publisgSingleMessage("ProducerDelay.properties");
			System.out.println("Single Message Publish");
			//Consume Msg
			List<MessageResponse> msgListResp = crt.getMessages(1,"ConsumerDelay.properties");
			System.out.println("Get Single message size : "+msgListResp.size());
			// thread sleep for 1 Min (equivalent to processing message)
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			String consumerId = FileUtils.getProperty("ConsumerDelay.properties","consumerId");
			conReq.setClientId(consumerId);	
			conReq.setTimeUnit(2);
			conReq.setProcessTime(40);
			conReq.setMessageId(msgListResp.get(0).getMsgid());
			System.out.println("ID: "+consumerId+"  MSGID: "+msgListResp.get(0).getMsgid());
			System.out.println("Updating TTL");
			crt.updateMsgProcessTime(conReq);
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			System.out.println("Acknowledging Mesage");
			if(msgListResp.size()>0)
			{
				List<AcknowledgeDetail> ackList = new ArrayList<AcknowledgeDetail>();
				AcknowledgeDetail ack = new AcknowledgeDetail();
				ack.setAckStatus(0);
				ack.setMessageId(msgListResp.get(0).getMsgid());
				ackList.add(ack);

				ackRequest.setClientId(consumerId);
				ackRequest.setAckDetail(ackList);
				System.out.println("Sending Nack");
				AcknowledgeResponse ackResp = crt.sendAck(ackRequest);
				List<Acknowledgement> ackStatusList = ackResp.getAckProcStatus();
				
				if(!ackStatusList.isEmpty() && ackStatusList.get(0).getNackSuccess()==true){
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