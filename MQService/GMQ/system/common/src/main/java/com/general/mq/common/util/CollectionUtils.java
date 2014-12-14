package com.general.mq.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class CollectionUtils {
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.size() == 0;
	}

	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

	public static <T> T first(Collection<T> collection) {
		if (isNotEmpty(collection)) {
			return collection.iterator().next();
		}
		return null;
	}

	public static <T> T find(Collection<T> collection, Matcher<T> matcher) {
		if (isNotEmpty(collection)) {
			final Iterator<T> iterator = collection.iterator();
			while (iterator.hasNext()) {
				T t = iterator.next();
				if (matcher.match(t)) {
					return t;
				}
			}
		}
		return null;
	}

	public static <T> Collection<T> select(Collection<T> collection, Matcher<T> matcher) {
		final Collection<T> select = new ArrayList<T>();
		if (isNotEmpty(collection)) {
			final Iterator<T> iterator = collection.iterator();
			while (iterator.hasNext()) {
				T t = iterator.next();
				if (matcher.match(t)) {
					select.add(t);
				}
			}
		}
		return select;
	}

	public static <T> void forEach(Collection<T> collection, Closure<T> closure) {

			if (isNotEmpty(collection)) {
				final Iterator<T> iterator = collection.iterator();
				while (iterator.hasNext()) {
					T t = iterator.next();
					closure.call(t);
				}
			}
		}
	
	
	/**
	 * One to One way of finding Key from Value in Map
	 * @param map
	 * @param value
	 * @returnkey
	 */
	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (value.equals(entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
	
	/**
	 * Many to One way of finding Keys from value
	 * @param map
	 * @param value
	 * @return keys
	 */
	public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
	    Set<T> keys = new HashSet<T>();
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (value.equals(entry.getValue())) {
	            keys.add(entry.getKey());
	        }
	    }
	    return keys;
	}
}
