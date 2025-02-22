package org.myproject.springbootjpaadvanced.dynamic_schema_switch.repository.config;

import org.myproject.springbootjpaadvanced.dynamic_schema_switch.annotation.ConfigRepository;
import org.myproject.springbootjpaadvanced.dynamic_schema_switch.entity.config.TenantConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author nguyenle
 * @since 9:04 PM Sat 2/22/2025
 */
@ConfigRepository
@Repository
public interface TenantConfigRepository extends JpaRepository<TenantConfig, Long> {

    List<TenantConfig> findAllTenantConfigs();

}
