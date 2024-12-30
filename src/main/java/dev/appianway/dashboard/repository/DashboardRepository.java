package dev.appianway.dashboard.repository;

import dev.appianway.dashboard.model.entity.Dashboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, String> {
    @Query("SELECT DISTINCT d FROM Dashboard d JOIN d.indicators i WHERE i.type = 'BATTERY_CHARGING' AND i.status = true")
    List<Dashboard> findDashboardsWithChargingOn();

    @Query("SELECT DISTINCT d FROM Dashboard d JOIN d.metrics m WHERE m.type = 'MOTOR_SPEED' AND m.value > 0.0f")
    List<Dashboard> findDashboardsWithMotorOn();

    List<Dashboard> findAll();

    Optional<Dashboard> findByUuid(String uuid);
    @Query("SELECT DISTINCT d FROM Dashboard d JOIN d.indicators i JOIN d.metrics m WHERE i.type = 'BATTERY_CHARGING' AND i.status = false AND m.type = 'MOTOR_SPEED' AND m.value = 0.0f")
    List<Dashboard> findDashboardsWithMotorOffAndChargingOff();

    void deleteByUuid(String uuid);
}
