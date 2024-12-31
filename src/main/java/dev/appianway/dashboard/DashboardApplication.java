package dev.appianway.dashboard;

import dev.appianway.dashboard.service.InitialSetupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DashboardApplication {

	public static void main(String[] args) {
		String port = System.getenv("PORT");
		if (port != null) {
			System.setProperty("server.port", port);
		}

		SpringApplication.run(DashboardApplication.class, args);
	}

	@Autowired
	private InitialSetupService initialSetupService;

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			System.out.println("Starting the application");
			initialSetupService.setupInitialData();
			System.out.println("Data generated successfully!");
		};
	}

}
