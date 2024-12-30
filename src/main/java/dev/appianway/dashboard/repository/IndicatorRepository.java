package dev.appianway.dashboard.repository;

import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.model.entity.Indicator;
import dev.appianway.dashboard.model.entity.IndicatorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndicatorRepository extends JpaRepository<Indicator, Long> {
    List<Indicator> findByDashboard(Dashboard dashboard);
    List<Indicator> findByDashboardAndTypeIn(Dashboard dashboard, List<IndicatorType> types);
}
