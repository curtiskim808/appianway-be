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

        // Add your endpoints
        endpoints.add(new ApiEndpoint("GET", "/dashboards/{dashboardId}/battery-info", "Get all battery information for a dashboard"));
        endpoints.add(new ApiEndpoint("GET", "/dashboards/{dashboardId}/battery-info?type={type}", "Get battery information by types for a dashboard"));
        endpoints.add(new ApiEndpoint("GET", "/dashboards", "Redirect to the first dashboard"));
        endpoints.add(new ApiEndpoint("GET", "/dashboards/{uuid}", "Get a dashboard by ID"));
        endpoints.add(new ApiEndpoint("POST", "/dashboards", "Create a new dashboard"));
        endpoints.add(new ApiEndpoint("GET", "/dashboards/{dashboardId}/indicators", "Get all indicators for a dashboard"));
        endpoints.add(new ApiEndpoint("GET", "/dashboards/{dashboardId}/indicators?type={type}", "Get indicators by types for a dashboard"));
        endpoints.add(new ApiEndpoint("PUT", "/dashboards/{dashboardId}/indicators", "Update an indicator for a dashboard"));
        endpoints.add(new ApiEndpoint("GET", "/dashboards/{dashboardId}/metrics", "Get all metrics for a dashboard"));
        endpoints.add(new ApiEndpoint("GET", "/dashboards/{dashboardId}/metrics?type={type}", "Get metrics by types for a dashboard"));
        endpoints.add(new ApiEndpoint("PUT", "/dashboards/{dashboardId}/metrics", "Update a metric for a dashboard"));

        model.addAttribute("endpoints", endpoints);
        return "api-docs";
    }
}

@Data
class ApiEndpoint {
    private final String method;
    private final String path;
    private final String description;
}