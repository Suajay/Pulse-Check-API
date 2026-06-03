package com.critmon.watchdog.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MonitorResponse {

    private String id;
    private int timeout;

    @JsonProperty("alert_email")
    private String alertEmail;

    private String status;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("last_heartbeat_at")
    private String lastHeartbeatAt;

    @JsonProperty("seconds_remaining")
    private Integer secondsRemaining;

    public MonitorResponse() {}

    public String getId() { return id; }
    public int getTimeout() { return timeout; }
    public String getAlertEmail() { return alertEmail; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
    public String getLastHeartbeatAt() { return lastHeartbeatAt; }
    public Integer getSecondsRemaining() { return secondsRemaining; }

    public void setId(String id) { this.id = id; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
    public void setAlertEmail(String alertEmail) { this.alertEmail = alertEmail; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setLastHeartbeatAt(String lastHeartbeatAt) { this.lastHeartbeatAt = lastHeartbeatAt; }
    public void setSecondsRemaining(Integer secondsRemaining) { this.secondsRemaining = secondsRemaining; }
}
