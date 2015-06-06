package com.general.mq.monitoring;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.general.mq.common.util.conf.MQConfig;
import com.general.mq.dao.model.History;

public class QueueDump {

	private static BlockingQueue<History> dbQueue = new LinkedBlockingQueue<History>(MQConfig.DUMP_QUEUE_SIZE);

	public static BlockingQueue<History> accessQueue() {
		return dbQueue;
	}

}