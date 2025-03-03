package com.myproject.springbootehcache.tiered_cache.config;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author nguyenle
 * @since 3:02 PM Mon 3/3/2025
 */
@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public CacheManager cacheManager() {
		return CacheManagerBuilder.newCacheManagerBuilder().build(true);
	}

}
