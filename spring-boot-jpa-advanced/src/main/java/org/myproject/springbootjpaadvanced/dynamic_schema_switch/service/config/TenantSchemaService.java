package org.myproject.springbootjpaadvanced.dynamic_schema_switch.service.config;

import lombok.RequiredArgsConstructor;
import org.myproject.springbootjpaadvanced.dynamic_schema_switch.entity.config.TenantConfig;
import org.myproject.springbootjpaadvanced.dynamic_schema_switch.repository.config.TenantConfigRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nguyenle
 * @since 9:09 PM Sat 2/22/2025
 */
@Service
@RequiredArgsConstructor
public class TenantSchemaService {

    private final String DEFAULT_SCHEMA_NAME = "public";

    private final String CONFIG_SCHEMA_NAME = "config";

    private final TenantConfigRepository tenantConfigRepository;

    private Map<String, String> mapTenantId2TenantSchemaName= new HashMap<>();

    private void initialize() {
        List<TenantConfig> allTenantConfigs = tenantConfigRepository.findAll();
        for (TenantConfig tenantConfig : allTenantConfigs) {
            mapTenantId2TenantSchemaName.put(tenantConfig.getTenantId(), tenantConfig.getSchemaName());
        }
    }

    public String getTenantSchemaName(String tenantId) {
        if (mapTenantId2TenantSchemaName.isEmpty()) {
            initialize();
        }
        return mapTenantId2TenantSchemaName.getOrDefault(tenantId, DEFAULT_SCHEMA_NAME);
    }

    public String getConfigSchemaName() {
        return CONFIG_SCHEMA_NAME;
    }

}
