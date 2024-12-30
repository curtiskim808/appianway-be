CREATE TABLE indicators (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            dashboard_uuid CHAR(36) NOT NULL,
                            type VARCHAR(50) NOT NULL,
                            status TINYINT(1) NOT NULL,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            CONSTRAINT fk_dashboard_indicator FOREIGN KEY (dashboard_uuid) REFERENCES dashboards (uuid) ON DELETE CASCADE
);