package com.myproject.springbootmasterslavedbreplica.config.datasource;

import com.myproject.springbootmasterslavedbreplica.shared.DatasourceType;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author nguyenle
 * @since 4:54 PM Wed 2/12/2025
 */
@Aspect
@Component
public class DataSourceAspect {

	@Before("execution(* com.myproject.springbootmasterslavedbreplica.repository..*.save*(..)) "
		+ "|| execution(* com.myproject.springbootmasterslavedbreplica.repository..*.insert*(..)) "
		+ "|| execution(* com.myproject.springbootmasterslavedbreplica.repository..*.update*(..)) "
		+ "|| execution(* com.myproject.springbootmasterslavedbreplica.repository..*.delete*(..))")
	public void setMasterDataSourceType() {
		DataSourceContextHolder.setDatasourceType(DatasourceType.MASTER);
	}

	@Before("execution(* com.myproject.springbootmasterslavedbreplica.repository..*.find*(..)) "
		+ "|| execution(* com.myproject.springbootmasterslavedbreplica.repository..*.get*(..)) "
		+ "|| execution(* com.myproject.springbootmasterslavedbreplica.repository..*.read*(..))")
	public void setSlaveDataSourceType() {
		DataSourceContextHolder.setDatasourceType(DatasourceType.SLAVE);
	}

	@After("@annotation(org.springframework.transaction.annotation.Transactional)")
	public void afterQueryExecution() {
		DataSourceContextHolder.clearDatasourceType();
	}

}
