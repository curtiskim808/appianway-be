package dev.appianway.dashboard.service;

import dev.appianway.dashboard.model.dto.MetricDTO;
import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.model.entity.Metric;
import dev.appianway.dashboard.model.entity.MetricType;
import dev.appianway.dashboard.repository.MetricRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MetricServiceTest {
    @Mock
    MetricRepository metricRepository;
    @InjectMocks
    MetricService metricService;

    @Mock
    SimpMessagingTemplate messagingTemplate;

    @Test
    void getAllMetrics() {
        Dashboard dashboard = new Dashboard();
        dashboard.setUuid("123");
        Metric metric = new Metric();
        metric.setDashboard(dashboard);
        metric.setId(1L);
        metric.setType(MetricType.MOTOR_RPM);
        metric.setValue(100.0f);
        metric.setUnit("RPM");

        Mockito.when(metricRepository.findByDashboard(dashboard)).thenReturn(List.of(metric));
        List<Metric> metrics = metricService.getAllMetrics(dashboard);
        assertEquals(List.of(metric), metrics);
    }

    @Test
    void getMetricsByTypes() {
        Dashboard dashboard = new Dashboard();
        dashboard.setUuid("123");
        Metric metric = new Metric();
        metric.setDashboard(dashboard);
        metric.setId(1L);
        metric.setType(MetricType.MOTOR_RPM);
        metric.setValue(100.0f);
        metric.setUnit("RPM");

        Mockito.when(metricRepository.findByDashboardAndTypeIn(dashboard, List.of(MetricType.MOTOR_RPM))).thenReturn(List.of(metric));
        List<Metric> metrics = metricService.getMetricsByTypes(dashboard, List.of(MetricType.MOTOR_RPM));
        assertEquals(List.of(metric), metrics);
    }

    @Test
    void updateMetric() {
        Metric metric = new Metric();
        metric.setId(1L);
        metric.setValue(100.0f);
        metric.setUnit("RPM");
        metric.setType(MetricType.MOTOR_RPM);
        metric.setDashboard(new Dashboard());

        Mockito.when(metricRepository.findById(1L)).thenReturn(java.util.Optional.of(metric));
        Mockito.when(metricRepository.save(metric)).thenReturn(metric);
        Metric updatedMetric = metricService.updateMetric(1L, MetricType.MOTOR_RPM, 200.0f, "RPM");
        assertEquals(200.0f, updatedMetric.getValue());
    }
}