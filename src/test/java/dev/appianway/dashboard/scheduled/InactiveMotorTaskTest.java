package dev.appianway.dashboard.scheduled;

import dev.appianway.dashboard.model.entity.BatteryInfo;
import dev.appianway.dashboard.model.entity.BatteryInfoType;
import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.service.BatteryInfoService;
import dev.appianway.dashboard.service.DashboardService;
import dev.appianway.dashboard.service.MetricService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class InactiveMotorTaskTest {

    private SchedulerController schedulerController;
    private DashboardService dashboardService;
    private BatteryInfoService batteryInfoService;
    private SimpMessagingTemplate messagingTemplate;

    private InactiveMotorTask InactiveMotorTask;

    @BeforeEach
    void setUp() {
        schedulerController = mock(SchedulerController.class);
        dashboardService = mock(DashboardService.class);
        batteryInfoService = mock(BatteryInfoService.class);
        messagingTemplate = mock(SimpMessagingTemplate.class);

        InactiveMotorTask = new InactiveMotorTask(schedulerController);
        // Inject mocks
        ReflectionTestUtils.setField(InactiveMotorTask, "dashboardService", dashboardService);
        ReflectionTestUtils.setField(InactiveMotorTask, "batteryInfoService", batteryInfoService);
        ReflectionTestUtils.setField(InactiveMotorTask, "messagingTemplate", messagingTemplate);

        // Set configuration properties
        ReflectionTestUtils.setField(InactiveMotorTask, "normalBatteryTemperature", 20.0f);
        ReflectionTestUtils.setField(InactiveMotorTask, "batteryDecrementTemperature", 0.1f);
    }

    @Test
    void updateBatteryMetrics_WhenSchedulerNotReady_ShouldDoNothing() {
        when(schedulerController.isReadyToStart()).thenReturn(false);

        InactiveMotorTask.updateBatteryMetrics();

        verifyNoInteractions(dashboardService, batteryInfoService, messagingTemplate);
    }

    @Test
    void updateBatteryMetrics_ShouldDecrementBatteryTemperatureAndSendMessage() {
        when(schedulerController.isReadyToStart()).thenReturn(true);

        Dashboard dashboard = new Dashboard();
        dashboard.setUuid("dashboard-123");
        List<Dashboard> dashboards = Collections.singletonList(dashboard);

        when(dashboardService.getAllDashboardsWithMotorOffAndChargingOff()).thenReturn(dashboards);

        BatteryInfo batteryInfo = new BatteryInfo();
        batteryInfo.setValue(50.0f);
        batteryInfo.setDashboard(dashboard);
        when(batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.TEMPERATURE)).thenReturn(batteryInfo);

        InactiveMotorTask.updateBatteryMetrics();
        // Verify updateBatteryMetrics called
        verify(batteryInfoService, times(1))
                .updateBatteryInfo(batteryInfo, BatteryInfoType.TEMPERATURE, 49.9f, "°C");
    }

    @Test
    void updateBatteryMetrics_ShouldNotExceedMinTemperature() {
        when(schedulerController.isReadyToStart()).thenReturn(true);

        Dashboard dashboard = new Dashboard();
        dashboard.setUuid("dashboard-456");
        List<Dashboard> dashboards = Collections.singletonList(dashboard);

        when(dashboardService.getAllDashboardsWithMotorOffAndChargingOff()).thenReturn(dashboards);

        BatteryInfo batteryInfo = new BatteryInfo();
        batteryInfo.setValue(20.1f); // At min temperature
        batteryInfo.setDashboard(dashboard);
        when(batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.TEMPERATURE)).thenReturn(batteryInfo);

        InactiveMotorTask.updateBatteryMetrics();

        verify(batteryInfoService, times(1))
                .updateBatteryInfo(batteryInfo, BatteryInfoType.TEMPERATURE, 20.0f, "°C");
    }
    @Test
    void updateBatteryMetrics_ShouldHandleNullBatteryInfo() {
        when(schedulerController.isReadyToStart()).thenReturn(true);

        Dashboard dashboard = new Dashboard();
        dashboard.setUuid("dashboard-789");
        List<Dashboard> dashboards = Collections.singletonList(dashboard);

        when(dashboardService.getAllDashboardsWithChargingOn()).thenReturn(dashboards);
        when(batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.TEMPERATURE)).thenReturn(null);

        InactiveMotorTask.updateBatteryMetrics();

        // updateBatteryInfo should not be called
        verify(batteryInfoService, never())
                .updateBatteryInfo(any(), eq(BatteryInfoType.TEMPERATURE), anyFloat(), eq("°C"));

        // Messaging should not be sent
        verify(messagingTemplate, never()).convertAndSend(anyString(), Optional.ofNullable(any()));
    }
}
