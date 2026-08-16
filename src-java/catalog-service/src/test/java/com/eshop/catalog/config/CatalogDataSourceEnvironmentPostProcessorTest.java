package com.eshop.catalog.config;

import org.junit.jupiter.api.Test;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CatalogDataSourceEnvironmentPostProcessorTest {

    @Test
    void mapsAspireNpgsqlConnectionStringToSpringDatasource() {
        StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addFirst(new MapPropertySource("testEnvironment", Map.of(
                "ConnectionStrings__catalogdb",
                "Host=postgres;Port=5432;Database=catalogdb;Username=catalog;Password=secret")));

        new CatalogDataSourceEnvironmentPostProcessor().postProcessEnvironment(environment, null);

        assertEquals("jdbc:postgresql://postgres:5432/catalogdb",
                environment.getProperty("spring.datasource.url"));
        assertEquals("catalog", environment.getProperty("spring.datasource.username"));
        assertEquals("secret", environment.getProperty("spring.datasource.password"));
        assertEquals("org.postgresql.Driver",
                environment.getProperty("spring.datasource.driver-class-name"));
    }

    @Test
    void mapsAfterSharedAspireEnvironmentPostProcessor() {
        StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addFirst(new MapPropertySource("testEnvironment", Map.of(
                "ConnectionStrings__catalogdb",
                "Host=postgres;Database=catalogdb;Username=catalog;Password=secret")));

        new com.eshop.defaults.AspireEnvironmentPostProcessor().postProcessEnvironment(environment, null);
        new CatalogDataSourceEnvironmentPostProcessor().postProcessEnvironment(environment, null);

        assertEquals("Host=postgres;Database=catalogdb;Username=catalog;Password=secret",
                environment.getProperty("eshop.connection-strings.catalogdb"));
        assertEquals("jdbc:postgresql://postgres:5432/catalogdb",
                environment.getProperty("spring.datasource.url"));
    }

    @Test
    void doesNotOverrideExplicitSpringDatasourceUrl() {
        StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addFirst(new MapPropertySource("testEnvironment", Map.of(
                "spring.datasource.url", "jdbc:postgresql://localhost:5432/explicit",
                "ConnectionStrings__catalogdb",
                "Host=postgres;Database=catalogdb;Username=catalog;Password=secret")));

        new CatalogDataSourceEnvironmentPostProcessor().postProcessEnvironment(environment, null);

        assertEquals("jdbc:postgresql://localhost:5432/explicit",
                environment.getProperty("spring.datasource.url"));
        assertNull(environment.getProperty("spring.datasource.username"));
    }
}
