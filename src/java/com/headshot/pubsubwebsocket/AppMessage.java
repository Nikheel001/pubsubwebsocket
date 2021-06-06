package com.headshot.pubsubwebsocket;

import java.util.HashMap;

/**
 * 
 * @author nikheel.patel
 *
 */
public class AppMessage {

	HashMap<String, Integer> products = new HashMap<String, Integer>();

	/**
	 * 
	 * @param key
	 * @param value
	 * @param buy
	 */
	public synchronized void update(String key, Integer value, boolean buy) {

		if (buy) {
			products.put(key, products.getOrDefault(key, 0) - value);
		} else {
			products.put(key, products.getOrDefault(key, 0) + value);
		}
	}

	@Override
	public String toString() {
		return products.toString();
	}
}
