package com.myproject.springbootmasterslavedbsynchronization.config.bean;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.LazyInitializationExcludeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author nguyenle
 * @since 12:56 AM Tue 2/18/2025
 */
@Configuration
public class BeanInitializerConfig {

    @Bean
    public LazyInitializationExcludeFilter lazyInitializationExcludeFilter() {
        return LazyInitializationExcludeFilter.forBeanTypes(HikariDataSource.class);
    }

}
