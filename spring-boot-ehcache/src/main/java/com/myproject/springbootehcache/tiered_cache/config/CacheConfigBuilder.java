package com.myproject.springbootehcache.tiered_cache.config;

import java.time.Duration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;

/**
 * @author nguyenle
 * @since 4:34 PM Mon 3/3/2025
 */
public class CacheConfigBuilder<K, V> {
	private Class<K> keyType;
	private Class<V> valueType;

	private long heapSize;

	private boolean useOffHeapMemory;
	private long offHeapSize;
	private MemoryUnit offHeapMemoryUnit;

	private boolean useDiskMemory;
	private long diskSize;
	private MemoryUnit diskMemoryUnit;
	private boolean persistence;

	private boolean timeToLive;
	private Duration ttlDuration;

	private boolean timeToIdle;
	private Duration ttiDuration;

	public CacheConfigBuilder<K, V> keyType(Class<K> keyType) {
		this.keyType = keyType;
		return this;
	}

	public CacheConfigBuilder<K, V> valueType(Class<V> valueType) {
		this.valueType = valueType;
		return this;
	}

	public CacheConfigBuilder<K, V> heapSize(long heapSize) {
		this.heapSize = heapSize;
		return this;
	}

	public CacheConfigBuilder<K, V> useOffHeapMemory(boolean use, long size, MemoryUnit unit) {
		this.useOffHeapMemory = use;
		this.offHeapSize = size;
		this.offHeapMemoryUnit = unit;
		return this;
	}

	public CacheConfigBuilder<K, V> useDiskMemory(boolean use, long size, MemoryUnit unit) {
		this.useDiskMemory = use;
		this.diskSize = size;
		this.diskMemoryUnit = unit;
		return this;
	}

	public CacheConfigBuilder<K, V> persistence(boolean persistence) {
		this.persistence = persistence;
		return this;
	}

	public CacheConfigBuilder<K, V> timeToLive(boolean enabled, Duration duration) {
		this.timeToLive = enabled;
		this.ttlDuration = duration;
		return this;
	}

	public CacheConfigBuilder<K, V> timeToIdle(boolean enabled, Duration duration) {
		this.timeToIdle = enabled;
		this.ttiDuration = duration;
		return this;
	}

	public CacheConfigurationBuilder<K, V> build() {
		CacheConfigurationBuilder<K, V> config = CacheConfigurationBuilder.newCacheConfigurationBuilder(
			keyType, valueType, ResourcePoolsBuilder.heap(heapSize)
		);

		if (useOffHeapMemory) {
			config = config.withResourcePools(ResourcePoolsBuilder
				.heap(heapSize)
				.offheap(offHeapSize, offHeapMemoryUnit));
		}

		if (useDiskMemory) {
			config = config.withResourcePools(ResourcePoolsBuilder
				.heap(heapSize)
				.disk(diskSize, diskMemoryUnit, persistence));
		}

		if (timeToLive) {
			config = config.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(ttlDuration));
		}

		if (timeToIdle) {
			config = config.withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(ttiDuration));
		}

		return config;
	}
}
