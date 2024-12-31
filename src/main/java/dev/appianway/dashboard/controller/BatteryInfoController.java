package dev.appianway.dashboard.controller;


import dev.appianway.dashboard.model.dto.BatteryInfoDTO;
import dev.appianway.dashboard.model.entity.BatteryInfo;
import dev.appianway.dashboard.model.entity.BatteryInfoType;
import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.service.BatteryInfoService;
import dev.appianway.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
// BatteryInfoController fora REST controller class that handles HTTP requests related to battery information.
@RestController
@RequestMapping("/dashboards/{dashboardId}/battery-info")
public class BatteryInfoController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private BatteryInfoService batteryInfoService;


    @GetMapping
    public ResponseEntity<List<BatteryInfoDTO>> getAllBatteryInfo(@PathVariable String dashboardId) {
        Optional<Dashboard> dashboardOpt = dashboardService.getDashboard(dashboardId);
        if (dashboardOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<BatteryInfo> batteryInfos = batteryInfoService.getAllBatteryInfos(dashboardOpt.get());
        System.out.println("batteryInfos: " + batteryInfos);
        List<BatteryInfoDTO> batteryInfoDTOs = batteryInfos.stream().map(batteryInfo -> new BatteryInfoDTO(batteryInfo)).collect(Collectors.toList());
        return ResponseEntity.ok(batteryInfoDTOs);
    }

    @GetMapping(params = "type")
    public ResponseEntity<List<BatteryInfoDTO>> getBatteryInfoByTypes(
            @PathVariable String dashboardId,
            @RequestParam String type) {
        Optional<Dashboard> dashboardOpt = dashboardService.getDashboard(dashboardId);
        if (dashboardOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<BatteryInfoType> batteryInfoTypes = Arrays.stream(type.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .map(BatteryInfoType::valueOf)
                .collect(Collectors.toList());

        List<BatteryInfoDTO> batteryInfoDTOs = batteryInfoService.getBatteryInfosByTypes(dashboardOpt.get(), batteryInfoTypes).stream().map(batteryInfo -> new BatteryInfoDTO(batteryInfo)).collect(Collectors.toList());
        return ResponseEntity.ok(batteryInfoDTOs);
    }
}
