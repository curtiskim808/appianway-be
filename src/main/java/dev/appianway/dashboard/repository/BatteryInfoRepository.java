package dev.appianway.dashboard.repository;

import dev.appianway.dashboard.model.entity.BatteryInfo;
import dev.appianway.dashboard.model.entity.BatteryInfoType;
import dev.appianway.dashboard.model.entity.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatteryInfoRepository extends JpaRepository<BatteryInfo, Long> {
    List<BatteryInfo> findByDashboard(Dashboard dashboard);
    List<BatteryInfo> findByDashboardAndTypeIn(Dashboard dashboard, List<BatteryInfoType> types);
}

