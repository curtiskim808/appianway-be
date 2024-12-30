package dev.appianway.dashboard;

import dev.appianway.dashboard.service.InitialSetupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;


@SpringBootApplication
@EnableScheduling
public class DashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(DashboardApplication.class, args);
	}


	@Autowired
	private InitialSetupService initialSetupService;

	@Bean
	@Profile("!test")
	public CommandLineRunner commandLineRunner() {
		return args -> {
			System.out.println("Starting the application");
			initialSetupService.setupInitialData();
			System.out.println("Data generated successfully!");
		};
	}

}
