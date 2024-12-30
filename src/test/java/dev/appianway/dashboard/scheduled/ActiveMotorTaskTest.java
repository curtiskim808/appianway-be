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

class ActiveMotorTaskTest {

    private SchedulerController schedulerController;
    private DashboardService dashboardService;
    private BatteryInfoService batteryInfoService;
    private MetricService metricService;
    private SimpMessagingTemplate messagingTemplate;

    private ActiveMotorTask activeMotorTask;

    @BeforeEach
    void setUp() {
        schedulerController = mock(SchedulerController.class);
        dashboardService = mock(DashboardService.class);
        batteryInfoService = mock(BatteryInfoService.class);
        metricService = mock(MetricService.class);
        messagingTemplate = mock(SimpMessagingTemplate.class);

        activeMotorTask = new ActiveMotorTask(schedulerController);
        // Inject mocks
        ReflectionTestUtils.setField(activeMotorTask, "dashboardService", dashboardService);
        ReflectionTestUtils.setField(activeMotorTask, "batteryInfoService", batteryInfoService);
        ReflectionTestUtils.setField(activeMotorTask, "metricService", metricService);
        ReflectionTestUtils.setField(activeMotorTask, "messagingTemplate", messagingTemplate);

        // Set configuration properties
        ReflectionTestUtils.setField(activeMotorTask, "maxBatteryCapacity", 100.0f);
        ReflectionTestUtils.setField(activeMotorTask, "minBatteryCapacity", 20.0f);
        ReflectionTestUtils.setField(activeMotorTask, "batteryIncrementCapacity", 1.0f);
        ReflectionTestUtils.setField(activeMotorTask, "batteryDecrementCapacity", 1.0f);
        ReflectionTestUtils.setField(activeMotorTask, "maxBatteryTemperature", 75.0f);
        ReflectionTestUtils.setField(activeMotorTask, "minBatteryTemperature", 15.0f);
        ReflectionTestUtils.setField(activeMotorTask, "batteryIncrementTemperature", 0.5f);
        ReflectionTestUtils.setField(activeMotorTask, "batteryDecrementTemperature", 0.5f);
    }

    @Test
    void updateBatteryMetrics_WhenSchedulerNotReady_ShouldDoNothing() {
        when(schedulerController.isReadyToStart()).thenReturn(false);

        activeMotorTask.updateBatteryMetrics();

        verifyNoInteractions(dashboardService, batteryInfoService, messagingTemplate);
    }

    @Test
    void updateBatteryMetrics_NoDashboards_ShouldDoNothing() {
        when(schedulerController.isReadyToStart()).thenReturn(true);
        when(dashboardService.getAllDashboardsWithMotorOn()).thenReturn(Collections.emptyList());

        activeMotorTask.updateBatteryMetrics();

        verify(dashboardService, times(1)).getAllDashboardsWithMotorOn();
        verifyNoMoreInteractions(dashboardService, batteryInfoService, messagingTemplate);
    }

    @Test
    void updateBatteryMetrics_ShouldDecrementBatteryCapacityAndIncrementTemperature() {
        when(schedulerController.isReadyToStart()).thenReturn(true);

        Dashboard dashboard = new Dashboard();
        dashboard.setUuid("dashboard-abc");
        List<Dashboard> dashboards = Collections.singletonList(dashboard);

        when(dashboardService.getAllDashboardsWithMotorOn()).thenReturn(dashboards);

        BatteryInfo batteryCapacity = new BatteryInfo();
        batteryCapacity.setValue(50.0f);
        batteryCapacity.setDashboard(dashboard);
        when(batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.REMAINING_CAPACITY)).thenReturn(batteryCapacity);

        BatteryInfo batteryTemperature = new BatteryInfo();
        batteryTemperature.setValue(30.0f);
        batteryTemperature.setDashboard(dashboard);
        when(batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.TEMPERATURE)).thenReturn(batteryTemperature);

        activeMotorTask.updateBatteryMetrics();

        // Verify battery capacity decrement called
        verify(batteryInfoService, times(1))
                .updateBatteryInfo(batteryCapacity, BatteryInfoType.REMAINING_CAPACITY, 49.0f, "%");

        // Verify battery temperature increment called
        verify(batteryInfoService, times(1))
                .updateBatteryInfo(batteryTemperature, BatteryInfoType.TEMPERATURE, 30.5f, "°C");
    }

    @Test
    void updateBatteryMetrics_ShouldNotGoBelowMinCapacityOrAboveMaxTemperature() {
        when(schedulerController.isReadyToStart()).thenReturn(true);

        Dashboard dashboard = new Dashboard();
        dashboard.setUuid("dashboard-def");
        List<Dashboard> dashboards = Collections.singletonList(dashboard);

        when(dashboardService.getAllDashboardsWithMotorOn()).thenReturn(dashboards);

        BatteryInfo batteryCapacity = new BatteryInfo();
        batteryCapacity.setValue(20.0f); // At min capacity
        when(batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.REMAINING_CAPACITY)).thenReturn(batteryCapacity);

        BatteryInfo batteryTemperature = new BatteryInfo();
        batteryTemperature.setValue(75.0f); // At max temperature
        when(batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.TEMPERATURE)).thenReturn(batteryTemperature);

        activeMotorTask.updateBatteryMetrics();

        // Battery capacity should not decrease
        assertEquals(20.0f, batteryCapacity.getValue());
        verify(batteryInfoService, never())
                .updateBatteryInfo(eq(batteryCapacity), eq(BatteryInfoType.REMAINING_CAPACITY), anyFloat(), eq("%"));

        // Battery temperature should not increase
        assertEquals(75.0f, batteryTemperature.getValue());
        verify(batteryInfoService, never())
                .updateBatteryInfo(eq(batteryTemperature), eq(BatteryInfoType.TEMPERATURE), anyFloat(), eq("°C"));

        // Messaging should not be sent
        verify(messagingTemplate, never()).convertAndSend(anyString(), Optional.ofNullable(any()));
    }

    @Test
    void updateBatteryMetrics_ShouldHandleNullBatteryInfos() {
        when(schedulerController.isReadyToStart()).thenReturn(true);

        Dashboard dashboard = new Dashboard();
        dashboard.setUuid("dashboard-ghi");
        List<Dashboard> dashboards = Collections.singletonList(dashboard);

        when(dashboardService.getAllDashboardsWithMotorOn()).thenReturn(dashboards);
        when(batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.REMAINING_CAPACITY)).thenReturn(null);
        when(batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.TEMPERATURE)).thenReturn(null);

        activeMotorTask.updateBatteryMetrics();

        // updateBatteryInfo should not be called
        verify(batteryInfoService, never())
                .updateBatteryInfo(any(), eq(BatteryInfoType.REMAINING_CAPACITY), anyFloat(), eq("%"));
        verify(batteryInfoService, never())
                .updateBatteryInfo(any(), eq(BatteryInfoType.TEMPERATURE), anyFloat(), eq("°C"));

        // Messaging should not be sent
        verify(messagingTemplate, never()).convertAndSend(anyString(), Optional.ofNullable(any()));
    }
}
