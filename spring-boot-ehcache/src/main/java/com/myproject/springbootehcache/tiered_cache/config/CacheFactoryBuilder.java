package com.myproject.springbootehcache.tiered_cache.config;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.springframework.stereotype.Component;

/**
 * @author nguyenle
 * @since 4:19 PM Mon 3/3/2025
 */
@Component
@RequiredArgsConstructor
public class CacheFactoryBuilder {

	private final CacheManager cacheManager;

	private final Map<String, Cache<?, ?>> caches = new HashMap<>();

	public <K, V> Cache<K, V> build(
		String name,
		CacheConfiguration<K, V> cacheConfiguration
	) {
		Cache<K, V> cache = cacheManager.createCache(name, cacheConfiguration);
		caches.put(name, cache);
		return cache;
	}

	public Cache<?, ?> getCache(String name) {
		return caches.get(name);
	}
}
