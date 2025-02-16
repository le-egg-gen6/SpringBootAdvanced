package com.myproject.springbootcdcdebezium.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author nguyenle
 * @since 3:03 PM Fri 2/14/2025
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "debezium")
public class DebeziumCDCConfig {

    private Map<String, String> database = new HashMap<>();
    private Map<String, String> connector = new HashMap<>();
    private Map<String, String> plugin = new HashMap<>();
    private String name;
    private String historyStorage;

    public Properties getDebeziumProperties() {
        Properties properties = new Properties();

        properties.put("name", name);
        properties.put("connector.class", connector.get("class"));
        properties.put("database.hostname", database.get("hostname"));
        properties.put("database.port", database.get("port"));
        properties.put("database.user", database.get("user"));
        properties.put("database.password", database.get("password"));
        properties.put("database.dbname", database.get("dbname"));
        properties.put("database.server.name", database.get("server-name"));
        properties.put("plugin.name", plugin.get("name"));
        properties.put("database.history", historyStorage);

        return properties;
    }
}
