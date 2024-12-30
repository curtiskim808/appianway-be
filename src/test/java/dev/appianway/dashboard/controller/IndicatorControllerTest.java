package dev.appianway.dashboard.controller;

import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.model.entity.Indicator;
import dev.appianway.dashboard.model.entity.IndicatorType;
import dev.appianway.dashboard.service.BatteryInfoService;
import dev.appianway.dashboard.service.DashboardService;
import dev.appianway.dashboard.service.IndicatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class IndicatorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DashboardService dashboardService;

    @Mock
    private IndicatorService indicatorService;

    @InjectMocks
    private IndicatorController indicatorController;

    private Dashboard dashboard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(indicatorController).build();

        dashboard = new Dashboard();
        String uuid = "2cbc1639-3e5f-4e77-96ba-1cc94a0467e8";
        dashboard.setUuid(uuid);
    }

    @Test
    void getAllIndicators() throws Exception {
        Indicator indicator = new Indicator();
        indicator.setDashboard(dashboard);
        indicator.setType(IndicatorType.BATTERY_CHARGING);

        when(dashboardService.getDashboard(dashboard.getUuid())).thenReturn(java.util.Optional.of(dashboard));
        when(indicatorService.getAllIndicators(dashboard)).thenReturn(java.util.List.of(indicator));

        mockMvc.perform(get("/dashboards/" + dashboard.getUuid() + "/indicators").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void getIndicatorsByTypes() throws Exception {
        Indicator indicator = new Indicator();
        indicator.setDashboard(dashboard);
        indicator.setType(IndicatorType.BATTERY_CHARGING);
        indicator.setStatus(true);

        when(dashboardService.getDashboard(dashboard.getUuid())).thenReturn(java.util.Optional.of(dashboard));
        when(indicatorService.getIndicatorsByTypes(dashboard, java.util.List.of(IndicatorType.BATTERY_CHARGING))).thenReturn(java.util.List.of(indicator));

        mockMvc.perform(get("/dashboards/" + dashboard.getUuid() + "/indicators?type=battery_charging").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void updateIndicator() throws Exception {
        Indicator indicator = new Indicator();
        indicator.setDashboard(dashboard);
        indicator.setType(IndicatorType.BATTERY_CHARGING);
        indicator.setStatus(true);

        when(dashboardService.getDashboard(dashboard.getUuid())).thenReturn(java.util.Optional.of(dashboard));
        when(indicatorService.updateIndicator(indicator.getId(), false)).thenReturn(indicator);

        mockMvc.perform(get("/dashboards/" + dashboard.getUuid() + "/indicators").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

}