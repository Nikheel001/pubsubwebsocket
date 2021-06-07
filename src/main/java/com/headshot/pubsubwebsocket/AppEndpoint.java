package com.headshot.pubsubwebsocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
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

	private static ConcurrentHashMap<String, Session> subs = new ConcurrentHashMap<String, Session>();

	private static final Logger log = Logger.getLogger(AppEndpoint.class.getSimpleName());
//	private static AppMessage store = new AppMessage();
	static int ctr = 0;

	/**
	 * share updates to all client connections
	 */
	public static void publish() {
//		String msg = store.toString();

		String msg = String.valueOf(++ctr % 120);

		subs.values().parallelStream().forEach((i) -> {
			try {
				i.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				log.severe(e.getMessage());
			}
		});
	}

	@OnOpen
	public void onopenconnection(Session session) {
		subs.put(session.getId(), session);
		log.info("subscriber added");
	}

	@OnClose
	public void oncloseconnection(Session session) {
		subs.remove(session.getId());
	}

	@OnError
	public void error(Session session, Throwable t) {
		subs.remove(session.getId());
		log.info(t.getMessage());
		log.info("Connection error.");
	}

}