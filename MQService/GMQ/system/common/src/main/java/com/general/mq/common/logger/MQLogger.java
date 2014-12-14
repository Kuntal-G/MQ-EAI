package com.general.mq.common.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * Generic Logging Class,so that we don't have to initiate the getLogger on every class that we want to use it for.
 *
 */
public class MQLogger {


	public final static Logger l = LoggerFactory.getLogger(MQLogger.class.getName());



}
