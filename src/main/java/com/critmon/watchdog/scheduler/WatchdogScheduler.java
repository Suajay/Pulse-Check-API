package com.critmon.watchdog.scheduler;

import com.critmon.watchdog.model.Monitor;
import com.critmon.watchdog.model.MonitorStatus;
import com.critmon.watchdog.repository.MonitorRepository;
import com.critmon.watchdog.service.AlertService;
import com.critmon.watchdog.service.MonitorService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
public class WatchdogScheduler {

    private final MonitorRepository repository;
    private final AlertService alertService;
    private final MonitorService monitorService;

    public WatchdogScheduler(MonitorRepository repository,
                             AlertService alertService,
                             MonitorService monitorService) {
        this.repository = repository;
        this.alertService = alertService;
        this.monitorService = monitorService;
    }

    @Scheduled(fixedRate = 5000)
    public void checkMonitors() {
        List<Monitor> activeMonitors = repository.findAllByStatus(MonitorStatus.ACTIVE);

        for (Monitor monitor : activeMonitors) {
            synchronized (monitor) {
                if (monitor.getStatus() != MonitorStatus.ACTIVE) {
                    continue;
                }

                Instant reference = monitor.getLastHeartbeatAt() != null
                        ? monitor.getLastHeartbeatAt()
                        : monitor.getCreatedAt();

                long elapsed = Duration.between(reference, Instant.now()).toSeconds();

                if (elapsed > monitor.getTimeout()) {
                    alertService.emitAlert(monitor);
                    monitorService.markDown(monitor.getId());
                }
            }
        }
    }
}
