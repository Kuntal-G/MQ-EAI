package com.general.with.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.general.mq.background.jobs.ChannelCleaner;
import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.exception.SystemException;
import com.general.mq.dto.HistoryDto;
import com.general.mq.dto.QueueDetailDto;
import com.general.mq.management.QueueChannel;
import com.general.mq.management.QueueChannelFactory;
import com.general.mq.rest.ConsumerResource;
import com.general.mq.rest.ProducerResource;
import com.general.mq.rest.QueueResource;
import com.general.mq.rest.rqrsp.AcknowledgeDetail;
import com.general.mq.rest.rqrsp.AcknowledgeRequest;
import com.general.mq.rest.rqrsp.AcknowledgeResponse;
import com.general.mq.rest.rqrsp.Acknowledgement;
import com.general.mq.rest.rqrsp.ConsumerRequest;
import com.general.mq.rest.rqrsp.ConsumerResponse;
import com.general.mq.rest.rqrsp.ProducerRequest;
import com.general.mq.rest.rqrsp.ProducerResponse;
import com.general.mq.rest.rqrsp.QueueRequest;
import com.general.mq.rest.rqrsp.QueueResponse;
import com.general.mq.service.cache.adaptor.ChannelAdaptor;
import com.general.mq.service.cache.adaptor.ClientAdaptor;
import com.general.mq.service.cache.adaptor.QueueAdaptor;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;


//To be used for quick development test
public class MainTest {
	private static final QueueResource qResource=new QueueResource();
	private static final ProducerResource pResource=new ProducerResource();
	private static final ConsumerResource cResource=new ConsumerResource();
	//private static final HistoryResource hResource=new HistoryResource();

	public static void main(String[] args) throws ApplicationException {
		
		//testPriority();
		//Initialize all local Cache **********
		//loadCache();

		/*********QUEUE SECTION********/
		//queueCreate();
		/*queueModify();
		queueTermination();


		*//*********PRODUCER SECTION********/
		//registerProducer();
		//publishMessage();


		/*********CONSUMER SECTION********/
//		consumerRegister();
//		consumerDeregister();
		//consumeMessage();
//		msgCount();
//		msgProcessingCount();


		/*********HISTORY SECTION********/
//		loadHistory();		
		
		
		/*******CHANNELCLEANER SECTION****/
			//startCleanerTest();
			
			/*********RMQ STATS********/
			//getQ_Conn_Chnl_Stats();
		
		/**************History*************/
		//getFilteredHistory();
		String date="Nov 28 2014 00:00:00 GMT 0530 (IST)";
		@SuppressWarnings("deprecation")
		Date d=new Date(date);
		System.out.println(d);

	}



	/**
	 * Load All Cache at start Up.
	 * @throws ApplicationException
	 */
	public static void loadCache() throws ApplicationException{
		ClientAdaptor.instance().getAllClient();
		QueueAdaptor.instance().loadAllExchanges();
		QueueAdaptor.instance().loadAllQueues();	
		ChannelAdaptor.instance().findAllChannels();	

	}
	/**
	 *Register Producer 
	 * @throws SystemException
	 * @throws ApplicationException
	 */
	public static void registerProducer() throws SystemException, ApplicationException{
		ProducerRequest request=new ProducerRequest();
		request.setQueueName("channelCleaningTest");
		request.setRoutingKey("k1");
		ProducerResponse response= pResource.registerProducer(request);
		System.out.println(response.getClientId());


	}

	/**
	 * Publish Message
	 * @throws SystemException
	 * @throws ApplicationException
	 */

	public static void publishMessage() throws SystemException, ApplicationException{
		ProducerRequest request=new ProducerRequest();
		request.setClientId("CID-776-7277220574271408");
		List<String> messages = new ArrayList<String>();
		messages.add("Msg1");
		messages.add("Msg2");
		/*messages.add("Msg3");
		messages.add("Msg4");*/
		request.setMessages(messages);
		request.setAttributeName("attrib1");
		request.setAttributeValue("This is cutom val");
		ProducerResponse response= pResource.publishMessage(request);
		System.out.println(response.status);


	}

