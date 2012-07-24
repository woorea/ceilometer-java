package org.openstack.ceilometer.collector.handlers;

import org.openstack.ceilometer.model.MeterEvent;


public interface MessageHandler {

	public MeterEvent handle(String message);
	
}
