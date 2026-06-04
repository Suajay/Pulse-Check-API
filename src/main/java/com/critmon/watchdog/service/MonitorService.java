package com.critmon.watchdog.service;

import com.critmon.watchdog.exception.MonitorConflictException;
import com.critmon.watchdog.exception.MonitorNotFoundException;
import com.critmon.watchdog.model.Monitor;
import com.critmon.watchdog.model.MonitorStatus;
import com.critmon.watchdog.model.dto.CreateMonitorRequest;
import com.critmon.watchdog.model.dto.HeartbeatResponse;
import com.critmon.watchdog.model.dto.MonitorResponse;
import com.critmon.watchdog.repository.MonitorRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class MonitorService {

    private final MonitorRepository repository;

    public MonitorService(MonitorRepository repository) {
        this.repository = repository;
    }

    public MonitorResponse register(CreateMonitorRequest request) {
        Monitor monitor = new Monitor(request.getId(), request.getTimeout(), request.getAlertEmail());
        boolean created = repository.create(monitor);
        if (!created) {
            throw new MonitorConflictException(
                    "MONITOR_ALREADY_EXISTS",
                    "A monitor with id '" + request.getId() + "' already exists"
            );
        }
        return toResponse(monitor);
    }

    public HeartbeatResponse heartbeat(String id) {
        Monitor monitor = repository.findById(id)
                .orElseThrow(() -> new MonitorNotFoundException(id));

        synchronized (monitor) {
            if (monitor.getStatus() == MonitorStatus.DOWN) {
                throw new MonitorConflictException(
                        "MONITOR_IS_DOWN",
                        "Monitor '" + id + "' is down and must be re-registered"
                );
            }
            monitor.setStatus(MonitorStatus.ACTIVE);
            monitor.setLastHeartbeatAt(Instant.now());
        }

        return toHeartbeatResponse(monitor);
    }

    public MonitorResponse pause(String id) {
        Monitor monitor = repository.findById(id)
                .orElseThrow(() -> new MonitorNotFoundException(id));

        synchronized (monitor) {
            if (monitor.getStatus() == MonitorStatus.PAUSED) {
                throw new MonitorConflictException(
                        "MONITOR_ALREADY_PAUSED",
                        "Monitor '" + id + "' is already paused"
                );
            }
            if (monitor.getStatus() == MonitorStatus.DOWN) {
                throw new MonitorConflictException(
                        "MONITOR_IS_DOWN",
                        "Monitor '" + id + "' is already down and cannot be paused"
                );
            }
            monitor.setStatus(MonitorStatus.PAUSED);
        }

        return toResponse(monitor);
    }

    public MonitorResponse getById(String id) {
        Monitor monitor = repository.findById(id)
                .orElseThrow(() -> new MonitorNotFoundException(id));
        return toResponse(monitor);
    }

    public List<MonitorResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public void markDown(String id) {
        repository.findById(id).ifPresent(monitor -> {
            synchronized (monitor) {
                if (monitor.getStatus() == MonitorStatus.ACTIVE) {
                    monitor.setStatus(MonitorStatus.DOWN);
                }
            }
        });
    }

    public Integer computeSecondsRemaining(Monitor monitor) {
        return switch (monitor.getStatus()) {
            case DOWN -> 0;
            case PAUSED -> null;
            case ACTIVE -> {
                Instant reference = monitor.getLastHeartbeatAt() != null
                        ? monitor.getLastHeartbeatAt()
                        : monitor.getCreatedAt();
                long elapsed = Duration.between(reference, Instant.now()).toSeconds();
                yield (int) Math.max(0, monitor.getTimeout() - elapsed);
            }
        };
    }

    private MonitorResponse toResponse(Monitor monitor) {
        MonitorResponse response = new MonitorResponse();
        response.setId(monitor.getId());
        response.setTimeout(monitor.getTimeout());
        response.setAlertEmail(monitor.getAlertEmail());
        response.setStatus(monitor.getStatus().name().toLowerCase());
        response.setCreatedAt(monitor.getCreatedAt().toString());
        response.setLastHeartbeatAt(
                monitor.getLastHeartbeatAt() != null ? monitor.getLastHeartbeatAt().toString() : null
        );
        response.setSecondsRemaining(computeSecondsRemaining(monitor));
        return response;
    }

    private HeartbeatResponse toHeartbeatResponse(Monitor monitor) {
        HeartbeatResponse response = new HeartbeatResponse();
        response.setId(monitor.getId());
        response.setStatus(monitor.getStatus().name().toLowerCase());
        response.setLastHeartbeatAt(
                monitor.getLastHeartbeatAt() != null ? monitor.getLastHeartbeatAt().toString() : null
        );
        response.setSecondsRemaining(computeSecondsRemaining(monitor));
        return response;
    }
}
