package dev.appianway.dashboard.controller;

import dev.appianway.dashboard.model.dto.DashboardDTO;
import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.service.DashboardService;
import dev.appianway.dashboard.service.InitialSetupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;
// DashboardController for a REST controller class that handles HTTP requests related to dashboards.
@RestController
@RequestMapping("/dashboards")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private InitialSetupService initialSetupService;

    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboard() {
        Dashboard dashboard = dashboardService.getFirstDashboard();
        if (dashboard == null) {
            return ResponseEntity.notFound().build();
        }
        DashboardDTO dashboardDTO = new DashboardDTO(dashboard);

        return ResponseEntity.ok(dashboardDTO);
    }

    // Endpoint to get a dashboard by ID (optional)
    @GetMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DashboardDTO> getDashboard(@PathVariable String uuid) {
        return dashboardService.getDashboard(uuid)
                .map(dashboard -> ResponseEntity.ok(new DashboardDTO(dashboard)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteDashboard(@PathVariable String uuid) {
        dashboardService.deleteDashboard(uuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/setup")
    public ResponseEntity<Void> setupInitialData() {
        initialSetupService.setupInitialData();
        return ResponseEntity.noContent().build();

    }

    @PostMapping("/new-dashboard-set")
    public ResponseEntity<DashboardDTO> createNewDashboardSet() {
        Dashboard dashboard = initialSetupService.createNewDashboardSet();
        DashboardDTO dashboardDTO = new DashboardDTO(dashboard);
        return ResponseEntity.ok(dashboardDTO);
    }
}
