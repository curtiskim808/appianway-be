package dev.appianway.dashboard.controller;


import dev.appianway.dashboard.model.dto.MetricDTO;
import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.model.entity.Metric;
import dev.appianway.dashboard.model.entity.MetricType;
import dev.appianway.dashboard.service.DashboardService;
import dev.appianway.dashboard.service.MetricService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;
import java.util.stream.Collectors;
// MetricController for a REST controller class that handles HTTP requests related to metrics.
@RestController
@RequestMapping("/dashboards/{dashboardId}/metrics")
public class MetricController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private MetricService metricService;

    // GET /api/v1/dashboards/{id}/metrics
    @GetMapping
    public ResponseEntity<List<MetricDTO>> getAllMetrics(@PathVariable String dashboardId) {
        Optional<Dashboard> dashboardOpt = dashboardService.getDashboard(dashboardId);
        if (dashboardOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Metric> metrics = metricService.getAllMetrics(dashboardOpt.get());
        List<MetricDTO> metricDTOs = metrics.stream().map(metric -> new MetricDTO(metric)).collect(Collectors.toList());
        return ResponseEntity.ok(metricDTOs);
    }

    // GET /api/v1/dashboards/{id}/metrics?type=power-input,motor-rpm
    @GetMapping(params = "type")
    public ResponseEntity<List<MetricDTO>> getMetricsByTypes(
            @PathVariable String dashboardId,
            @RequestParam String type) {
        Optional<Dashboard> dashboardOpt = dashboardService.getDashboard(dashboardId);
        if (dashboardOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<MetricType> metricTypes = Arrays.stream(type.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .map(MetricType::valueOf)
                .collect(Collectors.toList());

        List<Metric> metrics = metricService.getMetricsByTypes(dashboardOpt.get(), metricTypes);
        List<MetricDTO> metricDTOs = metrics.stream().map(metric -> new MetricDTO(metric)).collect(Collectors.toList());
        return ResponseEntity.ok(metricDTOs);
    }

    // PUT /api/v1/dashboards/{id}/metrics
    @PutMapping
    public ResponseEntity<MetricDTO> updateMetric(
            @PathVariable String dashboardId,
            @RequestBody UpdateMetricRequest request) {
        Optional<Dashboard> dashboardOpt = dashboardService.getDashboard(dashboardId);
        if (dashboardOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MetricType type = MetricType.valueOf(request.getType().toUpperCase());
        Metric updatedMetric = metricService.updateMetric(request.getId(), type, request.getValue(), request.getUnit());

        MetricDTO updatedMetricDTO = new MetricDTO(updatedMetric);

        return ResponseEntity.ok(updatedMetricDTO);
    }

    // DTO for updating metrics
    @Data
    public static class UpdateMetricRequest {
        private Long id;
        private String type;
        private Float value;
        private String unit;
    }
}

