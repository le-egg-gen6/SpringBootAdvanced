package com.myproject.springbootmasterslavedbreplica.config.bean;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.LazyInitializationExcludeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author nguyenle
 * @since 4:55 PM Thu 2/13/2025
 */
@Configuration
public class BeanInitializerConfig {

	@Bean
	public LazyInitializationExcludeFilter lazyInitializationExcludeFilter() {
		return LazyInitializationExcludeFilter.forBeanTypes(HikariDataSource.class);
	}

}
