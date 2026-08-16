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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(PostgresTestConfiguration.class)
class CatalogDataSourceTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private Environment environment;

    @Test
    void connectsWithoutCreatingOrModifyingSchema() throws Exception {
        assertEquals("validate", environment.getProperty("spring.jpa.hibernate.ddl-auto"));
        try (Connection connection = dataSource.getConnection()) {
            assertTrue(connection.isValid(5));
            try (Statement statement = connection.createStatement();
                 ResultSet tables = statement.executeQuery(
                         "select count(*) from information_schema.tables where table_schema = 'public'")) {
                tables.next();
                assertEquals(0, tables.getInt(1), "Hibernate must not create or alter tables");
            }
        }
    }
}
