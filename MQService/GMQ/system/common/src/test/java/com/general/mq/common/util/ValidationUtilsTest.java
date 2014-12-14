package com.general.mq.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.util.ValidationUtils;

public class ValidationUtilsTest {
	
	@Test
	public void testValidParentId() throws ApplicationException {
		final boolean isValid = ValidationUtils.isValidParentId("p_1");
		assertTrue(isValid);
	}
	
	@Test
	public void testValidClientId() throws ApplicationException {
		final boolean isValid = ValidationUtils.isValidClientId("CID-737-10388256");
		assertTrue(isValid);
	}
	
	@Test
	public void testValidRoutingKey() throws ApplicationException {
		final boolean isValid = ValidationUtils.isValidRoutingKey("xs_12");
		assertTrue(isValid);
	}
	
	@Test
	public void testValidTimeUnit() throws ApplicationException {
		final boolean isValid = ValidationUtils.isValidTimeUnit(1);
		assertTrue(isValid);
	}
	
	@Test
	public void testValidQName() throws ApplicationException {
		final boolean isValid = ValidationUtils.isValidQName("solr_Test");
		assertTrue(isValid);
	}

	
	@Test
	public void testBadParentId()  {
		try {
			final boolean isValid = ValidationUtils.isValidParentId("@@@");
			assertTrue(isValid);
		} catch (ApplicationException e) {
			assertEquals(308,e.getErrorCode().getNumber() );
		}
		
	}
	
	@Test
	public void testBadClientId() {
		try {
			final boolean isValid = ValidationUtils.isValidClientId("----");
			assertTrue(isValid);
		} catch (ApplicationException e) {
			assertEquals(308,e.getErrorCode().getNumber() );
		}
	}
	
	@Test
	public void testBadTimeUnit() {
		try {
			final boolean isValid = ValidationUtils.isValidTimeUnit(5);
			assertTrue(isValid);
		} catch (ApplicationException e) {
			assertEquals(308,e.getErrorCode().getNumber() );
		}
	}
	
	@Test
	public void testBadRoutingKey() {
		try {
			final boolean isValid = ValidationUtils.isValidRoutingKey("$%");
			assertTrue(isValid);
		} catch (ApplicationException e) {
			assertEquals(308,e.getErrorCode().getNumber() );
		}
	}

}