	/**
	 * Queue Create
	 * @throws ApplicationException
	 */
	public static void queueCreate() throws ApplicationException{
		QueueRequest qRequ = new QueueRequest();
		QueueDetailDto qdto = new QueueDetailDto();
		qdto.queueName = "suman";
		qdto.setRoutingKey("saurabh");
		qdto.setTimeUnit(3);
		qdto.nxtAttmptDly=5;
		//qdto.maxAttmpt=3;
		qdto.msgPriorityAttmpt=3;
		//qdto.queueName = "Test00_delay";//
		//qdto.nxtAttmptDly=30000;
		qRequ.setQueueDetail(qdto);
		QueueResponse res=qResource.createQueue(qRequ);
		System.out.println(res.status);

	}

	/**
	 * Queue Message Count
	 * @throws ApplicationException
	 */
	public static void msgCount() throws ApplicationException{
		QueueResponse res=qResource.queueMsgCount("e54fa20a-863b-4374-a66f-29949acb868a", "", "");
		//QueueResponse res2 = qResource.queueMsgCount(null, "Msg", "rk1");
		//QueueResponse res3 = qResource.queueMsgCount(null, "sola", "ddd");
		//QueueResponse res4 = qResource.queueMsgCount("10969d60-e2c5-4213-b525-b1015cb58d25", "sola", "ddd");
		//QueueResponse res5 = qResource.queueMsgCount("e54fa20a-863b-4374-a66f-29949acb868a", null, null);
		System.out.println(res.getTotCount());
		System.out.println(res.status);

	}


	
	

	/**
	 * Message processing time update -TTL update
	 * @throws ApplicationException
	 */
	public static void ttlUpdate() throws ApplicationException{
		ConsumerRequest request=new ConsumerRequest();
		request.setClientId("5691d20c-01e7-416e-8f28-ec3b626bf493");
		request.setProcessTime(1800000);
		request.setMessageId("123");
		ConsumerResponse resp=cResource.updateMsgProcessingTime(request);
		System.out.println(resp.status);

	}
	

	/**
	 * Consumer Registration
	 * @throws ApplicationException
	 */
	public static void consumerRegister() throws ApplicationException{
		ConsumerRequest cr = new ConsumerRequest();
		cr.setQueueName("channelCleaningTest");
		cr.setRoutingKey("k1");
		cr.setProcessTime(2);
		cr.setTimeUnit(3);
		//cr.setRoutingKey("Default");
		ConsumerResponse resp=cResource.registerConsumer(cr);
		System.out.println(resp.getClientId());
		System.out.println(resp.status);

	}
	/**
	 * Consumer De-registration
	 * @throws ApplicationException
	 */
	public static void consumerDeregister() throws ApplicationException{
		ConsumerRequest cr = new ConsumerRequest();
		cr.setClientId("a5d577f9-dae2-4c4b-905d-c0fb15f93261");
		ConsumerResponse resp=cResource.registerConsumer(cr);
		System.out.println(resp.status);

	}

