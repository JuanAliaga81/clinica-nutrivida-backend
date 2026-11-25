package cl.nutrivida.gateway;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
		System.out.println("Gateway application started successfully.");
	}

	@Bean
	public CorsFilter configurarCorsPreflight() {
		return new CorsFilter(request -> {
			if (!CorsUtils.isPreFlightRequest(request)) {
				return null;
			}

			CorsConfiguration cors = new CorsConfiguration();
			cors.setAllowedOrigins(List.of("http://localhost:5173", "http://127.0.0.1:5173"));
			cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
			cors.setAllowedHeaders(List.of("*"));
			cors.setMaxAge(3600L);
			return cors;
		});
	}
}