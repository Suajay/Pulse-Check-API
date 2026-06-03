package com.critmon.watchdog.repository;

import com.critmon.watchdog.model.Monitor;
import com.critmon.watchdog.model.MonitorStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MonitorRepository {

    private final ConcurrentHashMap<String, Monitor> store = new ConcurrentHashMap<>();

    public Monitor save(Monitor monitor) {
        store.put(monitor.getId(), monitor);
        return monitor;
    }

    public boolean create(Monitor monitor) {
        return store.putIfAbsent(monitor.getId(), monitor) == null;
    }

    public Optional<Monitor> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Monitor> findAll() {
        return new ArrayList<>(store.values());
    }

    public List<Monitor> findAllByStatus(MonitorStatus status) {
        return store.values().stream()
                .filter(m -> m.getStatus() == status)
                .toList();
    }

    public boolean existsById(String id) {
        return store.containsKey(id);
    }
}
