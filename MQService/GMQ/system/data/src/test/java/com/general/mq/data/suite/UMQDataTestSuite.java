package com.general.mq.data.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.general.mq.dao.model.DomainModelTest;
import com.general.mq.dao.transform.ClientTransformerTest;
import com.general.mq.data.factory.TestConnectionManager;


/**
 * 
 * This Test Suite will cover most functionality of data module in various flows,along with Validation and exception checking.
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ DomainModelTest.class,ClientTransformerTest.class,TestConnectionManager.class})

public class UMQDataTestSuite {

}
