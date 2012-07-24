package org.openstack.ceilometer.model;

public class MeterEvent {

	private Object source;
	
	private String name;
	
	private String userId;
	
	private String projectId;
	
	private String resourceId;
	
	private String metadata;
	
	//meter
	
	private String type;
	
	private Number volume;
	
	private Integer duration;
	
	private long timestamp;
	
	
	private String messageId;
	
	private String messageSignature;

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Number getVolume() {
		return volume;
	}

	public void setVolume(Number volume) {
		this.volume = volume;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMessageSignature() {
		return messageSignature;
	}

	public void setMessageSignature(String messageSignature) {
		this.messageSignature = messageSignature;
	}

	@Override
	public String toString() {
		return "Meter [source=" + source + ", name=" + name + ", userId="
				+ userId + ", projectId=" + projectId + ", resourceId="
				+ resourceId + ", metadata=" + metadata + ", type=" + type
				+ ", volume=" + volume + ", duration=" + duration
				+ ", timestamp=" + timestamp + ", messageId=" + messageId
				+ ", messageSignature=" + messageSignature + "]";
	}
	
}
