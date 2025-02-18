package com.myproject.springbootmasterslavedbsynchronization.config.kafka;

import com.myproject.springbootmasterslavedbsynchronization.utils.LogUtils;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author nguyenle
 * @since 10:42 AM Tue 2/18/2025
 */
@Configuration
public class KafkaConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	@Value("${spring.kafka.consumer.auto-offset-reset}")
	private String autoOffsetReset;

	@Value("${spring.kafka.consumer.key-deserializer}")
	private String keyDeserializerClassName;

	@Value("${spring.kafka.consumer.value-deserializer}")
	private String valueDeserializerClassName;

	private Class<?> getKeyDeserializerClass() {
		try {
			return Class.forName(keyDeserializerClassName);
		} catch (ClassNotFoundException e) {
			LogUtils.info("Error while get key deserializer class for kafka");
			LogUtils.error(e);
			return StringDeserializer.class;
		}
	}

	private Class<?> getValueDeserializerClass() {
		try {
			return Class.forName(valueDeserializerClassName);
		} catch (ClassNotFoundException e) {
			LogUtils.info("Error while get value deserializer class for kafka");
			LogUtils.error(e);
			return StringDeserializer.class;
		}
	}

	@Bean
	public KafkaConsumer<String, String> kafkaConsumer() {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, getKeyDeserializerClass());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, getValueDeserializerClass());

		return new KafkaConsumer<>(props);
	}

}
