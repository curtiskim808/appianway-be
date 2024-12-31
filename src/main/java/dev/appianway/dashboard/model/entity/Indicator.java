package dev.appianway.dashboard.model.entity;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;
// Indicator entity for to define the type of indicator.
@Entity
@Table(name = "indicators")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Indicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign Key to Dashboard
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_uuid", nullable = false)
    private Dashboard dashboard;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private IndicatorType type;

    @Column(name = "status", nullable = false)
    private Boolean status;

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
