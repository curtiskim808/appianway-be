package dev.appianway.dashboard.model.entity;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;
// Metric entity for to define the metric of the dashboard.
@Entity
@Table(name = "metrics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign Key to Dashboard
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_uuid", nullable = false)
    private Dashboard dashboard;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MetricType type;

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
