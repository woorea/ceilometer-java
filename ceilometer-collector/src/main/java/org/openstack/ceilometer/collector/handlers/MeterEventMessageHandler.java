package org.openstack.ceilometer.collector.handlers;

import java.io.IOException;

import org.openstack.ceilometer.jackson.JacksonJsonContext;
import org.openstack.ceilometer.model.MeterEvent;

public class MeterEventMessageHandler implements MessageHandler {

	@Override
	public MeterEvent handle(String message) {
		try {
			return JacksonJsonContext.OBJECT_MAPPER.readValue(message, MeterEvent.class);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
}
