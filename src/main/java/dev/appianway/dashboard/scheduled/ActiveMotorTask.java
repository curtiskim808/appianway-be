package dev.appianway.dashboard.scheduled;

import dev.appianway.dashboard.model.dto.BatteryInfoDTO;
import dev.appianway.dashboard.model.entity.BatteryInfo;
import dev.appianway.dashboard.service.BatteryInfoService;
import dev.appianway.dashboard.service.DashboardService;
import dev.appianway.dashboard.service.MetricService;
import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.model.entity.BatteryInfoType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
// This Scheduled task simulates the motor being active and updates the battery metrics.
@Slf4j
@Component
public class ActiveMotorTask {

    private final SchedulerController schedulerController;
    private final DashboardService dashboardService;
    private final BatteryInfoService batteryInfoService;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${dashboard.battery.min-capacity}")
    private Float minBatteryCapacity;

    @Value("${dashboard.battery.decrement-capacity}")
    private Float batteryDecrementCapacity;

    @Value("${dashboard.battery.max-temperature}")
    private Float maxBatteryTemperature;

    @Value("${dashboard.battery.increment-temperature}")
    private Float batteryIncrementTemperature;

    public ActiveMotorTask(SchedulerController schedulerController,
                           DashboardService dashboardService,
                           BatteryInfoService batteryInfoService,
                           SimpMessagingTemplate messagingTemplate) {
        this.schedulerController = schedulerController;
        this.dashboardService = dashboardService;
        this.batteryInfoService = batteryInfoService;
        this.messagingTemplate = messagingTemplate;
    }

    // Runs every 60 seconds
    @Async("taskExecutor")
    @Scheduled(fixedDelayString = "${task.active-motor.delay:60000}")
    public void updateBatteryMetrics() {
        try {
            if (!schedulerController.isReadyToStart()) {
                log.debug("Scheduler not ready to start");
                return;
            }

            List<Dashboard> activeDashboards = dashboardService.getAllDashboardsWithMotorOn();
            log.debug("Found {} active dashboards", activeDashboards.size());

            for (Dashboard dashboard : activeDashboards) {
                try {
                    processActiveDashboard(dashboard);
                } catch (Exception e) {
                    log.error("Error processing active dashboard {}: {}",
                            dashboard.getUuid(), e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            log.error("Error in updateBatteryMetrics: {}", e.getMessage(), e);
        }
    }

    private void processActiveDashboard(Dashboard dashboard) {
        String dashboardId = dashboard.getUuid();
        log.debug("Processing active dashboard: {}", dashboardId);

        updateBatteryCapacity(dashboard, dashboardId);
        updateBatteryTemperature(dashboard, dashboardId);
    }

    private void updateBatteryCapacity(Dashboard dashboard, String dashboardId) {
        try {
            BatteryInfo batteryCapacity = batteryInfoService.getBatteryInfo(
                    dashboard, BatteryInfoType.REMAINING_CAPACITY);

            if (batteryCapacity != null && batteryCapacity.getValue() > minBatteryCapacity) {
                Float newCapacity = calculateNewCapacity(batteryCapacity.getValue());
                log.debug("Updating battery capacity for dashboard {}: {} -> {}",
                        dashboardId, batteryCapacity.getValue(), newCapacity);

                batteryInfoService.updateBatteryInfo(
                        batteryCapacity,
                        BatteryInfoType.REMAINING_CAPACITY,
                        newCapacity,
                        "%"
                );

                broadcastBatteryUpdate(dashboardId, batteryCapacity, "capacity");
            }
        } catch (Exception e) {
            log.error("Error updating battery capacity for dashboard {}: {}",
                    dashboardId, e.getMessage(), e);
        }
    }

    private void updateBatteryTemperature(Dashboard dashboard, String dashboardId) {
        try {
            BatteryInfo batteryTemperature = batteryInfoService.getBatteryInfo(
                    dashboard, BatteryInfoType.TEMPERATURE);

            if (batteryTemperature != null && batteryTemperature.getValue() < maxBatteryTemperature) {
                Float newTemperature = calculateNewTemperature(batteryTemperature.getValue());
                log.debug("Updating battery temperature for dashboard {}: {} -> {}",
                        dashboardId, batteryTemperature.getValue(), newTemperature);

                batteryInfoService.updateBatteryInfo(
                        batteryTemperature,
                        BatteryInfoType.TEMPERATURE,
                        newTemperature,
                        "°C"
                );

                broadcastBatteryUpdate(dashboardId, batteryTemperature, "temperature");
            }
        } catch (Exception e) {
            log.error("Error updating battery temperature for dashboard {}: {}",
                    dashboardId, e.getMessage(), e);
        }
    }

    private Float calculateNewCapacity(Float currentCapacity) {
        return Math.max(currentCapacity - batteryDecrementCapacity, minBatteryCapacity);
    }

    private Float calculateNewTemperature(Float currentTemperature) {
        return Math.min(currentTemperature + batteryIncrementTemperature, maxBatteryTemperature);
    }

    private void broadcastBatteryUpdate(String dashboardId, BatteryInfo batteryInfo, String type) {
        try {
            BatteryInfoDTO batteryInfoDTO = new BatteryInfoDTO(batteryInfo);
            String topic = String.format("/topic/battery_%s/%s", type, dashboardId);
            messagingTemplate.convertAndSend(topic, batteryInfoDTO);
            log.debug("Broadcast battery {} update for dashboard {}", type, dashboardId);
        } catch (Exception e) {
            log.error("Error broadcasting battery {} update for dashboard {}: {}",
                    type, dashboardId, e.getMessage(), e);
        }
    }
}
