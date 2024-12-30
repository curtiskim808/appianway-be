package dev.appianway.dashboard.controller;

import dev.appianway.dashboard.model.entity.BatteryInfo;
import dev.appianway.dashboard.model.entity.BatteryInfoType;
import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.service.BatteryInfoService;
import dev.appianway.dashboard.service.DashboardService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BatteryInfoControllerTest {
    private MockMvc mockMvc;

    @Mock
    private DashboardService dashboardService;

    @Mock
    private BatteryInfoService batteryInfoService;

    @InjectMocks
    private BatteryInfoController batteryInfoController;

    private Dashboard dashboard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(batteryInfoController).build();

        dashboard = new Dashboard();
        String uuid = "2cbc1639-3e5f-4e77-96ba-1cc94a0467e8";
        dashboard.setUuid(uuid);
    }

    @Test
    void getAllBatteryInfo() throws Exception {
        BatteryInfo batteryInfo = new BatteryInfo();
        batteryInfo.setDashboard(dashboard);
        batteryInfo.setType(BatteryInfoType.REMAINING_CAPACITY);

        when(dashboardService.getDashboard(dashboard.getUuid())).thenReturn(java.util.Optional.of(dashboard));
        when(batteryInfoService.getAllBatteryInfos(dashboard)).thenReturn(java.util.List.of(batteryInfo));

        mockMvc.perform(get("/dashboards/" + dashboard.getUuid() + "/battery-info").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void getBatteryInfoByTypes() throws Exception {
        BatteryInfo batteryInfo = new BatteryInfo();
        batteryInfo.setDashboard(dashboard);
        batteryInfo.setType(BatteryInfoType.REMAINING_CAPACITY);

        when(dashboardService.getDashboard(dashboard.getUuid())).thenReturn(java.util.Optional.of(dashboard));
        when(batteryInfoService.getBatteryInfosByTypes(dashboard, java.util.List.of(BatteryInfoType.REMAINING_CAPACITY))).thenReturn(java.util.List.of(batteryInfo));

        mockMvc.perform(get("/dashboards/" + dashboard.getUuid() + "/battery-info?type=REMAINING_CAPACITY").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}