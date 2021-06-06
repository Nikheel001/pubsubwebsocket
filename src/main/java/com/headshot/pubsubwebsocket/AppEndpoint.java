package com.headshot.pubsubwebsocket;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

/**
 * 
 * @author nikheel.patel
 *
 */
@ServerEndpoint("/app")
public class AppEndpoint {

	private static final Logger log = Logger.getLogger(AppEndpoint.class.getSimpleName());
	private static Queue<Session> subs = new ConcurrentLinkedQueue<Session>();
	private static AppMessage store = new AppMessage();
	private static AppMessagePublishTask publishTask = new AppMessagePublishTask(subs, store);
	private static Thread publisher;

	public AppEndpoint() {
		if (publisher == null) {
			publisher = new Thread(publishTask);
			publisher.setDaemon(true);
			publisher.start();
		}
	}

	/**
	 * O(1)
	 * 
	 * @param session
	 * @param config
	 */
	@OnOpen
	public void onopenconnection(Session session) {
		// add to sub list
		subs.add(session);
		log.info("subscriber added");
	}

	/**
	 * O(n)
	 * 
	 * @param session
	 * @param config
	 */
	@OnClose
	public void oncloseconnection(Session session) {
		subs.remove(session);
	}

	/**
	 * O(n)
	 * 
	 * @param session
	 * @param t
	 */
	@OnError
	public void error(Session session, Throwable t) {
		subs.remove(session);
		log.info(t.getMessage());
		log.info("Connection error.");
	}

}