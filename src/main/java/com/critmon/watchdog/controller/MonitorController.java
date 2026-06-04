package com.critmon.watchdog.controller;

import com.critmon.watchdog.model.dto.CreateMonitorRequest;
import com.critmon.watchdog.model.dto.HeartbeatResponse;
import com.critmon.watchdog.model.dto.MonitorResponse;
import com.critmon.watchdog.service.MonitorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monitors")
public class MonitorController {

    private final MonitorService monitorService;

    public MonitorController(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @PostMapping
    public ResponseEntity<MonitorResponse> register(@Valid @RequestBody CreateMonitorRequest request) {
        MonitorResponse response = monitorService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/heartbeat")
    public ResponseEntity<HeartbeatResponse> heartbeat(@PathVariable String id) {
        HeartbeatResponse response = monitorService.heartbeat(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/pause")
    public ResponseEntity<MonitorResponse> pause(@PathVariable String id) {
        MonitorResponse response = monitorService.pause(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonitorResponse> getById(@PathVariable String id) {
        MonitorResponse response = monitorService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MonitorResponse>> getAll() {
        return ResponseEntity.ok(monitorService.getAll());
    }
}
