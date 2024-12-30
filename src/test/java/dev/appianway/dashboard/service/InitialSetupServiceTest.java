package dev.appianway.dashboard.service;

import dev.appianway.dashboard.model.entity.*;
import dev.appianway.dashboard.repository.*;
import dev.appianway.dashboard.scheduled.SchedulerController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class InitialSetupServiceTest {
    @Mock
    private DashboardRepository dashboardRepository;

    @Mock
    private BatteryInfoRepository batteryInfoRepository;

    @Mock
    private IndicatorRepository indicatorRepository;

    @Mock
    private MetricRepository metricRepository;

    @Mock
    private SchedulerController schedulerController;

    @InjectMocks
    private InitialSetupService initialSetupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cleanupDatabase() {
        initialSetupService.cleanupDatabase();
        verify(metricRepository, times(1)).deleteAll();
        verify(indicatorRepository, times(1)).deleteAll();
        verify(batteryInfoRepository, times(1)).deleteAll();
        verify(dashboardRepository, times(1)).deleteAll();
    }

    @Test
    void createDashboard() {
        Dashboard dashboard = initialSetupService.createDashboard();
        assertNotNull(dashboard);
        assertEquals("Main Vehicle Dashboard", dashboard.getName());
    }

    @Test
    void setupInitialData() {
        Dashboard dashboard = new Dashboard();
        when(dashboardRepository.save(any(Dashboard.class))).thenReturn(dashboard);
        when(dashboardRepository.findAll()).thenReturn(List.of(dashboard));

        initialSetupService.setupInitialData();

        verify(dashboardRepository, times(1)).save(any(Dashboard.class));
        verify(batteryInfoRepository, times(1)).saveAll(anyList());
        verify(indicatorRepository, times(1)).saveAll(anyList());
        verify(metricRepository, times(1)).saveAll(anyList());
        verify(schedulerController, times(1)).setReadyToStart(true);
    }

    @Test
    void createNewDashboardSet() {
        Dashboard dashboard = new Dashboard();
        when(dashboardRepository.save(any(Dashboard.class))).thenReturn(dashboard);
        when(dashboardRepository.findAll()).thenReturn(List.of(dashboard));

        Dashboard createdDashboard = initialSetupService.createNewDashboardSet();

        assertNotNull(createdDashboard);
        verify(dashboardRepository, times(1)).save(any(Dashboard.class));
        verify(batteryInfoRepository, times(1)).saveAll(anyList());
        verify(indicatorRepository, times(1)).saveAll(anyList());
        verify(metricRepository, times(1)).saveAll(anyList());
    }
}