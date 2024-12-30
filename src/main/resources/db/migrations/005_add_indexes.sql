CREATE INDEX idx_metrics_dashboard_uuid ON metrics (dashboard_uuid);
CREATE INDEX idx_indicators_dashboard_uuid_type ON indicators (dashboard_uuid, type);
CREATE INDEX idx_battery_info_dashboard_uuid_type ON battery_info (dashboard_uuid, type);