package com.myproject.springbootmasterslavedbsynchronization.service;

import com.myproject.springbootmasterslavedbsynchronization.entity.DBCDCLog;
import com.myproject.springbootmasterslavedbsynchronization.entity.LogState;
import com.myproject.springbootmasterslavedbsynchronization.repository.CDCLogRepository;
import com.myproject.springbootmasterslavedbsynchronization.utils.LogUtils;
import com.myproject.springbootmasterslavedbsynchronization.utils.SQLNativeQueryUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author nguyenle
 * @since 11:21 AM Tue 2/18/2025
 */
@Service
@RequiredArgsConstructor
public class CDCLogProcessService {

	private final int LOG_PROCESS_FAIL_THRESHOLD = 10;

	private final CDCLogRepository cdcLogRepository;

	private final int CORE_POOL_SIZE = 2;

	private final int MAXIMUM_POOL_SIZE = 5;

	private final int KEEP_ALIVE_TIME_IN_MINUTE = 1;

	private ExecutorService executorService;

	@PostConstruct
	private void init() {
		executorService = new ThreadPoolExecutor(
			CORE_POOL_SIZE,
			MAXIMUM_POOL_SIZE,
			KEEP_ALIVE_TIME_IN_MINUTE,
			TimeUnit.MINUTES,
			new LinkedBlockingQueue<>()
		);
	}

	@Scheduled(initialDelay = 0, fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
	private void processFailedLog() {
		List<DBCDCLog> logs = cdcLogRepository.findDBCDCLogByStateAndIgnoredOrderByReceiveDateAsc(LogState.NO_PROCESSED, false);
		for (DBCDCLog log : logs) {

		}
	}

	private void logProcessSuccess(DBCDCLog log) {
		log.setState(LogState.PROCESSED);
		cdcLogRepository.save(log);
	}

	private void logProcessFail(DBCDCLog log) {
		int currentProcessFailed = log.getNumberProcessFailed();
		log.setNumberProcessFailed(currentProcessFailed + 1);
		if (log.getNumberProcessFailed() > LOG_PROCESS_FAIL_THRESHOLD) {
			log.setIgnored(true);
		}
		cdcLogRepository.save(log);
	}

	public void processLogAsync(String log) {
		DBCDCLog dbcdcLog = new DBCDCLog(log);
		try {
			cdcLogRepository.save(dbcdcLog);
		} catch (Throwable t) {
			LogUtils.info("Log not saved to db, log string: " + log);
			LogUtils.error(t);
			return;
		}
		CompletableFuture.runAsync(() -> {
			try {
				String sqlNativeQuery = SQLNativeQueryUtils.buildNativeQuery(log);

			} catch (Throwable t) {
				LogUtils.info("Log not processed successfully, log string " + log);
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
