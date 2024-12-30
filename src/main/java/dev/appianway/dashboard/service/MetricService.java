package dev.appianway.dashboard.service;

import dev.appianway.dashboard.model.dto.MetricDTO;
import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.model.entity.Metric;
import dev.appianway.dashboard.model.entity.MetricType;
import dev.appianway.dashboard.repository.MetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetricService {
    @Value("${dashboard.metric.motor-max-rpm}")
    private Float motorMaxRpm;
    @Value("${dashboard.metric.motor-max-speed}")
    private Float motorMaxSpeed;
    @Value("${dashboard.metric.motor-max-power}")
    private Float motorMaxPower;
    @Value("${dashboard.metric.motor-min-power}")
    private Float motorMinPower;

    @Autowired
    private MetricRepository metricRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public List<Metric> getAllMetrics(Dashboard dashboard) {
        return metricRepository.findByDashboard(dashboard);
    }

    public List<Metric> getMetricsByTypes(Dashboard dashboard, List<MetricType> types) {
        return metricRepository.findByDashboardAndTypeIn(dashboard, types);
    }

    public Metric updateMetric(Long id, MetricType type, Float value, String unit) {
        Metric metric = metricRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Metric not found"));
        metric.setValue(value);
        if (unit != null) {
            metric.setUnit(unit);
        }
        Metric updatedMetric =  metricRepository.save(metric);
        if (type == MetricType.MOTOR_SPEED) {
            handleMotorSpeedUpdate(metric.getDashboard(), value);
        }

        MetricDTO updatedMetricDTO = new MetricDTO(updatedMetric);

        // Broadcast the update via WebSocket
        messagingTemplate.convertAndSend("/topic/metrics/" + updatedMetricDTO.getDashboardUuid(), updatedMetricDTO);
        return updatedMetric;
    }

    private void handleMotorSpeedUpdate(Dashboard dashboard, Float motorSpeed) {
        // update motor rpm and power input
        List<Metric> metrics = metricRepository.findByDashboardAndTypeIn(dashboard, List.of(MetricType.MOTOR_RPM, MetricType.POWER_INPUT));
        for (Metric metric : metrics) {
            if (metric.getType() == MetricType.MOTOR_RPM) {
                metric.setValue(motorSpeed*(motorMaxRpm/motorMaxSpeed));
            } else if (metric.getType() == MetricType.POWER_INPUT) {
                metric.setValue(motorSpeed * ((motorMaxPower - 0) / motorMaxSpeed));
            }
            Metric updatedMetric = metricRepository.save(metric);
            MetricDTO updatedMetricDTO = new MetricDTO(updatedMetric);

            // Broadcast the update via WebSocket
            messagingTemplate.convertAndSend("/topic/metrics/" + updatedMetricDTO.getDashboardUuid(), updatedMetricDTO);
        }
    }

}
