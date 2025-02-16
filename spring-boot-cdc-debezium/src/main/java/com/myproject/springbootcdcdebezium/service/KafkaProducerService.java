package com.myproject.springbootcdcdebezium.service;

import com.myproject.springbootcdcdebezium.entity.DBCDCLog;
import com.myproject.springbootcdcdebezium.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author nguyenle
 * @since 3:05 PM Fri 2/14/2025
 */
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final String CDC_LOG_TOPIC = "cdc_log";

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final CDCLogService cdcLogService;

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    public void fetchEndSend() {
        List<DBCDCLog> allUnsentLog = cdcLogService.getAllUnsentLog();
        for (DBCDCLog log : allUnsentLog) {
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(CDC_LOG_TOPIC, log.getLog());
            future.thenAccept(sendResult -> {
                cdcLogService.logSentSuccess(log);
            }).exceptionally(ex -> {
                cdcLogService.logSentFailed(log);
                LogUtils.info("DBCDCLog with id " + log.getId() + " has error while sending");
                LogUtils.error(ex);
                return null;
            });
        }
    }

}
