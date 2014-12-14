package com.general.mq.dao.model;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class DomainModelTest {

	@Test
	public void testAnnotations() throws Exception {

		final File file = new File("target/classes/com/general/mq/dao/model/");
		
		final String[] list = file.list();
		for (String string : list) {
			if (string.endsWith("class")) {
				final String string2 = string.split("\\.")[0];
				if (!string2.endsWith("Test")) {
					//System.out.println("--" + string2);
					Class<?> forName = Class.forName("com.general.mq.dao.model." + string2);
					Field[] fields = forName.getDeclaredFields();
					for (Field field : fields) {
						if ((field.getModifiers() | Modifier.FINAL) != field.getModifiers()) {
							final String name = "set" + DomainModelTest.naturalOrder(field.getName());
							Method setMethod = forName.getDeclaredMethod(name, field.getType());
							final Object input = getObjectValue(field, null);
							final Object newInstance = forName.newInstance();
							setMethod.invoke(newInstance, input);
							Method getMethod = null;
							try {
								getMethod = forName.getDeclaredMethod("get" + DomainModelTest.naturalOrder(field.getName()));
							} catch (Exception e) {
								getMethod = forName.getDeclaredMethod("is" + DomainModelTest.naturalOrder(field.getName()));
							}
							final Object output = getMethod.invoke(newInstance);
							assertEquals(input, output);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object getObjectValue(Field filed, Class<?> type) throws InstantiationException, IllegalAccessException {
		type = type != null ? type : filed.getType();
		if (type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class)) {
			return 1;
		} else if (type.isAssignableFrom(char.class) || type.isAssignableFrom(Character.class)) {
			return '1';
		} else if (type.isAssignableFrom(float.class) || type.isAssignableFrom(Float.class)) {
			return 1f;
		} else if (type.isAssignableFrom(long.class) || type.isAssignableFrom(Long.class)) {
			return 1l;
		} else if (type.isAssignableFrom(boolean.class) || type.isAssignableFrom(Boolean.class)) {
			return true;
		} else if (type.isAssignableFrom(BigDecimal.class)) {
			return new BigDecimal("1");
		}  else if (type.isAssignableFrom(Timestamp.class)) {
			return new Timestamp(new Date().getTime());
		} else if (type.isAssignableFrom(String.class)) {
			return "null0";
		} else if (type.isAssignableFrom(List.class)) {
			final Class<?> clazz = (Class<?>) ((java.lang.reflect.ParameterizedType)filed.getGenericType()).getActualTypeArguments()[0];
			final Object objectValue = getObjectValue(filed, clazz);
			final List list = new ArrayList();
			list.add(objectValue);
			return list;
		} else {
			return type.newInstance();
		}
	}

	public static String naturalOrder(String pWord) {
		StringBuilder sb = new StringBuilder();
		String[] words = pWord.replaceAll("_", " ").split("\\s");
		for (int i = 0; i < words.length; i++) {
			if (i > 0)
				sb.append(" ");
			if (words[i].length() > 0) {
				sb.append(Character.toUpperCase(words[i].charAt(0)));
				if (words[i].length() > 1) {
					sb.append(words[i].substring(1));
				}
			}
		}
		return sb.toString();
	}

}