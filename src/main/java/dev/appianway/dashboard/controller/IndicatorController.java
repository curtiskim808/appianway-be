package dev.appianway.dashboard.controller;

import dev.appianway.dashboard.model.dto.BatteryInfoDTO;
import dev.appianway.dashboard.model.dto.IndicatorDTO;
import dev.appianway.dashboard.model.entity.BatteryInfo;
import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.model.entity.Indicator;
import dev.appianway.dashboard.model.entity.IndicatorType;
import dev.appianway.dashboard.service.DashboardService;
import dev.appianway.dashboard.service.IndicatorService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboards/{dashboardId}/indicators")
public class IndicatorController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private IndicatorService indicatorService;

    // GET /api/v1/dashboards/{id}/indicators
    @GetMapping
    public ResponseEntity<List<IndicatorDTO>> getAllIndicators(@PathVariable String dashboardId) {
        Optional<Dashboard> dashboardOpt = dashboardService.getDashboard(dashboardId);
        if (dashboardOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Indicator> indicators = indicatorService.getAllIndicators(dashboardOpt.get());
        List<IndicatorDTO> indicatorDTOs = indicators.stream().map(indicator -> new IndicatorDTO(indicator)).collect(Collectors.toList());
        return ResponseEntity.ok(indicatorDTOs);
    }


    // GET /api/v1/dashboards/{id}/indicators?type=parking-brake,engine-status
    @GetMapping(params = "type")
    public ResponseEntity<List<IndicatorDTO>> getIndicatorsByTypes(
            @PathVariable String dashboardId,
            @RequestParam String type) {
        Optional<Dashboard> dashboardOpt = dashboardService.getDashboard(dashboardId);
        if (dashboardOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<IndicatorType> indicatorTypes = Arrays.stream(type.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .map(IndicatorType::valueOf)
                .collect(Collectors.toList());

        List<Indicator> indicators = indicatorService.getIndicatorsByTypes(dashboardOpt.get(), indicatorTypes);
        List<IndicatorDTO> indicatorDTOs = indicators.stream().map(indicator -> new IndicatorDTO(indicator)).collect(Collectors.toList());
        return ResponseEntity.ok(indicatorDTOs);
    }

    // PUT /api/v1/dashboards/{id}/indicators
    @PutMapping
    public ResponseEntity<IndicatorDTO> updateIndicator(
            @PathVariable String dashboardId,
            @RequestBody UpdateIndicatorRequest request) {
        Optional<Dashboard> dashboardOpt = dashboardService.getDashboard(dashboardId);
        if (dashboardOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        IndicatorType type = IndicatorType.valueOf(request.getType().toUpperCase());

        Indicator updatedIndicator = indicatorService.updateIndicator(request.getId(), request.getStatus());
        IndicatorDTO updatedIndicatorDTO = new IndicatorDTO(updatedIndicator);

        return ResponseEntity.ok(updatedIndicatorDTO);
    }

    // DTO for updating indicators
    @Data
    public static class UpdateIndicatorRequest {
        private Long id;
        private String type;
        private Boolean status;
    }
}
