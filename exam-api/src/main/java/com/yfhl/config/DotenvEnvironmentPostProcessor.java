package com.yfhl.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Load variables from a .env file at the application working directory into Spring Environment.
 * This allows using a plain .env file (ignored by git) without extra scripts.
 */
public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    private static final String PROPERTY_SOURCE_NAME = "dotenvPropertySource";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            Path envPath = Paths.get(".env");
            if (!Files.exists(envPath)) {
                return;
            }

            List<String> lines = Files.readAllLines(envPath, StandardCharsets.UTF_8);
            Map<String, Object> map = new HashMap<>();
            for (String line : lines) {
                String s = line.trim();
                if (s.isEmpty() || s.startsWith("#")) continue;
                int idx = s.indexOf('=');
                if (idx <= 0) continue;
                String key = s.substring(0, idx).trim();
                String val = s.substring(idx + 1).trim();
                if ((val.startsWith("\"") && val.endsWith("\"")) || (val.startsWith("'") && val.endsWith("'"))) {
                    val = val.substring(1, val.length() - 1);
                }
                // only set if not already present (env or properties take precedence)
                if (environment.getProperty(key) == null) {
                    map.put(key, val);
                    // also set as a JVM system property when absent so placeholders
                    // that are resolved from system properties (e.g. datasource URL)
                    // get the value during early bootstrap (helps JDBC parsing)
                    try {
                        if (System.getProperty(key) == null && System.getenv(key) == null) {
                            System.setProperty(key, val);
                        }
                    } catch (Throwable ignored) {
                    }
                }
            }

            if (!map.isEmpty()) {
                MutablePropertySources sources = environment.getPropertySources();
                sources.addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, map));
            }
        } catch (Throwable ignored) {
            // Fail safe: do not prevent application from starting if .env cannot be read
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
