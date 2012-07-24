package org.openstack.ceilometer.collector.handlers;

import org.openstack.ceilometer.model.MeterEvent;
import org.openstack.ceilometer.model.NotificationInfo;

public interface NotificationHandler {

	public MeterEvent handle(NotificationInfo notificationInfo);
	
	public boolean supports(NotificationInfo notificationInfo);
	
}
