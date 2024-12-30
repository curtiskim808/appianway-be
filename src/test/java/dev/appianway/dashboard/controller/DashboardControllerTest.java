package dev.appianway.dashboard.controller;

import dev.appianway.dashboard.model.dto.DashboardDTO;
import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.repository.DashboardRepository;
import dev.appianway.dashboard.service.BatteryInfoService;
import dev.appianway.dashboard.service.DashboardService;
import dev.appianway.dashboard.service.InitialSetupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.print.attribute.standard.Media;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DashboardControllerTest {
    private MockMvc mockMvc;

    @Mock
    private DashboardService dashboardService;

    @Mock
    private InitialSetupService initialSetupService;

    @InjectMocks
    private DashboardController dashboardController;

    private Dashboard dashboard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(dashboardController).build();

        // Initialize test data
        dashboard = new Dashboard();
        String uuid = "2cbc1639-3e5f-4e77-96ba-1cc94a0467e8";
        dashboard.setUuid(uuid);
    }

    @Test
    void getDashboard() throws Exception {
        String uuid = "2cbc1639-3e5f-4e77-96ba-1cc94a0467e8";

        when(dashboardService.getDashboard(uuid)).thenReturn(Optional.of(dashboard));

        mockMvc.perform(get("/dashboards/" + uuid).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void getDashboard_notFound() throws Exception {
        String uuid = "2cbc1639-3e5f-4e77-96ba-1cc94a0467e8";

        when(dashboardService.getDashboard(uuid)).thenReturn(Optional.empty());

        mockMvc.perform(get("/dashboards/" + uuid).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void getDashboard_redirectToFirstDashboard() throws Exception {
        when(dashboardService.getFirstDashboard()).thenReturn(dashboard);

        mockMvc.perform(get("/dashboards").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void createDashboard() throws Exception {
        Dashboard dashboard = new Dashboard();
        String uuid = "3abw1339-3e5f-4e77-96ba-1cc94a0467e8";
        dashboard.setUuid(uuid);

        when(dashboardService.saveDashboard(dashboard)).thenReturn(dashboard);

        mockMvc.perform(post("/dashboards").contentType(MediaType.APPLICATION_JSON_VALUE).content("{\"uuid\":\"" + uuid + "\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void setupInitialData() throws Exception {
        doNothing().when(initialSetupService).setupInitialData();

        mockMvc.perform(post("/dashboards/setup"))
                .andExpect(status().isNoContent());

        verify(initialSetupService, times(1)).setupInitialData();
    }

    @Test
    void createNewDashboardSet() throws Exception {
        Dashboard dashboard = new Dashboard();
        when(initialSetupService.createNewDashboardSet()).thenReturn(dashboard);

        mockMvc.perform(post("/dashboards/new-dashboard-set")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(dashboard.getName()));

        verify(initialSetupService, times(1)).createNewDashboardSet();
    }
}