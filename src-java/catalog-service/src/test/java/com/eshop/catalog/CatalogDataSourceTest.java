package com.eshop.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(PostgresTestConfiguration.class)
class CatalogDataSourceTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private Environment environment;

    @Test
    void validatesExistingSchemaWithoutCreatingTables() throws Exception {
        assertEquals("validate", environment.getProperty("spring.jpa.hibernate.ddl-auto"));
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet tables = statement.executeQuery(
                     "select table_name from information_schema.tables where table_schema = 'public'")) {
            Set<String> names = new HashSet<>();
            while (tables.next()) {
                names.add(tables.getString(1));
            }
            assertTrue(names.contains("Catalog"));
            assertTrue(names.contains("CatalogBrand"));
            assertTrue(names.contains("CatalogType"));
            assertFalse(names.contains("catalog_item"));
            assertFalse(names.contains("hibernate_sequence"));
        }
    }
}
