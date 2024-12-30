package dev.appianway.dashboard.model.dto;

import dev.appianway.dashboard.model.entity.Indicator;
import dev.appianway.dashboard.model.entity.IndicatorType;
import lombok.Builder;

import java.time.LocalDateTime;

public class IndicatorDTO {
    private Long id;
    private String dashboardUuid;
    private IndicatorType type;
    private Boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public IndicatorDTO(Indicator indicator) {
        this.id = indicator.getId();
        this.dashboardUuid = indicator.getDashboard().getUuid();
        this.type = indicator.getType();
        this.status = indicator.getStatus();
        this.createdAt = indicator.getCreatedAt();
        this.updatedAt = indicator.getUpdatedAt();
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

    public IndicatorType getType() {
        return type;
    }

    public void setType(IndicatorType type) {
        this.type = type;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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
