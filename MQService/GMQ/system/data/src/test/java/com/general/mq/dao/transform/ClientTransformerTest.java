package com.general.mq.dao.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Test;

import com.general.mq.dao.model.Client;
import com.general.mq.dao.transform.ClientTransformer;
import com.general.mq.dto.ClientDto;

public class ClientTransformerTest {
	
	@Test
	public void testsyncToDto() throws Exception
	{
		ClientTransformer transformer = new ClientTransformer();
		Client domain = new Client();
		ClientDto dto= new ClientDto();
		domain.setClientId("CID-123-400");
		domain.setQueueId(100);
		domain.setRoutingKey("testRK");
		assertNull(dto.queueId);
		transformer.syncToDto(domain, dto);
		assertEquals("100",dto.queueId.toString());
	}
	@Test
	public void testsyncToDomain() throws Exception {
		ClientTransformer transformer = new ClientTransformer();
		Client domain = new Client();
		ClientDto dto= new ClientDto();
		dto.clientId="CID-123-400";
		dto.clientType=1;
		dto.isActive=1;
		dto.routingKey="testRK";
		assertNull(domain.getRoutingKey());
		transformer.syncToDomain(dto, domain);
		assertEquals("testRK", domain.getRoutingKey());
	}

	@Test
	public void testsimilar() throws Exception {
		ClientTransformer transformer = new ClientTransformer();
		Client domain = new Client();
		ClientDto dto= new ClientDto();
		dto.clientId="CID-123-400";
		domain.setClientId("CID-123-400");
		Boolean equal = (Boolean) executeMethod(transformer, "similar", new Object[] {dto, domain});
		assertNotNull(equal);
		assertTrue(equal);
	}


	private Object executeMethod(Object instance, String name, Object[] params) throws Exception {
		Class<?> c = instance.getClass();
		Class<?>[] types = new Class[params.length];
		for (int i = 0; i < params.length; i++)
			types[i] = params[i].getClass();
		Method m = c.getDeclaredMethod(name, types);
		m.setAccessible(true);
		return m.invoke(instance, params);
	}




}
