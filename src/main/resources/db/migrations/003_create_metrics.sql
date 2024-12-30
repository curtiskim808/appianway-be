CREATE TABLE metrics (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         dashboard_uuid CHAR(36) NOT NULL, -- Foreign key column referencing dashboards
                         type VARCHAR(50) NOT NULL,       -- Enum type stored as a string
                         value FLOAT NOT NULL,            -- Matches the `value` field in your entity
                         unit CHAR(5),               -- Matches the `unit` field in your entity
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Default value when created
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Automatically updated
                         CONSTRAINT fk_dashboard_metric FOREIGN KEY (dashboard_uuid) REFERENCES dashboards (uuid) ON DELETE CASCADE
);