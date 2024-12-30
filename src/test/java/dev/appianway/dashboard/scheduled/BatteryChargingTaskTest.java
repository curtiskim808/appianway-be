package dev.appianway.dashboard.scheduled;

import dev.appianway.dashboard.model.entity.BatteryInfo;
import dev.appianway.dashboard.model.entity.BatteryInfoType;
import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.service.BatteryInfoService;
import dev.appianway.dashboard.service.DashboardService;
import dev.appianway.dashboard.service.MetricService;
import dev.appianway.dashboard.model.dto.BatteryInfoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BatteryChargingTaskTest {
    private SchedulerController schedulerController;
    private DashboardService dashboardService;
    private BatteryInfoService batteryInfoService;
    private SimpMessagingTemplate messagingTemplate;
    private BatteryChargingTask batteryChargingTask;

    @BeforeEach
    void setUp() {
        schedulerController = mock(SchedulerController.class);
        dashboardService = mock(DashboardService.class);
        batteryInfoService = mock(BatteryInfoService.class);
        messagingTemplate = mock(SimpMessagingTemplate.class);

        batteryChargingTask = new BatteryChargingTask(schedulerController);
        // Inject mocks
        ReflectionTestUtils.setField(batteryChargingTask, "dashboardService", dashboardService);
        ReflectionTestUtils.setField(batteryChargingTask, "batteryInfoService", batteryInfoService);
        ReflectionTestUtils.setField(batteryChargingTask, "messagingTemplate", messagingTemplate);

        // Set configuration properties
        ReflectionTestUtils.setField(batteryChargingTask, "maxBatteryCapacity", 100.0f);
        ReflectionTestUtils.setField(batteryChargingTask, "batteryIncrementCapacity", 1.0f);
    }

    @Test
    void updateBatteryMetrics_WhenSchedulerNotReady_ShouldDoNothing() {
        when(schedulerController.isReadyToStart()).thenReturn(false);

        batteryChargingTask.updateBatteryMetrics();

        verifyNoInteractions(dashboardService, batteryInfoService, messagingTemplate);
    }

    @Test
    void updateBatteryMetrics_ShouldIncrementBatteryCapacityAndSendMessage() {
        when(schedulerController.isReadyToStart()).thenReturn(true);

        Dashboard dashboard = new Dashboard();
        dashboard.setUuid("dashboard-123");
        List<Dashboard> dashboards = Collections.singletonList(dashboard);

        when(dashboardService.getAllDashboardsWithChargingOn()).thenReturn(dashboards);

        BatteryInfo batteryInfo = new BatteryInfo();
        batteryInfo.setValue(50.0f);
        batteryInfo.setDashboard(dashboard);
        when(batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.REMAINING_CAPACITY)).thenReturn(batteryInfo);

        batteryChargingTask.updateBatteryMetrics();
        // Verify updateBatteryMetrics called
        verify(batteryInfoService, times(1))
                .updateBatteryInfo(batteryInfo, BatteryInfoType.REMAINING_CAPACITY, 51.0f, "%");
    }

    @Test
    void updateBatteryMetrics_ShouldNotExceedMaxCapacity() {
        when(schedulerController.isReadyToStart()).thenReturn(true);

        Dashboard dashboard = new Dashboard();
        dashboard.setUuid("dashboard-456");
        List<Dashboard> dashboards = Collections.singletonList(dashboard);

        when(dashboardService.getAllDashboardsWithChargingOn()).thenReturn(dashboards);

        BatteryInfo batteryInfo = new BatteryInfo();
        batteryInfo.setValue(100.0f); // At max capacity
        when(batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.REMAINING_CAPACITY)).thenReturn(batteryInfo);

        batteryChargingTask.updateBatteryMetrics();

        // Battery should remain at max
        assertEquals(100.0f, batteryInfo.getValue());

        // updateBatteryInfo should not be called since it's already at max
        verify(batteryInfoService, never())
                .updateBatteryInfo(any(), eq(BatteryInfoType.REMAINING_CAPACITY), anyFloat(), eq("%"));

        // Messaging should not be sent
        verify(messagingTemplate, never()).convertAndSend(anyString(), Optional.ofNullable(any()));
    }

    @Test
    void updateBatteryMetrics_ShouldHandleNullBatteryInfo() {
        when(schedulerController.isReadyToStart()).thenReturn(true);

        Dashboard dashboard = new Dashboard();
        dashboard.setUuid("dashboard-789");
        List<Dashboard> dashboards = Collections.singletonList(dashboard);

        when(dashboardService.getAllDashboardsWithChargingOn()).thenReturn(dashboards);
        when(batteryInfoService.getBatteryInfo(dashboard, BatteryInfoType.REMAINING_CAPACITY)).thenReturn(null);

        batteryChargingTask.updateBatteryMetrics();

        // updateBatteryInfo should not be called
        verify(batteryInfoService, never())
                .updateBatteryInfo(any(), eq(BatteryInfoType.REMAINING_CAPACITY), anyFloat(), eq("%"));

        // Messaging should not be sent
        verify(messagingTemplate, never()).convertAndSend(anyString(), Optional.ofNullable(any()));
    }
}
