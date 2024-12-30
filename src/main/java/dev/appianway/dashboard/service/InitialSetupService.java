package dev.appianway.dashboard.service;

import dev.appianway.dashboard.model.entity.*;
import dev.appianway.dashboard.repository.*;
import dev.appianway.dashboard.scheduled.SchedulerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InitialSetupService {
    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private BatteryInfoRepository batteryInfoRepository;

    @Autowired
    private IndicatorRepository indicatorRepository;

    @Autowired
    private MetricRepository metricRepository;

    @Autowired
    private SchedulerController schedulerController;

    public void cleanupDatabase() {
        System.out.println("Cleaning up database...");
        metricRepository.deleteAll();
        indicatorRepository.deleteAll();
        batteryInfoRepository.deleteAll();
        dashboardRepository.deleteAll();
        System.out.println("Database cleanup complete.");
    }

    public Dashboard createDashboard() {
        Dashboard dashboard = new Dashboard();
        dashboard.setName("Main Vehicle Dashboard");
        return dashboard;
    }

    public List<BatteryInfo> createBatteryInfos(Dashboard dashboard) {
        BatteryInfo batteryInfo1 = new BatteryInfo();
        batteryInfo1.setDashboard(dashboard);
        batteryInfo1.setType(BatteryInfoType.TEMPERATURE);
        batteryInfo1.setValue(26.0f);
        batteryInfo1.setUnit("°C");

        BatteryInfo batteryInfo2 = new BatteryInfo();
        batteryInfo2.setDashboard(dashboard);
        batteryInfo2.setType(BatteryInfoType.REMAINING_CAPACITY);
        batteryInfo2.setValue(19.5f);
        batteryInfo2.setUnit("%");

        return List.of(batteryInfo1, batteryInfo2);
    }

    public List<Indicator> createIndicators(Dashboard dashboard) {
        Indicator indicator1 = new Indicator();
        indicator1.setDashboard(dashboard);
        indicator1.setType(IndicatorType.BATTERY_CHARGING);
        indicator1.setStatus(false);

        Indicator indicator2 = new Indicator();
        indicator2.setDashboard(dashboard);
        indicator2.setType(IndicatorType.BATTERY_LOW);
        indicator2.setStatus(false);

        Indicator indicator3 = new Indicator();
        indicator3.setDashboard(dashboard);
        indicator3.setType(IndicatorType.ENGINE_STATUS);
        indicator3.setStatus(false);

        Indicator indicator4 = new Indicator();
        indicator4.setDashboard(dashboard);
        indicator4.setType(IndicatorType.PARKING_BRAKE);
        indicator4.setStatus(false);

        Indicator indicator5 = new Indicator();
        indicator5.setDashboard(dashboard);
        indicator5.setType(IndicatorType.MOTOR_STATUS);
        indicator5.setStatus(false);

        return List.of(indicator1, indicator2, indicator3, indicator4, indicator5);
    }

    public List<Metric> createMetrics(Dashboard dashboard) {
        Metric metric1 = new Metric();
        metric1.setDashboard(dashboard);
        metric1.setType(MetricType.MOTOR_SPEED);
        metric1.setValue(0.0f);
        metric1.setUnit("x200");

        Metric metric2 = new Metric();
        metric2.setDashboard(dashboard);
        metric2.setType(MetricType.GEAR_RATIO);
        metric2.setValue(3.0f);
        metric2.setUnit("RATIO");

        Metric metric3 = new Metric();
        metric3.setDashboard(dashboard);
        metric3.setType(MetricType.MOTOR_RPM);
        metric3.setValue(0.0f);
        metric3.setUnit("RPM");

        Metric metric4 = new Metric();
        metric4.setDashboard(dashboard);
        metric4.setType(MetricType.POWER_INPUT);
        metric4.setValue(0.0f);
        metric4.setUnit("KW");

        return List.of(metric1, metric2, metric3, metric4);
    }

    public void setupInitialData() {
        cleanupDatabase();
        createNewDashboardSet();
        schedulerController.setReadyToStart(true);
    }

    public Dashboard createNewDashboardSet() {
        Dashboard dashboard = createDashboard();
        Dashboard createdDashboard = dashboardRepository.save(dashboard);

        List<BatteryInfo> batteryInfos = createBatteryInfos(createdDashboard);
        List<Indicator> indicators = createIndicators(createdDashboard);
        List<Metric> metrics = createMetrics(createdDashboard);

        batteryInfoRepository.saveAll(batteryInfos);
        indicatorRepository.saveAll(indicators);
        metricRepository.saveAll(metrics);

        return createdDashboard;
    }
}