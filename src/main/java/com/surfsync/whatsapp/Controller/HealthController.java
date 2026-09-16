package com.surfsync.whatsapp.Controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/api/health")
    public Map<String, String> health() {

        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

        return Map.of("application", "UP", "database", result != null && result == 1 ? "UP" : "DOWN", "openwa", "http://localhost:2785");
    }
}