package dev.appianway.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api-docs")
public class ApiDocsController {
    @GetMapping
    public String apiDocs(Model model) {
        List<ApiEndpoint> endpoints = new ArrayList<>();

        // Dashboard endpoints
        endpoints.add(new ApiEndpoint("GET", "/dashboards", "Get the main first dashboard", "{ \"uuid\": \"9123a325-f1d5-461b-a892-b6b2d5a7259b\", \"name\": \"Main Vehicle Dashboard\", \"createdAt\": \"2024-12-30T20:47:12\", \"updatedAt\": null }"));
        endpoints.add(new ApiEndpoint("GET", "/dashboards/{uuid}", "Get a dashboard by ID", "{ \"uuid\": \"9123a325-f1d5-461b-a892-b6b2d5a7259b\", \"name\": \"Main Vehicle Dashboard\", \"createdAt\": \"2024-12-30T20:47:12\", \"updatedAt\": null }"));
        endpoints.add(new ApiEndpoint("DELETE", "/dashboards/{uuid}", "Delete a dashboard", "Response 204 NO CONTENT\n No response body"));

        // Battery info endpoints
        endpoints.add(new ApiEndpoint("GET", "/dashboards/{dashboardId}/battery-info", "Get all battery information for a dashboard", "[{ \"id\": 18, \"dashboardUuid\": \"a9b6be38-bff9-4438-98ec-758de96ab77f\", \"value\": 19.5, \"unit\": \"%\", \"type\": \"REMAINING_CAPACITY\", \"createdAt\": \"2024-12-30T20:43:44\", \"updatedAt\": null }, { \"id\": 17, \"dashboardUuid\": \"a9b6be38-bff9-4438-98ec-758de96ab77f\", \"value\": 24.9, \"unit\": \"°C\", \"type\": \"TEMPERATURE\", \"createdAt\": \"2024-12-30T20:43:44\", \"updatedAt\": \"2024-12-30T20:44:39\" }]"));
        endpoints.add(new ApiEndpoint("GET", "/dashboards/{dashboardId}/battery-info?type={type}", "Get battery information by types for a dashboard", "[{ \"id\": 336, \"dashboardUuid\": \"5d61364c-6f8e-41f8-bc10-bdd17447cde3\", \"value\": 19.5, \"unit\": \"%\", \"type\": \"REMAINING_CAPACITY\", \"createdAt\": \"2024-12-30T20:10:10\", \"updatedAt\": null }, { \"id\": 335, \"dashboardUuid\": \"5d61364c-6f8e-41f8-bc10-bdd17447cde3\", \"value\": 23.1, \"unit\": \"°C\", \"type\": \"TEMPERATURE\", \"createdAt\": \"2024-12-30T20:10:10\", \"updatedAt\": \"2024-12-30T20:12:35\" }]"));

        // Indicator endpoints
        endpoints.add(new ApiEndpoint("GET", "/dashboards/{dashboardId}/indicators", "Get all indicators for a dashboard", "[{ \"id\": 1, \"dashboardUuid\": \"1cf707d2-0cdb-4022-a572-16b254ff1613\", \"type\": \"BATTERY_CHARGING\", \"status\": false, \"createdAt\": \"2024-12-30T20:18:40\", \"updatedAt\": null }, { \"id\": 2, \"dashboardUuid\": \"1cf707d2-0cdb-4022-a572-16b254ff1613\", \"type\": \"BATTERY_LOW\", \"status\": false, \"createdAt\": \"2024-12-30T20:18:40\", \"updatedAt\": null }, { \"id\": 3, \"dashboardUuid\": \"1cf707d2-0cdb-4022-a572-16b254ff1613\", \"type\": \"ENGINE_STATUS\", \"status\": false, \"createdAt\": \"2024-12-30T20:18:40\", \"updatedAt\": null }, { \"id\": 5, \"dashboardUuid\": \"1cf707d2-0cdb-4022-a572-16b254ff1613\", \"type\": \"MOTOR_STATUS\", \"status\": false, \"createdAt\": \"2024-12-30T20:18:40\", \"updatedAt\": null }, { \"id\": 4, \"dashboardUuid\": \"1cf707d2-0cdb-4022-a572-16b254ff1613\", \"type\": \"PARKING_BRAKE\", \"status\": false, \"createdAt\": \"2024-12-30T20:18:40\", \"updatedAt\": null }]"));
        endpoints.add(new ApiEndpoint("GET", "/dashboards/{dashboardId}/indicators?type={type}", "Get indicators by types for a dashboard", "[{ \"id\": 56, \"dashboardUuid\": \"9123a325-f1d5-461b-a892-b6b2d5a7259b\", \"type\": \"BATTERY_CHARGING\", \"status\": false, \"createdAt\": \"2024-12-30T20:47:12\", \"updatedAt\": null }, { \"id\": 57, \"dashboardUuid\": \"9123a325-f1d5-461b-a892-b6b2d5a7259b\", \"type\": \"BATTERY_LOW\", \"status\": false, \"createdAt\": \"2024-12-30T20:47:12\", \"updatedAt\": null }, { \"id\": 58, \"dashboardUuid\": \"9123a325-f1d5-461b-a892-b6b2d5a7259b\", \"type\": \"ENGINE_STATUS\", \"status\": false, \"createdAt\": \"2024-12-30T20:47:12\", \"updatedAt\": null }, { \"id\": 59, \"dashboardUuid\": \"9123a325-f1d5-461b-a892-b6b2d5a7259b\", \"type\": \"PARKING_BRAKE\", \"status\": false, \"createdAt\": \"2024-12-30T20:47:12\", \"updatedAt\": null }, { \"id\": 60, \"dashboardUuid\": \"9123a325-f1d5-461b-a892-b6b2d5a7259b\", \"type\": \"MOTOR_STATUS\", \"status\": false, \"createdAt\": \"2024-12-30T20:47:12\", \"updatedAt\": null }]"));
        endpoints.add(new ApiEndpoint("PUT", "/dashboards/{dashboardId}/indicators", "Update an indicator for a dashboard", "{ \"id\": 12, \"dashboardUuid\": \"dec747bf-a347-4e4c-ae7f-287bceee9a5d\", \"type\": \"BATTERY_LOW\", \"status\": false, \"createdAt\": \"2024-12-30T20:24:20\", \"updatedAt\": \"2024-12-30T20:27:28.410884\" }"));

        // Metric endpoints
        endpoints.add(new ApiEndpoint("GET", "/dashboards/{dashboardId}/metrics", "Get all metrics for a dashboard", "[{ \"id\": 17, \"dashboardUuid\": \"5b5421a4-7ce8-4355-85e2-9e4bd0229267\", \"type\": \"MOTOR_SPEED\", \"value\": 0, \"unit\": \"x200\", \"createdAt\": \"2024-12-30T20:30:44\", \"updatedAt\": null }, { \"id\": 18, \"dashboardUuid\": \"5b5421a4-7ce8-4355-85e2-9e4bd0229267\", \"type\": \"GEAR_RATIO\", \"value\": 3, \"unit\": \"RATIO\", \"createdAt\": \"2024-12-30T20:30:44\", \"updatedAt\": null }, { \"id\": 19, \"dashboardUuid\": \"5b5421a4-7ce8-4355-85e2-9e4bd0229267\", \"type\": \"MOTOR_RPM\", \"value\": 0, \"unit\": \"RPM\", \"createdAt\": \"2024-12-30T20:30:44\", \"updatedAt\": null }, { \"id\": 20, \"dashboardUuid\": \"5b5421a4-7ce8-4355-85e2-9e4bd0229267\", \"type\": \"POWER_INPUT\", \"value\": 0, \"unit\": \"KW\", \"createdAt\": \"2024-12-30T20:30:44\", \"updatedAt\": null }]"));
        endpoints.add(new ApiEndpoint("GET", "/dashboards/{dashboardId}/metrics?type={type}", "Get metrics by types for a dashboard", "[{ \"id\": 17, \"dashboardUuid\": \"5b5421a4-7ce8-4355-85e2-9e4bd0229267\", \"type\": \"MOTOR_SPEED\", \"value\": 0, \"unit\": \"x200\", \"createdAt\": \"2024-12-30T20:30:44\", \"updatedAt\": null }, { \"id\": 20, \"dashboardUuid\": \"5b5421a4-7ce8-4355-85e2-9e4bd0229267\", \"type\": \"POWER_INPUT\", \"value\": 0, \"unit\": \"KW\", \"createdAt\": \"2024-12-30T20:30:44\", \"updatedAt\": null }]"));
        endpoints.add(new ApiEndpoint("PUT", "/dashboards/{dashboardId}/metrics", "Update a metric for a dashboard", "{ \"id\": 17, \"type\": \"MOTOR_SPEED\", \"value\": 0, \"unit\": \"x200\" }"));

        // Dashboard setup endpoints for admin
        endpoints.add(new ApiEndpoint("POST", "/api/dashboard/setup", "Setup initial data from admin user", "Response 204 NO CONTENT\n No response body"));
        endpoints.add(new ApiEndpoint("POST", "/api/dashboard/new-dashboard-set", "Create a new dashboard set from admin user", "{ \"uuid\": \"26dd70c0-caef-4b56-b3ba-b764153eb475\", \"name\": \"Main Vehicle Dashboard\", \"createdAt\": \"2024-12-30T20:45:34.753478\", \"updatedAt\": null }"));

        model.addAttribute("endpoints", endpoints);
        return "api-docs";
    }
}

@Data
class ApiEndpoint {
    private final String method;
    private final String path;
    private final String description;
    private final String exampleResult;
}