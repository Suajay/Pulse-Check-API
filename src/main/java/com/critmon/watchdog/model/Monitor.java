package com.critmon.watchdog.model;

import java.time.Instant;

public class Monitor {

    private final String id;
    private final int timeout;
    private final String alertEmail;
    private volatile MonitorStatus status;
    private final Instant createdAt;
    private volatile Instant lastHeartbeatAt;

    public Monitor(String id, int timeout, String alertEmail) {
        this.id = id;
        this.timeout = timeout;
        this.alertEmail = alertEmail;
        this.status = MonitorStatus.ACTIVE;
        this.createdAt = Instant.now();
        this.lastHeartbeatAt = null;
    }

    public String getId() { return id; }
    public int getTimeout() { return timeout; }
    public String getAlertEmail() { return alertEmail; }
    public MonitorStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getLastHeartbeatAt() { return lastHeartbeatAt; }

    public void setStatus(MonitorStatus status) { this.status = status; }
    public void setLastHeartbeatAt(Instant lastHeartbeatAt) { this.lastHeartbeatAt = lastHeartbeatAt; }
}
