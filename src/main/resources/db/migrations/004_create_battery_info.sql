CREATE TABLE battery_info (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              dashboard_uuid CHAR(36) NOT NULL, -- Foreign key column pointing to dashboards table
                              type VARCHAR(50) NOT NULL,       -- Enum type stored as a string
                              value FLOAT NOT NULL,            -- Matches the `value` field in your entity
                              unit CHAR(5),               -- Matches the `unit` field in your entity
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              CONSTRAINT fk_dashboard_battery_info FOREIGN KEY (dashboard_uuid) REFERENCES dashboards (uuid) ON DELETE CASCADE
);