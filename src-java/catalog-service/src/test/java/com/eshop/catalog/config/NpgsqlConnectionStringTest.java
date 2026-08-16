package com.eshop.catalog.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NpgsqlConnectionStringTest {

    @Test
    void parsesAspireKeyValueFormat() {
        NpgsqlConnectionString parsed = NpgsqlConnectionString.parse(
                "Host=127.0.0.1;Port=55432;Username=postgres;Password=p@ss;Database=catalogdb");

        assertEquals("jdbc:postgresql://127.0.0.1:55432/catalogdb", parsed.jdbcUrl());
        assertEquals("postgres", parsed.username());
        assertEquals("p@ss", parsed.password());
    }

    @Test
    void parsesPostgresqlUri() {
        NpgsqlConnectionString parsed = NpgsqlConnectionString.parse(
                "postgresql://catalog:secret@postgres:5432/catalogdb");

        assertEquals("jdbc:postgresql://postgres:5432/catalogdb", parsed.jdbcUrl());
        assertEquals("catalog", parsed.username());
        assertEquals("secret", parsed.password());
    }

    @Test
    void rejectsMissingHost() {
        assertThrows(IllegalArgumentException.class,
                () -> NpgsqlConnectionString.parse("Database=catalogdb;Username=catalog"));
    }
}
