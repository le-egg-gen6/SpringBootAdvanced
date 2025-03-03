package com.myproject.springbootehcache.tiered_cache.config;

import java.io.File;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${cache.persist-dir}")
	private String cachePersistDir;

	@Bean
	public CacheManager cacheManager() {
		File storageDir = new File(cachePersistDir);
		return CacheManagerBuilder.newCacheManagerBuilder()
			.with(new CacheManagerPersistenceConfiguration(storageDir))
			.build(true);
	}

}
