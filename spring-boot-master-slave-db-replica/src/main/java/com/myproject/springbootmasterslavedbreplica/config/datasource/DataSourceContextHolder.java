package com.myproject.springbootmasterslavedbreplica.config.datasource;

import com.myproject.springbootmasterslavedbreplica.shared.DatasourceType;

/**
 * @author nguyenle
 * @since 4:53 PM Wed 2/12/2025
 */
public class DataSourceContextHolder {

	private static final ThreadLocal<DatasourceType> contextHolder = new ThreadLocal<>();

	public static DatasourceType getDatasourceType() {
		return contextHolder.get();
	}

	public static void setDatasourceType(DatasourceType datasourceType) {
		contextHolder.set(datasourceType);
	}

	public static void clearDatasourceType() {
		contextHolder.remove();
	}

}
