package com.critmon.watchdog.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreateMonitorRequest {

    @NotBlank(message = "id must not be blank")
    private String id;

    @Min(value = 1, message = "timeout must be at least 1 second")
    @Max(value = 86400, message = "timeout must not exceed 86400 seconds")
    private int timeout;

    @NotBlank(message = "alert_email must not be blank")
    @Email(message = "alert_email must be a valid email address")
    @JsonProperty("alert_email")
    private String alertEmail;

    public String getId() { return id; }
    public int getTimeout() { return timeout; }
    public String getAlertEmail() { return alertEmail; }

    public void setId(String id) { this.id = id; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
    public void setAlertEmail(String alertEmail) { this.alertEmail = alertEmail; }
}
