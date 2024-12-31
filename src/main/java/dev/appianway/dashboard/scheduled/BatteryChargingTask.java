package dev.appianway.dashboard.scheduled;

import dev.appianway.dashboard.model.dto.BatteryInfoDTO;
import dev.appianway.dashboard.model.entity.BatteryInfoType;
import dev.appianway.dashboard.service.BatteryInfoService;
import dev.appianway.dashboard.service.DashboardService;
import dev.appianway.dashboard.service.MetricService;
import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.model.entity.BatteryInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
// This Scheduled task simulates the battery charging and updates the battery metrics.
@Component
@Slf4j
public class BatteryChargingTask {
    private final SchedulerController schedulerController;
    private final DashboardService dashboardService;
    private final BatteryInfoService batteryInfoService;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${dashboard.battery.max-capacity}")
    private Float maxBatteryCapacity;
    @Value("${dashboard.battery.increment-capacity}")
    private Float batteryIncrementCapacity;

    public BatteryChargingTask(SchedulerController schedulerController,
                               DashboardService dashboardService,
                               BatteryInfoService batteryInfoService,
                               SimpMessagingTemplate messagingTemplate) {
        this.schedulerController = schedulerController;
        this.dashboardService = dashboardService;
        this.batteryInfoService = batteryInfoService;
        this.messagingTemplate = messagingTemplate;
    }


    // Runs every 30 second
    @Scheduled(fixedDelayString = "${task.battery-charging.delay:30000}")
    @Async("taskExecutor")
    public void updateBatteryMetrics() {
        try {
            if (!schedulerController.isReadyToStart()) {
                return;
            }

            List<Dashboard> chargingDashboards = dashboardService.getAllDashboardsWithChargingOn();

            for (Dashboard dashboard : chargingDashboards) {
                try {
                    processChargingDashboard(dashboard);
                } catch (Exception e) {
                    log.error("Error processing charging dashboard {}: {}", dashboard.getUuid(), e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error in updateBatteryMetrics: {}", e.getMessage());
        }
    }

    private void processChargingDashboard(Dashboard dashboard) {
        BatteryInfo batteryCapacity = batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.REMAINING_CAPACITY);
        if (batteryCapacity != null && batteryCapacity.getValue() < maxBatteryCapacity) {
            updateBatteryCapacity(dashboard, batteryCapacity);
        }
    }

    private void updateBatteryCapacity(Dashboard dashboard, BatteryInfo batteryCapacity) {
        Float newCapacity = calculateNewCapacity(batteryCapacity.getValue());
        batteryInfoService.updateBatteryInfo(batteryCapacity, BatteryInfoType.REMAINING_CAPACITY, newCapacity, "%");

        BatteryInfoDTO batteryInfoDTO = new BatteryInfoDTO(batteryCapacity);
        messagingTemplate.convertAndSend("/topic/battery_capacity/" + dashboard.getUuid(), batteryInfoDTO);
    }

    private Float calculateNewCapacity(Float currentCapacity) {
        return Math.min(currentCapacity + batteryIncrementCapacity, maxBatteryCapacity);
    }
}
