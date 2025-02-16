package com.myproject.springbootcdcdebezium.service;

import com.myproject.springbootcdcdebezium.config.DebeziumCDCConfig;
import com.myproject.springbootcdcdebezium.utils.JsonUtils;
import com.myproject.springbootcdcdebezium.utils.LogUtils;
import io.debezium.config.Configuration;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.*;

/**
 * @author nguyenle
 * @since 3:05 PM Fri 2/14/2025
 */
@Service
@RequiredArgsConstructor
public class DebeziumCDCLogPullingService {

    private final DebeziumCDCConfig config;

    private final CDCLogService cdcLogService;

    private ExecutorService debeziumLogEventHandlingService;

    private final int CORE_POOL_SIZE = 5;

    private final int MAX_POOL_SIZE = 10;

    private final int KEEP_ALIVE_TIME_IN_MINUTE= 1;

    private ExecutorService eventHandlingService;

    private DebeziumEngine<ChangeEvent<String, String>> engine;

    @PostConstruct
    private void initialize() {
        debeziumLogEventHandlingService = Executors.newSingleThreadExecutor();
        eventHandlingService = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME_IN_MINUTE,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>()
        );

        Properties props = config.getDebeziumProperties();
        Configuration configuration = Configuration.from(props);

        engine = DebeziumEngine.create(Json.class)
                .using(configuration.asProperties())
                .notifying(this::handleEvent)
                .build();

        debeziumLogEventHandlingService.submit(() -> {
            try {
                engine.run();
            } catch (Exception ex) {
                LogUtils.info("Error while starting debezium");
                LogUtils.error(ex);
            }
        });
    }

    private void handleEvent(ChangeEvent<String, String> event) {
        try {
            eventHandlingService.execute(() -> processEvent(event));
        } catch (Exception ex) {
            LogUtils.info("Error while put event to event queue");
            LogUtils.info("Event: ");
            LogUtils.error(ex);
        }
    }

    private void processEvent(ChangeEvent<String, String> event) {
        String finalLog = JsonUtils.compressLog(event.key(), event.value());
        cdcLogService.saveAsync(finalLog);
    }

    @PreDestroy
    private void shutdown() {
        try {
            if (engine != null) {
                engine.close();
            }
            debeziumLogEventHandlingService.shutdown();
            eventHandlingService.shutdown();

            if (!debeziumLogEventHandlingService.awaitTermination(30, TimeUnit.SECONDS)) {
                debeziumLogEventHandlingService.shutdownNow();
            }

            if (!eventHandlingService.awaitTermination(30, TimeUnit.SECONDS)) {
                eventHandlingService.shutdownNow();
            }

        } catch (Exception ex) {
            LogUtils.info("Error while shutting down debezium");
            LogUtils.error(ex);
        }
    }

}
