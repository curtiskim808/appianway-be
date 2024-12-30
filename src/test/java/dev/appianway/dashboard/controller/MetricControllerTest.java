package dev.appianway.dashboard.controller;

import dev.appianway.dashboard.model.dto.MetricDTO;
import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.model.entity.Metric;
import dev.appianway.dashboard.model.entity.MetricType;
import dev.appianway.dashboard.service.DashboardService;
import dev.appianway.dashboard.service.IndicatorService;
import dev.appianway.dashboard.service.MetricService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MetricControllerTest {
    private MockMvc mockMvc;

    @Mock
    private DashboardService dashboardService;

    @Mock
    private MetricService metricService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private MetricDTO metricDTO;

    @InjectMocks
    private MetricController metricController;

    private Dashboard dashboard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(metricController).build();

        dashboard = new Dashboard();
        String uuid = "2cbc1639-3e5f-4e77-96ba-1cc94a0467e8";
        dashboard.setUuid(uuid);
    }

    @Test
    void getAllMetrics() throws Exception {
        Metric metric = new Metric();
        metric.setDashboard(dashboard);
        metric.setType(MetricType.MOTOR_RPM);
        metric.setId(1L);

        when(dashboardService.getDashboard(dashboard.getUuid())).thenReturn(java.util.Optional.of(dashboard));
        when(metricService.getAllMetrics(dashboard)).thenReturn(java.util.List.of(metric));
        when(dashboardService.getDashboard(dashboard.getUuid())).thenReturn(java.util.Optional.of(dashboard));
        when(metricService.getAllMetrics(dashboard)).thenReturn(java.util.List.of());

        mockMvc.perform(get("/dashboards/" + dashboard.getUuid() + "/metrics").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void getMetricsByTypes() throws Exception {
        Metric metric = new Metric();
        metric.setDashboard(dashboard);
        metric.setType(MetricType.MOTOR_RPM);
        metric.setId(1L);

        when(dashboardService.getDashboard(dashboard.getUuid())).thenReturn(java.util.Optional.of(dashboard));
        when(metricService.getMetricsByTypes(dashboard, java.util.List.of(MetricType.MOTOR_RPM))).thenReturn(java.util.List.of(metric));

        mockMvc.perform(get("/dashboards/" + dashboard.getUuid() + "/metrics?type=" + MetricType.MOTOR_RPM).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void updateMetric() throws Exception {
        Metric metric = new Metric();
        metric.setId(1L);
        metric.setValue(100.0f);
        metric.setUnit("RPM");
        metric.setType(MetricType.MOTOR_RPM);
        metric.setDashboard(new Dashboard());

        when(dashboardService.getDashboard(dashboard.getUuid())).thenReturn(java.util.Optional.of(dashboard));
        when(metricService.updateMetric(metric.getId(), metric.getType(), metric.getValue() + 0.05f, metric.getUnit())).thenReturn(metric);
        metric.setValue((float) (metric.getValue() + 0.05));

        mockMvc.perform(put("/dashboards/" + dashboard.getUuid() +"/metrics").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"id\":1,\"type\":\"MOTOR_RPM\",\"value\":100.05,\"unit\":\"RPM\"}"))
                .andExpect(status().isOk());
    }
}