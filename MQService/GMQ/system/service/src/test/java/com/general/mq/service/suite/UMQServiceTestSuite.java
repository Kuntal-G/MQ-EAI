package com.general.mq.service.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.general.mq.service.test.TestCase_1;
import com.general.mq.service.test.TestCase_2;
import com.general.mq.service.test.TestCase_3;

/**
 * 
 * This Test Suite will cover most functionality of service module in various flows,along with Validation and exception checking.
 *
 */
		

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestCase_1.class,TestCase_2.class,TestCase_3.class})
public class UMQServiceTestSuite {

}
