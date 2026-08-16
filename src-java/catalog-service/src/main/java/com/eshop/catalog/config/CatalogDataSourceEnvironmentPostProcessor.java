package com.eshop.catalog.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Maps Aspire {@code ConnectionStrings__catalogdb} onto Spring datasource properties.
 */
public final class CatalogDataSourceEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    static final String SOURCE = "eshopCatalogDataSource";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (environment.containsProperty("spring.datasource.url")) {
            return;
        }
        String connectionString = firstNonBlank(environment,
                "eshop.connection-strings.catalogdb",
                "ConnectionStrings__catalogdb");
        if (connectionString == null) {
            return;
        }

        NpgsqlConnectionString parsed = NpgsqlConnectionString.parse(connectionString);
        Map<String, Object> mapped = new LinkedHashMap<>();
        mapped.put("spring.datasource.url", parsed.jdbcUrl());
        mapped.put("spring.datasource.driver-class-name", "org.postgresql.Driver");
        if (parsed.username() != null && !parsed.username().isBlank()) {
            mapped.put("spring.datasource.username", parsed.username());
        }
        if (parsed.password() != null) {
            mapped.put("spring.datasource.password", parsed.password());
        }
        environment.getPropertySources().addFirst(new MapPropertySource(SOURCE, mapped));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    private static String firstNonBlank(ConfigurableEnvironment environment, String... names) {
        for (String name : names) {
            String value = environment.getProperty(name);
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}
