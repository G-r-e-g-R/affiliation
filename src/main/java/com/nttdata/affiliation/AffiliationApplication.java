package com.nttdata.affiliation;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;

import java.time.Duration;


@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "APIs",
        version = "1.0",
        description = "Documentation APIs v1.0"))
@EnableEurekaClient
public class AffiliationApplication {
    /**
     * Constructor protegido.
     */
    protected AffiliationApplication() {
        super();
    }
    /**
     * Clase principal de las transacciones.
     * @param args
     */
    public static void main(final String[] args) {
        SpringApplication.run(AffiliationApplication.class, args);
    }

    /**
     * Configuración del Control del Circuit Breaker.
     * @return Customizer<ReactiveResilience4JCircuitBreakerFactory>
     */
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory>
    defaultCustomizer() {
        return factory -> factory.configureDefault(
                id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofSeconds(2))
                        .build())
                .build());
    }
}
