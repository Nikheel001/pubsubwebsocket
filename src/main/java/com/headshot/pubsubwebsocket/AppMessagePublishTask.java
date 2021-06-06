package com.headshot.pubsubwebsocket;

import java.io.IOException;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.websocket.Session;

/**
 * 
 * @author nikheel.patel
 *
 */
public class AppMessagePublishTask implements Runnable {

	private static final Logger log = Logger.getLogger(AppMessagePublishTask.class.getSimpleName());
	AppMessage store;
	static int ctr = 0;
	Queue<Session> subsref;
	boolean status;
	
	public void setStatus(boolean status) {
		this.status = status;
	}

	public AppMessagePublishTask(Queue<Session> subs, AppMessage msg) {
		subsref = subs;
		store = msg;
		status = true;
	}

	void publish() {
//		String msg = store.toString();

		String msg = String.valueOf(++ctr%120);
		try {
			log.info("Publishing new update");
			log.info("connections: " + subsref.size());
			for (Session session : subsref) {
				session.getBasicRemote().sendText(msg);
			}
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage());
		}
	}

	public void run() {
		try {
			while (status) {
				publish();
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			log.log(Level.SEVERE, e.getMessage());
		}
	}

}
