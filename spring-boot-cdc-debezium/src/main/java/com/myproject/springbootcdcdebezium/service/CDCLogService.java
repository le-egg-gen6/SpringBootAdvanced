package com.myproject.springbootcdcdebezium.service;

import com.myproject.springbootcdcdebezium.entity.DBCDCLog;
import com.myproject.springbootcdcdebezium.entity.LogState;
import com.myproject.springbootcdcdebezium.repository.CDCLogRepository;
import com.myproject.springbootcdcdebezium.utils.LogUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author nguyenle
 * @since 10:01 PM Sun 2/16/2025
 */
@Service
@RequiredArgsConstructor
public class CDCLogService {

    private final int LOG_SENT_FAIL_THRESHOLD = 5;
    
    private final CDCLogRepository logRepository;

    private final int CORE_POOL_SIZE = 2;

    private final int MAX_POOL_SIZE = 5;

    private final int KEEP_ALIVE_TIME_IN_MINUTE = 1;

    private ExecutorService executorService;

    @PostConstruct
    private void initialize() {
        executorService = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME_IN_MINUTE,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>()
        );
    }

    public List<DBCDCLog> getAllUnsentLog() {
        return logRepository.findDBCDCLogsByStateAndIgnoredOrderByCreatedDateAsc(LogState.NO_PUBLISHED, false);
    }

    public void logSentSuccess(DBCDCLog log) {
        log.setState(LogState.PUBLISHED);
        logRepository.save(log);
    }

    public void logSentFailed(DBCDCLog log) {
        int currentSentFailed = log.getNumberSentFailed();
        log.setNumberSentFailed(currentSentFailed + 1);
        if (log.getNumberSentFailed() > LOG_SENT_FAIL_THRESHOLD) {
            log.setIgnored(true);
        }
        logRepository.save(log);
    }

    public void saveAsync(String logStr) {
        DBCDCLog log = new DBCDCLog(logStr);
        CompletableFuture.runAsync(() -> {
            try {
                logRepository.save(log);
            } catch (Throwable t) {
                LogUtils.info("Log not saved to db, log string " + logStr);
                LogUtils.error(t);
            }
        }, executorService);
    }

    @PreDestroy
    private void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            LogUtils.info("Exception occurred while shutting down executor service");
            LogUtils.error(e);
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


}
