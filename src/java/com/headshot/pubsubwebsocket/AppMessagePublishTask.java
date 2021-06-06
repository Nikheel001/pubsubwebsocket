package com.headshot.pubsubwebsocket;

import java.util.Queue;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.websocket.Session;

/**
 * 
 * @author nikheel.patel
 *
 */
@Startup
@Singleton
public class AppMessagePublishTask {

	private static final Logger log = Logger.getLogger(AppMessagePublishTask.class.getSimpleName());
	@Resource TimerService tservice;
/*
	AppMessage store;
	Queue<Session> subsref;


	public AppMessagePublishTask(Queue<Session> subs, AppMessage msg) {
		subsref = subs;
		store = msg;
	}
*/
	@Timeout
	void publish() {
		AppEndpoint.publish();
	}

	@PostConstruct
	public void init() {
		log.info("Initializing AppMessagePublishTask EJB.");
		tservice.createIntervalTimer(1000, 1000, new TimerConfig());
	}
}