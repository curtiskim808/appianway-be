package dev.appianway.dashboard.repository;

import dev.appianway.dashboard.model.entity.Dashboard;
import dev.appianway.dashboard.model.entity.Metric;
import dev.appianway.dashboard.model.entity.MetricType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
// MetricRepository for to manage metrics.
@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {
    List<Metric> findByDashboard(Dashboard dashboard);
    List<Metric> findByDashboardAndTypeIn(Dashboard dashboard, List<MetricType> types);
}
