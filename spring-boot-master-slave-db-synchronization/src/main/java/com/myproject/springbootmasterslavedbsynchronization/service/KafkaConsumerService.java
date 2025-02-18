package com.myproject.springbootmasterslavedbsynchronization.service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author nguyenle
 * @since 10:47 AM Tue 2/18/2025
 */
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

	private final KafkaConsumer<String, String> consumer;

	private final CDCLogProcessService cdcLogProcessService;

	@Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
	public void fetchCDCLogTask() {
		ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(5000));

		for (ConsumerRecord<String, String> record : records) {
			cdcLogProcessService.processLogAsync(record.value());
		}

		consumer.commitAsync();
	}

}
