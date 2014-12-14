package com.general.mq.common.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.general.mq.common.util.ValidationUtilsTest;
import com.general.mq.common.util.conf.ConfigManagerTest;


/**
 * 
 * This Test Suite will cover most functionality of common module in various flows,along with Validation and exception checking.
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ ValidationUtilsTest.class,ConfigManagerTest.class})
public class UMQCommonTestSuite {

}
