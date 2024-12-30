package dev.appianway.dashboard.scheduled;

import dev.appianway.dashboard.model.dto.BatteryInfoDTO;
import dev.appianway.dashboard.model.entity.BatteryInfoType;
import dev.appianway.dashboard.service.BatteryInfoService;
import dev.appianway.dashboard.service.DashboardService;
import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.model.entity.BatteryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InactiveMotorTask {
    private final SchedulerController schedulerController;

    public InactiveMotorTask(SchedulerController schedulerController) {
        this.schedulerController = schedulerController;
    }

    @Value("${dashboard.battery.normal-temperature}")
    private Float normalBatteryTemperature;
    @Value("${dashboard.battery.decrement-temperature}")
    private Float batteryDecrementTemperature;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private BatteryInfoService batteryInfoService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Runs every 5 second
    @Scheduled(fixedRate = 5000)
    public void updateBatteryMetrics() {
        if (schedulerController.isReadyToStart()) {
            // This is Simulation of inactive dashboards
            // In real world, this data update will be done by the hardware
            List<Dashboard> inactiveDashboards = dashboardService.getAllDashboardsWithMotorOffAndChargingOff();
            for (Dashboard dashboard : inactiveDashboards) {
                String dashboardId = dashboard.getUuid();

                // Update Battery Temerature
                BatteryInfo batteryTemperature = batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.TEMPERATURE);
                if (batteryTemperature != null && batteryTemperature.getValue() > normalBatteryTemperature) {
                    Float newTemperature = batteryTemperature.getValue() - batteryDecrementTemperature; // decrement by 0.1°C
                    newTemperature = Math.max(newTemperature, normalBatteryTemperature); // Cap at normal temperature
                    batteryInfoService.updateBatteryInfo(batteryTemperature, BatteryInfoType.TEMPERATURE, newTemperature, "°C");

                    BatteryInfoDTO batteryInfoDTO = new BatteryInfoDTO(batteryTemperature);
                    // Broadcast updated battery temperature
                    messagingTemplate.convertAndSend("/topic/battery_temperature/" + dashboardId, batteryInfoDTO);
                }
            }
        }
    }
}
