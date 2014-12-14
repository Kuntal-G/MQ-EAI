package com.general.mq.service.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.general.mq.background.jobs.ChannelCleaner;
import com.general.mq.background.jobs.KeyExpiredSubscriber;
import com.general.mq.cache.management.CacheManager;
import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.common.util.conf.MQConfig;
import com.general.mq.data.factory.ConnectionManager;
import com.general.mq.monitoring.impl.MonitorCachePool;
import com.general.mq.monitoring.impl.MonitorDatabasePool;
import com.general.mq.monitoring.impl.MonitorHealth;
import com.general.mq.monitoring.impl.MonitorJVM;
import com.general.mq.monitoring.impl.MonitorQueue;
import com.general.mq.service.cache.adaptor.ChannelAdaptor;
import com.general.mq.service.cache.adaptor.ClientAdaptor;
import com.general.mq.service.cache.adaptor.QueueAdaptor;

public class StartupHandler extends HttpServlet  {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException	  {
		//Initializing DB and Cache Pool at start up.
		ConnectionManager.configureConnPool();
		CacheManager.configureConnPool(); 
		
		//Initializing  all Local Adaptor Cache and Key Expired Subscriber at start up.
		try {
			//Local Cache's
			ClientAdaptor.instance().getAllClient();
			QueueAdaptor.instance().loadAllExchanges();
			QueueAdaptor.instance().loadAllQueues();
			ChannelAdaptor.instance().findAllChannels();
			
			
			//Initialize Monitor Health Metric Services
			MonitorHealth.getInstance().register(MonitorJVM.getInstance());
			MonitorHealth.getInstance().register(MonitorDatabasePool.getInstance());
			MonitorHealth.getInstance().register(MonitorQueue.getInstance());
			MonitorHealth.getInstance().register(MonitorCachePool.getInstance());
			
			//Redis Key Listener
			Runnable keyListener = new Runnable() {
				@Override
				public void run() {
				KeyExpiredSubscriber.instance().subscribe();
				}
			};
			new Thread(keyListener).start();
			
			//Channel Cleaner  with configurable Delay interval.
			ChannelCleaner.startChannelCleaner(MQConfig.CHNL_CLENR_DELAY);
			
		} catch (ApplicationException e) {
			//MQLogger.l.error("StartUp Handler Servlet Unable to initialize");
		}

	}

	public void doGet(HttpServletRequest request,HttpServletResponse response)	throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h1>" + "Initializing all Resource..." + "</h1>");
	}

	public void destroy(){
		MQLogger.l.info("StartUp Handler Servlet Destroyed");
	}
	


}
