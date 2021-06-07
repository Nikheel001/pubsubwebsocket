package com.headshot.pubsubwebsocket;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
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
//	private static AppMessage store = new AppMessage();
	static int ctr = 0;

	/**
	 * share updates to all client connections
	 */
	public static void publish() {
//		String msg = store.toString();

		String msg = String.valueOf(++ctr % 120);
		try {
			log.info("Publishing new update");
			log.info("connections: " + subs.size());
			for (Session session : subs) {
				session.getBasicRemote().sendText(msg);
			}
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage());
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