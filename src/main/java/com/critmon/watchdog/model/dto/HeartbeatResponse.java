package com.critmon.watchdog.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HeartbeatResponse {

    private String id;
    private String status;

    @JsonProperty("last_heartbeat_at")
    private String lastHeartbeatAt;

    @JsonProperty("seconds_remaining")
    private Integer secondsRemaining;

    public HeartbeatResponse() {}

    public String getId() { return id; }
    public String getStatus() { return status; }
    public String getLastHeartbeatAt() { return lastHeartbeatAt; }
    public Integer getSecondsRemaining() { return secondsRemaining; }

    public void setId(String id) { this.id = id; }
    public void setStatus(String status) { this.status = status; }
    public void setLastHeartbeatAt(String lastHeartbeatAt) { this.lastHeartbeatAt = lastHeartbeatAt; }
    public void setSecondsRemaining(Integer secondsRemaining) { this.secondsRemaining = secondsRemaining; }
}
