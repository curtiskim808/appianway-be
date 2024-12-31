package dev.appianway.dashboard.scheduled;

import dev.appianway.dashboard.model.dto.BatteryInfoDTO;
import dev.appianway.dashboard.model.entity.BatteryInfo;
import dev.appianway.dashboard.service.BatteryInfoService;
import dev.appianway.dashboard.service.DashboardService;
import dev.appianway.dashboard.service.MetricService;
import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.model.entity.BatteryInfoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
// This Scheduled task simulates the motor being active and updates the battery metrics.
@Component
public class ActiveMotorTask {

    private final SchedulerController schedulerController;

    public ActiveMotorTask(SchedulerController schedulerController) {
        this.schedulerController = schedulerController;
    }

    @Value("${dashboard.battery.min-capacity}")
    private Float minBatteryCapacity;
    @Value("${dashboard.battery.decrement-capacity}")
    private Float batteryDecrementCapacity;
    @Value("${dashboard.battery.max-temperature}")
    private Float maxBatteryTemperature;
    @Value("${dashboard.battery.increment-temperature}")
    private Float batteryIncrementTemperature;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private BatteryInfoService batteryInfoService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Runs every 5 second
    @Scheduled(fixedRate = 5000)
    public void updateBatteryMetrics() {
        // This is Simulation of Motor in used
        // In real world, this data update will be done by the hardware
        if (schedulerController.isReadyToStart()) {
            List<Dashboard> dashboards = dashboardService.getAllDashboardsWithMotorOn();

            for (Dashboard dashboard : dashboards) {
                String dashboardId = dashboard.getUuid();

                // Update Remaining Battery Percentage
                BatteryInfo batteryCapacity = batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.REMAINING_CAPACITY);
                if (batteryCapacity != null && batteryCapacity.getValue() > minBatteryCapacity) {
                    Float newCapacity = batteryCapacity.getValue() - batteryDecrementCapacity; // Decrease by 0.1%
                    newCapacity = Math.max(newCapacity, minBatteryCapacity);
                    batteryInfoService.updateBatteryInfo(batteryCapacity, BatteryInfoType.REMAINING_CAPACITY, newCapacity, "%");

                    BatteryInfoDTO batteryInfoDTO = new BatteryInfoDTO(batteryCapacity);
                    // Broadcast updated battery capacity
                    messagingTemplate.convertAndSend("/topic/battery_capacity/" + dashboardId, batteryInfoDTO);
                }

                // Update Battery Temperature
                BatteryInfo batteryTemperature = batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.TEMPERATURE);
                if (batteryTemperature != null && batteryTemperature.getValue() < maxBatteryTemperature) {
                    Float newTemperature = batteryTemperature.getValue() + batteryIncrementTemperature; // Increase by 0.1°C
                    newTemperature = Math.min(newTemperature, maxBatteryTemperature);
                    batteryInfoService.updateBatteryInfo(batteryTemperature, BatteryInfoType.TEMPERATURE, newTemperature, "°C");

                    BatteryInfoDTO batteryInfoDTO = new BatteryInfoDTO(batteryTemperature);
                    // Broadcast updated battery temperature
                    messagingTemplate.convertAndSend("/topic/battery_temperature/" + dashboardId, batteryInfoDTO);
                }
            }
        }
    }
}
