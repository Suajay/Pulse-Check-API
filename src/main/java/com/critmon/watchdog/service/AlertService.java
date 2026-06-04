package com.critmon.watchdog.service;

import com.critmon.watchdog.model.Monitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AlertService {

    private static final Logger log = LoggerFactory.getLogger(AlertService.class);

    @Value("${alert.email.override:}")
    private String alertEmailOverride;

    public void emitAlert(Monitor monitor) {
        String email = (alertEmailOverride != null && !alertEmailOverride.isBlank())
                ? alertEmailOverride
                : monitor.getAlertEmail();

        String alertJson = String.format(
                "{\"ALERT\": \"Device %s is down!\", \"alert_email\": \"%s\", \"time\": \"%s\"}",
                monitor.getId(),
                email,
                Instant.now().toString()
        );

        log.warn(alertJson);
    }
}
