package org.openstack.ceilometer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.openstack.ceilometer.jackson.JacksonJsonContext;
import org.openstack.ceilometer.model.MeterEvent;

public abstract class Poller implements Runnable {
	
	private long pollingInterval = 60000;
	
	private Set<MessageListener> listeners = new HashSet<MessageListener>();

	public void setPollingInterval(long pollingInterval) {
		this.pollingInterval = pollingInterval;
	}
	
	public void addListener(MessageListener listener) {
		listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		onStart();
		while(true) {
			try {
				for(MeterEvent meterEvent : poll()) {
					try {
						String message = JacksonJsonContext.OBJECT_MAPPER.writeValueAsString(meterEvent);
						for(MessageListener listener : listeners) {
							listener.onMessage(message);
						}
					} catch (Exception e) {
						throw new RuntimeException(e.getMessage(), e);
					}
				}
				Thread.sleep(pollingInterval);
			} catch (InterruptedException e) { }
		}
	}
	
	protected void onStart() {
		
	}
	
	public abstract Collection<MeterEvent> poll();
	
}
