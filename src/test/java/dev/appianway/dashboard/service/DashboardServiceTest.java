package dev.appianway.dashboard.service;

import dev.appianway.dashboard.model.entity.Indicator;
import dev.appianway.dashboard.model.entity.IndicatorType;
import dev.appianway.dashboard.repository.DashboardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import dev.appianway.dashboard.model.entity.Dashboard;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {
    @Mock
    DashboardRepository dashboardRepository;
    @InjectMocks
    DashboardService dashboardService;

    @Test
    void redirectToFirstDashboard() {
        Dashboard dashboard = new Dashboard();
        Mockito.when(dashboardRepository.findAll()).thenReturn(List.of(dashboard));
        Dashboard firstDashboard = dashboardService.getFirstDashboard();
        assertEquals(dashboard, firstDashboard);
    }

    @Test
    void getDashboard() {
        Dashboard dashboard = new Dashboard();
        String uuid = "123";
        dashboard.setUuid(uuid);
        Mockito.when(dashboardRepository.findByUuid(uuid)).thenReturn(Optional.of(dashboard));
        Optional<Dashboard> dashboardOptional = dashboardService.getDashboard(uuid);
        assertEquals(dashboard, dashboardOptional.get());
    }

    @Test
    void getAllDashboardsWithChargingOn() {
        Dashboard dashboard = new Dashboard();
        Indicator indicator = new Indicator();
        indicator.setType(IndicatorType.BATTERY_CHARGING);
        indicator.setStatus(true);
        indicator.setDashboard(dashboard);

        Mockito.when(dashboardRepository.findDashboardsWithChargingOn()).thenReturn(List.of(dashboard));
        List<Dashboard> dashboards = dashboardService.getAllDashboardsWithChargingOn();
        assertEquals(List.of(dashboard), dashboards);
    }

    @Test
    void getAllDashboardsWithMotorOn() {
        Dashboard dashboard = new Dashboard();
        Indicator indicator = new Indicator();
        indicator.setType(IndicatorType.MOTOR_STATUS);
        indicator.setStatus(true);
        indicator.setDashboard(dashboard);

        Mockito.when(dashboardRepository.findDashboardsWithMotorOn()).thenReturn(List.of(dashboard));
        List<Dashboard> dashboards = dashboardService.getAllDashboardsWithMotorOn();
        assertEquals(List.of(dashboard), dashboards);
    }

    @Test
    void saveDashboard() {
        Dashboard dashboard = new Dashboard();
        Mockito.when(dashboardRepository.save(dashboard)).thenReturn(dashboard);
        Dashboard savedDashboard = dashboardService.saveDashboard(dashboard);
        assertEquals(dashboard, savedDashboard);
    }
}