	/**
	 * Consume and ACk/NACK Message
	 * @throws ApplicationException
	 */
	public static void consumeMessage() throws ApplicationException{
		ConsumerResponse conResp = cResource.consumeMsg("CID-186-7277270698156349",14);
		AcknowledgeRequest ackRequest = new AcknowledgeRequest();
		List<AcknowledgeDetail> ackList = new ArrayList<AcknowledgeDetail>();
		for(int i=0;i<conResp.getMsgs().size();i++)
		{
			System.out.println(conResp.getMsgs().get(i).getMsgid()+":"+conResp.getMsgs().get(i).getMsg());

			/*if(i==0)
			{
				AcknowledgeDetail ack = new AcknowledgeDetail();
				ack.setAckStatus(0);
				ack.setRetriable(true);
				ack.setMessageId(conResp.getMsgs().get(i).getMsgid());
				ackList.add(ack);
			}else{*/
				AcknowledgeDetail ack = new AcknowledgeDetail();
				if(i%2==0)
					ack.setAckStatus(1);
				else
					ack.setAckStatus(0);
				//ack.setRetriable(true);
				ack.setMessageId(conResp.getMsgs().get(i).getMsgid());
				ackList.add(ack);
			//}
		}
		ackRequest.setClientId("CID-186-7277270698156349");
		ackRequest.setAckDetail(ackList);
		AcknowledgeResponse ackResp = cResource.msgAcknowledge(ackRequest);
		for (Acknowledgement ack : ackResp.getAckProcStatus()) {
			System.out.println("status: "+ ack.getStatus() + "\t" + "msgid: "+ack.getMessageId());
		} /*
		AcknowledgeDetail ackDetail = new AcknowledgeDetail();
		ackDetail.setAckStatus(0);
		ackDetail.setMessageId(conResp.getMsgs().get(0).getMsgid());*/

		//ci.successAck("170613ee-b41b-44b0-9838-2f2ddd7bf8be", ackDetail,false);


		System.out.println(ackResp.status);
		//KeyExpiredSubscriber.instance().subscribe();
		//System.out.println("End");


	}

	/**
	 * History loading
	 * @throws ApplicationException
	 */
	public static void loadHistory() throws ApplicationException{
		//HistoryRequest hreq=new HistoryRequest();
		HistoryDto hDto=new HistoryDto();
		hDto.clientId="637eeab8-4a73-4cb6-8ebe-a31263c637d8";
		hDto.queueName="";
		//hreq.setHistoryDto(hDto);
		//HistoryResponse hresp=hResource.getFilteredHistoryData(hreq);
		//<HistoryDto> histories = hresp.getHistoryData();
		//if(histories!=null){
		//	for(HistoryDto hist : histories){
		///		System.out.println(hist.message);
		//	}
		//}

	}

	/**
	 * Test RabbitMQ priority feature
	 */
	public static void testPriority(){
		
		QueueChannelFactory channelFactory = QueueChannelFactory.getChannelFactory();
		QueueChannel qChannel = channelFactory.getQueueChannel();
		Channel queCreatChannel = qChannel.getChannel();		
		AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties().builder();
		builder.priority(5);
		
		String msg="Test msg 5";
		Map<String,Object> priorityArgs=new HashMap<String, Object>();
		priorityArgs.put("x-max-priority", 50);
		
		try {
			//queCreatChannel.queueDeclare("newtesting2", false, false, false, priorityArgs);
			queCreatChannel.basicPublish("","newtesting2",builder.build() ,msg.getBytes());
		} catch (IOException e) {
		
		
	}
	}
	
	/**
	 * CHANNEL CLEANER.
	 * *
	 */
	
	public static void startCleanerTest()
	{
		ChannelCleaner.startChannelCleaner(60000);
	}
	
	/**
	 * RMQ STATS
	 * *
	 */
	
	public static void getQ_Conn_Chnl_Stats()
	{
		int inUseConnSize = QueueChannelFactory.getChannelFactory().getInUseConnection();
		System.out.println("inUseConnSize: "+inUseConnSize);
		int unAvailConnSize = QueueChannelFactory.getChannelFactory().getUnAvailConnection();
		System.out.println("unAvailConnSize: "+unAvailConnSize);
		int totalchnls = QueueChannelFactory.getChannelFactory().getTotalChannels();
		System.out.println("chnlPerConn : "+totalchnls);
		
	}
	
	public static void getFilteredHistory()
	{
		//final HistoryResource history=new HistoryResource();
		//HistoryResponse histResp;
		try {
		//	histResp=history.getFilteredHistoryData("status", "1", "Dec 1 2014 00:00:00 GMT+0530 (IST)", "Nov 25 2014 00:00:00 GMT+0530 (IST)");
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
