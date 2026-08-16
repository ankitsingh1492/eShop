package com.eshop.catalog.config;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Parses Aspire/.NET Npgsql connection strings into Spring JDBC properties.
 */
public final class NpgsqlConnectionString {
    private final String jdbcUrl;
    private final String username;
    private final String password;

    private NpgsqlConnectionString(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public static NpgsqlConnectionString parse(String raw) {
        Objects.requireNonNull(raw, "connectionString");
        String value = raw.trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("catalogdb connection string must not be blank");
        }
        if (startsWithIgnoreCase(value, "jdbc:postgresql:")) {
            return fromJdbcUrl(value);
        }
        if (startsWithIgnoreCase(value, "postgres://") || startsWithIgnoreCase(value, "postgresql://")) {
            String uri = startsWithIgnoreCase(value, "postgres://")
                    ? "postgresql://" + value.substring("postgres://".length())
                    : value;
            return fromUri(URI.create(uri));
        }
        return fromKeyValue(value);
    }

    public String jdbcUrl() {
        return jdbcUrl;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    private static NpgsqlConnectionString fromJdbcUrl(String jdbcUrl) {
        URI uri = URI.create(jdbcUrl.substring("jdbc:".length()));
        String userInfo = uri.getUserInfo();
        String username = null;
        String password = null;
        if (userInfo != null) {
            int colon = userInfo.indexOf(':');
            if (colon < 0) {
                username = userInfo;
            } else {
                username = userInfo.substring(0, colon);
                password = userInfo.substring(colon + 1);
            }
        }
        return new NpgsqlConnectionString(jdbcUrl, username, password);
    }

    private static NpgsqlConnectionString fromUri(URI uri) {
        String host = uri.getHost();
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException("catalogdb connection string is missing Host");
        }
        int port = uri.getPort() > 0 ? uri.getPort() : 5432;
        String database = uri.getPath();
        if (database == null || database.isBlank() || "/".equals(database)) {
            throw new IllegalArgumentException("catalogdb connection string is missing Database");
        }
        if (database.startsWith("/")) {
            database = database.substring(1);
        }
        String username = null;
        String password = null;
        String userInfo = uri.getUserInfo();
        if (userInfo != null) {
            int colon = userInfo.indexOf(':');
            if (colon < 0) {
                username = userInfo;
            } else {
                username = userInfo.substring(0, colon);
                password = userInfo.substring(colon + 1);
            }
        }
        return new NpgsqlConnectionString(jdbcUrl(host, port, database), username, password);
    }

    private static NpgsqlConnectionString fromKeyValue(String value) {
        Map<String, String> parts = new LinkedHashMap<>();
        for (String segment : value.split(";")) {
            if (segment.isBlank()) {
                continue;
            }
            int separator = segment.indexOf('=');
            if (separator < 1) {
                continue;
            }
            String key = segment.substring(0, separator).trim().toLowerCase(Locale.ROOT);
            String partValue = segment.substring(separator + 1).trim();
            parts.put(key, partValue);
        }
        String host = first(parts, "host", "server", "data source");
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException("catalogdb connection string is missing Host");
        }
        int port = 5432;
        String portValue = first(parts, "port");
        if (portValue != null && !portValue.isBlank()) {
            port = Integer.parseInt(portValue);
        }
        String database = first(parts, "database", "initial catalog");
        if (database == null || database.isBlank()) {
            throw new IllegalArgumentException("catalogdb connection string is missing Database");
        }
        String username = first(parts, "username", "user id", "uid", "user");
        String password = first(parts, "password", "pwd");
        return new NpgsqlConnectionString(jdbcUrl(host, port, database), username, password);
    }

    private static String jdbcUrl(String host, int port, String database) {
        return "jdbc:postgresql://" + host + ":" + port + "/" + database;
    }

    private static String first(Map<String, String> parts, String... keys) {
        for (String key : keys) {
            if (parts.containsKey(key)) {
                return parts.get(key);
            }
        }
        return null;
    }

    private static boolean startsWithIgnoreCase(String value, String prefix) {
        return value.regionMatches(true, 0, prefix, 0, prefix.length());
    }
}
