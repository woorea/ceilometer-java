package org.openstack.ceilometer.api;

import org.glassfish.jersey.internal.inject.AbstractBinder;

public class MongoDbBinder extends AbstractBinder {
	
	private String host;
	
	private Integer port;
	
	private String username;
	
	private String password;

	@Override
	protected void configure() {
		MongoDbService mongoDbService = new MongoDbService();
		mongoDbService.start();
		System.out.println(mongoDbService);
		bind(mongoDbService).to(MongoDbService.class);
	}
	
}
