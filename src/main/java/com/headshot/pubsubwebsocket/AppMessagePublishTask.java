package com.headshot.pubsubwebsocket;

import java.util.logging.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.Timeout;
import jakarta.ejb.TimerConfig;
import jakarta.ejb.TimerService;

/**
 * 
 * @author nikheel.patel
 *
 */
@Startup
@Singleton
public class AppMessagePublishTask {

	private static final Logger log = Logger.getLogger(AppMessagePublishTask.class.getSimpleName());
	@Resource
	TimerService tservice;

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