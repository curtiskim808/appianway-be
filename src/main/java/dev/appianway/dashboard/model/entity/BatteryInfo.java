package dev.appianway.dashboard.model.entity;

import lombok.*;

import jakarta.persistence.*;

import java.time.LocalDateTime;
// BatteryInfo Entity for to store battery information.
@Entity
@Table(name = "battery_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatteryInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign Key to Dashboard
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_uuid", nullable = false)
    private Dashboard dashboard;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private BatteryInfoType type;

    @Column(name = "value", nullable = false)
    private Float value;

    @Column(name = "unit")
    private String unit;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }


    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
