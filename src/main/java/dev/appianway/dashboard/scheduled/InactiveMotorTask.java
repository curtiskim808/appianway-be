package dev.appianway.dashboard.scheduled;

import dev.appianway.dashboard.model.dto.BatteryInfoDTO;
import dev.appianway.dashboard.model.entity.BatteryInfoType;
import dev.appianway.dashboard.service.BatteryInfoService;
import dev.appianway.dashboard.service.DashboardService;
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
// This scheduled task simulates the motor being inactive and updates the battery metrics.
@Component
@Slf4j
public class InactiveMotorTask {
    private final SchedulerController schedulerController;
    private final DashboardService dashboardService;
    private final BatteryInfoService batteryInfoService;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${dashboard.battery.normal-temperature}")
    private Float normalBatteryTemperature;
    @Value("${dashboard.battery.decrement-temperature}")
    private Float batteryDecrementTemperature;

    public InactiveMotorTask(SchedulerController schedulerController,
                             DashboardService dashboardService,
                             BatteryInfoService batteryInfoService,
                             SimpMessagingTemplate messagingTemplate) {
        this.schedulerController = schedulerController;
        this.dashboardService = dashboardService;
        this.batteryInfoService = batteryInfoService;
        this.messagingTemplate = messagingTemplate;
    }

    // Runs every 13 second
    @Scheduled(fixedDelayString = "${task.inactive-motor.delay:120000}")
    @Async("taskExecutor")
    public void updateBatteryMetrics() {
        try {
            if (!schedulerController.isReadyToStart()) {
                return;
            }

            List<Dashboard> inactiveDashboards = dashboardService.getAllDashboardsWithMotorOffAndChargingOff();

            for (Dashboard dashboard : inactiveDashboards) {
                try {
                    processInactiveDashboard(dashboard);
                } catch (Exception e) {
                    log.error("Error processing dashboard {}: {}", dashboard.getUuid(), e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error in updateBatteryMetrics: {}", e.getMessage());
        }
    }

    private void processInactiveDashboard(Dashboard dashboard) {
        BatteryInfo batteryTemperature = batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.TEMPERATURE);
        if (batteryTemperature != null && batteryTemperature.getValue() > normalBatteryTemperature) {
            updateBatteryTemperature(dashboard, batteryTemperature);
        }
    }

    private void updateBatteryTemperature(Dashboard dashboard, BatteryInfo batteryTemperature) {
        Float newTemperature = calculateNewTemperature(batteryTemperature.getValue());
        batteryInfoService.updateBatteryInfo(batteryTemperature, BatteryInfoType.TEMPERATURE, newTemperature, "°C");

        BatteryInfoDTO batteryInfoDTO = new BatteryInfoDTO(batteryTemperature);
        messagingTemplate.convertAndSend("/topic/battery_temperature/" + dashboard.getUuid(), batteryInfoDTO);
    }

    private Float calculateNewTemperature(Float currentTemperature) {
        return Math.max(currentTemperature - batteryDecrementTemperature, normalBatteryTemperature);
    }
}
