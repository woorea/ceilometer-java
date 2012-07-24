package org.openstack.ceilometer.collector.handlers;

import org.openstack.ceilometer.model.MeterEvent;
import org.openstack.ceilometer.model.NotificationInfo;

public class InstanceNotificationHandler implements NotificationHandler {

	private static final String COMPUTE_INSTANCE = "compute.instance.";
	
	@Override
	public MeterEvent handle(NotificationInfo notificationInfo) {
		
		String action = notificationInfo.getEventType().substring(COMPUTE_INSTANCE.length());
		
		MeterEvent m = new MeterEvent();
		m.setSource("?");
		m.setName("instance");
		m.setUserId(notificationInfo.getUserId());
		m.setProjectId(notificationInfo.getProjectId());
		m.setResourceId((String) notificationInfo.getPayload().get("instance_id"));
		m.setTimestamp(System.currentTimeMillis());
		m.setType("delta");
		m.setVolume(1);
		m.setMetadata(notificationInfo.getPayload().toString());
		
		return m;
	}

	@Override
	public boolean supports(NotificationInfo notificationInfo) {
		return notificationInfo.getEventType().startsWith(COMPUTE_INSTANCE);
	}

	

}
