package dev.appianway.dashboard.model.dto;

import dev.appianway.dashboard.model.entity.Dashboard;
import lombok.Builder;

import java.time.LocalDateTime;

public class DashboardDTO {
    private String uuid;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DashboardDTO(Dashboard dashboard) {
        this.uuid = dashboard.getUuid();
        this.name = dashboard.getName();
        this.createdAt = dashboard.getCreatedAt();
        this.updatedAt = dashboard.getUpdatedAt();
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}