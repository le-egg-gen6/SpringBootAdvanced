package com.myproject.springbootmasterslavedbreplica.config.datasource;

import com.myproject.springbootmasterslavedbreplica.shared.DatasourceType;
import jakarta.persistence.EntityManagerFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author nguyenle
 * @since 4:33 PM Wed 2/12/2025
 */
@Configuration
@EnableJpaRepositories(
	basePackages = "com.myproject.springbootmasterslavedbreplica.repository",
	entityManagerFactoryRef = "masterEntityManagerFactoryRef",
	transactionManagerRef = "masterEntityManagerFactoryRef"
)
@EnableTransactionManagement
public class MasterSlaveDataSourceConfig {

	@Bean
	public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
		return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
	}

	@Bean(name = "masterDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.master")
	public DataSource masterDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "slaveDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.slave")
	public DataSource slaveDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "dataSource")
	public DataSource dataSource(
		@Qualifier("masterDataSource") DataSource masterDataSource,
		@Qualifier("slaveDataSource") DataSource slaveDataSource
	) {
		ReplicationRoutingDataSource dataSource = new ReplicationRoutingDataSource();

		Map<Object, Object> dataSourceMap = new HashMap<>();
		dataSourceMap.put(DatasourceType.MASTER, masterDataSource);
		dataSourceMap.put(DatasourceType.SLAVE, slaveDataSource);


		dataSource.setTargetDataSources(dataSourceMap);
		dataSource.setDefaultTargetDataSource(masterDataSource);

		return dataSource;
	}

	@Primary
	@Bean(name = "masterEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean masterEntityManagerFactoryBean(
		EntityManagerFactoryBuilder builder,
		@Qualifier("masterDataSource") DataSource masterDataSource
	) {
		return builder
			.dataSource(masterDataSource)
			.packages("com.myproject.springbootmasterslavedbreplica.repository")
			.persistenceUnit(DatasourceType.MASTER.name())
			.properties(Collections.singletonMap("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"))
			.build();
	}

	@Bean(name = "slaveEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean slaveEntityManagerFactoryBean(
		EntityManagerFactoryBuilder builder,
		@Qualifier("slaveDataSource") DataSource masterDataSource
	) {
		return builder
			.dataSource(masterDataSource)
			.packages("com.myproject.springbootmasterslavedbreplica.repository")
			.persistenceUnit(DatasourceType.MASTER.name())
			.properties(Collections.singletonMap("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"))
			.build();
	}

	@Primary
	@Bean(name = "masterTransactionManager")
	public PlatformTransactionManager writeTransactionManager(
		@Qualifier("masterEntityManagerFactory") EntityManagerFactory entityManagerFactory
	) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	@Bean(name = "slaveTransactionManager")
	public PlatformTransactionManager readTransactionManager(
		@Qualifier("slaveEntityManagerFactory") EntityManagerFactory entityManagerFactory
	) {
		return new JpaTransactionManager(entityManagerFactory);
	}

}
