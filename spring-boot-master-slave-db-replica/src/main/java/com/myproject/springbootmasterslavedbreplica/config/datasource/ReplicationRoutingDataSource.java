package com.myproject.springbootmasterslavedbreplica.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author nguyenle
 * @since 4:50 PM Wed 2/12/2025
 */
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

	private static final ThreadLocal<Boolean> isReadOnly = ThreadLocal.withInitial(() -> Boolean.FALSE);

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceContextHolder.getDatasourceType();
	}

	public static void setReadOnly() {
		isReadOnly.set(Boolean.TRUE);
	}

	public static void setWrite() {
		isReadOnly.set(Boolean.FALSE);
	}

}
