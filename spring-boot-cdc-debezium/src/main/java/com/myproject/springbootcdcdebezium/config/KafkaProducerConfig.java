package com.myproject.springbootcdcdebezium.config;

import com.myproject.springbootcdcdebezium.utils.LogUtils;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nguyenle
 * @since 3:04 PM Fri 2/14/2025
 */
@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-server}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializerClassName;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializerClassName;

    private Class<?> getKeySerializerClass() {
        try {
            return Class.forName(keySerializerClassName);
        } catch (ClassNotFoundException e) {
            LogUtils.info("Error while get key serializer class for kafka");
            LogUtils.error(e);
            return StringSerializer.class;
        }
    }

    private Class<?> getValueSerializerClass() {
        try {
            return Class.forName(valueSerializerClassName);
        } catch (ClassNotFoundException e) {
            LogUtils.info("Error while get value serializer class for kafka");
            LogUtils.error(e);
            return StringSerializer.class;
        }
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, getKeySerializerClass());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, getValueSerializerClass());
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
