package org.openstack.ceilometer.agents;

import org.openstack.ceilometer.rabbitmq.RabbitMqProducer;

public abstract class ComputeAgent {
	
	public static void main(String[] args) {
		
		RabbitMqProducer publisher = new RabbitMqProducer();
		publisher.setHost("192.168.1.38");
		publisher.setPassword("secret0");
		publisher.setRoutingKey("ceilometer.compute_agent");
		publisher.init();
		
		//LibvirtPoller libvirtPoller = new LibvirtPoller();
		//libvirtPoller.addListener(publisher);
	}

}
