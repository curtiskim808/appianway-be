package dev.appianway.dashboard.model.dto;

import dev.appianway.dashboard.model.entity.Metric;
import dev.appianway.dashboard.model.entity.MetricType;
import lombok.Builder;

import java.time.LocalDateTime;

public class MetricDTO {
    private Long id;
    private String dashboardUuid;
    private MetricType type;
    private Float value;
    private String unit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MetricDTO(Metric metric) {
        this.id = metric.getId();
        this.dashboardUuid = metric.getDashboard().getUuid();
        this.type = metric.getType();
        this.value = metric.getValue();
        this.unit = metric.getUnit();
        this.createdAt = metric.getCreatedAt();
        this.updatedAt = metric.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDashboardUuid() {
        return dashboardUuid;
    }

    public void setDashboardUuid(String dashboardUuid) {
        this.dashboardUuid = dashboardUuid;
    }

    public MetricType getType() {
        return type;
    }

    public void setType(MetricType type) {
        this.type = type;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
