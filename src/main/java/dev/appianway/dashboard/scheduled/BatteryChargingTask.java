package dev.appianway.dashboard.scheduled;

import dev.appianway.dashboard.model.dto.BatteryInfoDTO;
import dev.appianway.dashboard.model.entity.BatteryInfoType;
import dev.appianway.dashboard.service.BatteryInfoService;
import dev.appianway.dashboard.service.DashboardService;
import dev.appianway.dashboard.service.MetricService;
import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.model.entity.BatteryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BatteryChargingTask {
    private final SchedulerController schedulerController;

    public BatteryChargingTask(SchedulerController schedulerController) {
        this.schedulerController = schedulerController;
    }

    @Value("${dashboard.battery.max-capacity}")
    private Float maxBatteryCapacity;
    @Value("${dashboard.battery.increment-capacity}")
    private Float batteryIncrementCapacity;
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
            // This is Simulation of Charging
            // In real world, this data update will be done by the hardware
            List<Dashboard> chargingOnDashboards = dashboardService.getAllDashboardsWithChargingOn();

            for (Dashboard dashboard : chargingOnDashboards) {
                String dashboardId = dashboard.getUuid();

                // Update Remaining Battery Percentage
                System.out.println("dashboard + " + dashboard);
                BatteryInfo batteryCapacity = batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.REMAINING_CAPACITY);
                System.out.println("batteryCapacity + " + batteryCapacity);
                if (batteryCapacity != null && batteryCapacity.getValue() < maxBatteryCapacity) {
                    Float newCapacity = batteryCapacity.getValue() + batteryIncrementCapacity; // Increment by 1%
                    System.out.println("newCapacity + " + newCapacity);
                    newCapacity = Math.min(newCapacity, maxBatteryCapacity); // Cap at 100%
                    batteryInfoService.updateBatteryInfo(batteryCapacity, BatteryInfoType.REMAINING_CAPACITY, newCapacity, "%");

                    BatteryInfoDTO batteryInfoDTO = new BatteryInfoDTO(batteryCapacity);
                    // Broadcast updated battery capacity
                    messagingTemplate.convertAndSend("/topic/battery_capacity/" + dashboardId, batteryInfoDTO);
                }
            }
        }
    }
}
