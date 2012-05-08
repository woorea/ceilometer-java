package org.openstack.ceilometer;

import java.math.BigDecimal;

public class Usage {

	private String id;
	private String source;
	private String userId;
	private String projectId;
	private String resourceId;
	private String counterType;
	private BigDecimal counterVolume;
	private BigDecimal counterDuration;
	private Long counterTimestamp;
	private String payload;
	private String signature;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
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
	public String getCounterType() {
		return counterType;
	}
	public void setCounterType(String counterType) {
		this.counterType = counterType;
	}
	public BigDecimal getCounterVolume() {
		return counterVolume;
	}
	public void setCounterVolume(BigDecimal counterVolume) {
		this.counterVolume = counterVolume;
	}
	public BigDecimal getCounterDuration() {
		return counterDuration;
	}
	public void setCounterDuration(BigDecimal counterDuration) {
		this.counterDuration = counterDuration;
	}
	public Long getCounterTimestamp() {
		return counterTimestamp;
	}
	public void setCounterTimestamp(Long counterTimestamp) {
		this.counterTimestamp = counterTimestamp;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
}
