package dev.appianway.dashboard.service;

import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.repository.BatteryInfoRepository;
import dev.appianway.dashboard.repository.DashboardRepository;
import dev.appianway.dashboard.repository.IndicatorRepository;
import dev.appianway.dashboard.repository.MetricRepository;
import dev.appianway.dashboard.scheduled.SchedulerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;

    public Dashboard getFirstDashboard() {
        return dashboardRepository.findAll().get(0);
    }

    public Optional<Dashboard> getDashboard(String uuid) {
        Optional<Dashboard> dashboard = dashboardRepository.findByUuid(uuid);
        return dashboard;
    }

    public List<Dashboard> getAllDashboardsWithChargingOn() {
        return dashboardRepository.findDashboardsWithChargingOn();
    }

    public List<Dashboard> getAllDashboardsWithMotorOn() {
        return dashboardRepository.findDashboardsWithMotorOn();
    }

    public List<Dashboard> getAllDashboardsWithMotorOffAndChargingOff() {
        return dashboardRepository.findDashboardsWithMotorOffAndChargingOff();
    }

    public Dashboard saveDashboard(Dashboard dashboard) {
        return dashboardRepository.save(dashboard);
    }

    @Transactional
    public void deleteDashboard(String uuid) {
        dashboardRepository.deleteByUuid(uuid);
    }
}
