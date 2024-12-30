package dev.appianway.dashboard.service;

import dev.appianway.dashboard.model.dto.IndicatorDTO;
import dev.appianway.dashboard.model.dto.MetricDTO;
import dev.appianway.dashboard.model.entity.*;
import dev.appianway.dashboard.repository.IndicatorRepository;
import dev.appianway.dashboard.repository.MetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndicatorService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MetricRepository metricRepository;

    @Autowired
    private IndicatorRepository indicatorRepository;

    @Value("${dashboard.metric.power-negative-input}")
    private Float powerNegativeInput;

    public List<Indicator> getAllIndicators(Dashboard dashboard) {
        return indicatorRepository.findByDashboard(dashboard);
    }

    public List<Indicator> getIndicatorsByTypes(Dashboard dashboard, List<IndicatorType> types) {
        return indicatorRepository.findByDashboardAndTypeIn(dashboard, types);
    }

    public Indicator updateIndicator(Long id, Boolean status) {
        Indicator indicator = indicatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Indicator not found"));

        indicator.setStatus(status);
        Indicator updatedIndicator = indicatorRepository.save(indicator);
        if (indicator.getType() == IndicatorType.BATTERY_CHARGING) {
            handleBatteryChargingUpdate(indicator.getDashboard(), status);
        }
        // Broadcast the update via WebSocket
        IndicatorDTO updatedIndicatorDTO = new IndicatorDTO(updatedIndicator);
        messagingTemplate.convertAndSend("/topic/indicators/" + updatedIndicatorDTO.getDashboardUuid(), updatedIndicatorDTO);
        return updatedIndicator;
    }

    private void handleBatteryChargingUpdate(Dashboard dashboard, Boolean isCharging) {
        // update power input based on battery charging status
        List<Metric> metrics = metricRepository.findByDashboardAndTypeIn(dashboard, List.of(MetricType.POWER_INPUT));
        for (Metric metric : metrics) {
            if (isCharging) {
                // update power input to negative value to indicate charging
                metric.setValue(powerNegativeInput);
            } else {
                // reset power input to 0
                metric.setValue(0f);
            }

            Metric updatedMetric = metricRepository.save(metric);
            MetricDTO updatedMetricDTO = new MetricDTO(updatedMetric);
            // Broadcast the update via WebSocket
            messagingTemplate.convertAndSend("/topic/metrics/" + updatedMetricDTO.getDashboardUuid(), updatedMetricDTO);
        }

    }
}
