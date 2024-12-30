package dev.appianway.dashboard.service;

import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.model.entity.Indicator;
import dev.appianway.dashboard.model.entity.IndicatorType;
import dev.appianway.dashboard.repository.IndicatorRepository;
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
class IndicatorServiceTest {
    @Mock
    IndicatorRepository indicatorRepository;
    @Mock
    SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    IndicatorService indicatorService;

    @Test
    void getAllIndicators() {
        Dashboard dashboard = new Dashboard();
        Indicator indicator = new Indicator();
        indicator.setDashboard(dashboard);
        indicator.setStatus(true);
        indicator.setType(IndicatorType.BATTERY_CHARGING);

        Mockito.when(indicatorRepository.findByDashboard(dashboard)).thenReturn(List.of(indicator));
        List<Indicator> indicators = indicatorService.getAllIndicators(dashboard);
        assertEquals(List.of(indicator), indicators);
    }

    @Test
    void getIndicatorsByTypes() {
        Dashboard dashboard = new Dashboard();
        Indicator indicator = new Indicator();
        indicator.setDashboard(dashboard);
        indicator.setStatus(true);
        indicator.setType(IndicatorType.BATTERY_CHARGING);

        Mockito.when(indicatorRepository.findByDashboardAndTypeIn(dashboard, List.of(IndicatorType.BATTERY_CHARGING))).thenReturn(List.of(indicator));
        List<Indicator> indicators = indicatorService.getIndicatorsByTypes(dashboard, List.of(IndicatorType.BATTERY_CHARGING));
        assertEquals(List.of(indicator), indicators);
    }

    @Test
    void updateIndicator() {
        Indicator indicator = new Indicator();
        indicator.setId(1L);
        indicator.setStatus(true);
        Dashboard dashboard = new Dashboard();
        dashboard.setUuid("123");
        indicator.setDashboard(dashboard);
        Mockito.when(indicatorRepository.findById(1L)).thenReturn(java.util.Optional.of(indicator));
        Mockito.when(indicatorRepository.save(indicator)).thenReturn(indicator);
        Indicator updatedIndicator = indicatorService.updateIndicator(1L, false);
        assertFalse(updatedIndicator.getStatus());
    }
}