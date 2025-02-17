package com.myproject.springbootcdcdebezium.config;

import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author nguyenle
 * @since 3:03 PM Fri 2/14/2025
 */
@Component
@RequiredArgsConstructor
public class DebeziumCDCConfig {

    @Value("${debezium.connector.name}")
    private String connectorName;

    @Value("${debezium.connector.class}")
    private String connectorClass;

    @Value("${debezium.connector.database.hostname}")
    private String dbHostname;

    @Value("${debezium.connector.database.port}")
    private String dbPort;

    @Value("${debezium.connector.database.user}")
    private String dbUser;

    @Value("${debezium.connector.database.password}")
    private String dbPassword;

    @Value("${debezium.connector.database.dbname}")
    private String dbName;

    @Value("${debezium.connector.database.server-name}")
    private String serverName;

    @Value("${debezium.connector.slot.name}")
    private String slotName;

    @Value("${debezium.connector.publication.name}")
    private String publicationName;

    @Value("${debezium.connector.plugin.name}")
    private String pluginName;

    @Value("${debezium.connector.offset.storage}")
    private String offsetStorage;

    @Value("${debezium.connector.offset.file.filename}")
    private String offsetFile;

    @Value("${debezium.connector.offset.flush-interval-ms}")
    private String offsetFlushInterval;

    @Value("${debezium.connector.table.include-list}")
    private String tableIncludeList;

    @Value("${debezium.connector.schema.include-changes}")
    private String schemaIncludeChanges;

    public Properties getPropertiesConfig() {
        Properties props = new Properties();
        props.setProperty("name", connectorName);
        props.setProperty("connector.class", connectorClass);

        props.setProperty("database.hostname", dbHostname);
        props.setProperty("database.port", dbPort);
        props.setProperty("database.user", dbUser);
        props.setProperty("database.password", dbPassword);
        props.setProperty("database.dbname", dbName);
        props.setProperty("database.server.name", serverName);

        props.setProperty("plugin.name", pluginName);
        props.setProperty("slot.name", slotName);
        props.setProperty("publication.name", publicationName);

        props.setProperty("table.include.list", tableIncludeList);
        props.setProperty("include.schema.changes", schemaIncludeChanges);

        props.setProperty("topic.prefix", serverName);

        // Offset storage settings
        props.setProperty("offset.storage", offsetStorage);
        props.setProperty("offset.storage.file.filename", offsetFile);
        props.setProperty("offset.flush.interval.ms", offsetFlushInterval);

        return props;
    }
}
