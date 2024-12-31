package dev.appianway.dashboard.model.dto;

import dev.appianway.dashboard.model.entity.BatteryInfo;
import dev.appianway.dashboard.model.entity.BatteryInfoType;
import lombok.Builder;

import java.time.LocalDateTime;
// BatteryInfoDTO for to transfer data between a remote client and the server.
public class BatteryInfoDTO {
    private Long id;
    private String dashboardUuid;
    private Float value;
    private String unit;
    private BatteryInfoType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BatteryInfoDTO(BatteryInfo batteryInfo) {
        this.id = batteryInfo.getId();
        this.dashboardUuid = batteryInfo.getDashboard().getUuid();
        this.value = batteryInfo.getValue();
        this.unit = batteryInfo.getUnit();
        this.type = batteryInfo.getType();
        this.createdAt = batteryInfo.getCreatedAt();
        this.updatedAt = batteryInfo.getUpdatedAt();
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

    public BatteryInfoType getType() {
        return type;
    }

    public void setType(BatteryInfoType type) {
        this.type = type;
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
}