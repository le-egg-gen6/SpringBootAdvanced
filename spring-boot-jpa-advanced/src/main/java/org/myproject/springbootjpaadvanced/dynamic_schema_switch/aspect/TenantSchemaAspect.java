package org.myproject.springbootjpaadvanced.dynamic_schema_switch.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.myproject.springbootjpaadvanced.dynamic_schema_switch.annotation.ConfigRepository;
import org.myproject.springbootjpaadvanced.dynamic_schema_switch.context.TenantContext;
import org.myproject.springbootjpaadvanced.dynamic_schema_switch.service.config.TenantSchemaService;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author nguyenle
 * @since 9:08 PM Sat 2/22/2025
 */
@Aspect
@Component
@RequiredArgsConstructor
public class TenantSchemaAspect {

    private final TenantSchemaService tenantSchemaService;

    private final DataSource dataSource;

    @Around("execution( * org.myproject.springbootjpaadvanced.dynamic_schema_switch.repository..*(..))")
    public Object switchSchema(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Class<?> targetClass = proceedingJoinPoint.getTarget().getClass();

        String schemaName = null;

        if (isQueryInConfigurationSchema(targetClass)) {
            schemaName = tenantSchemaService.getConfigSchemaName();
        } else {
            String tenantId = TenantContext.getTenantId();
            schemaName = tenantSchemaService.getTenantSchemaName(tenantId);
        }

        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().execute("SET search_path TO " + schemaName);
        }

        return proceedingJoinPoint.proceed();

    }

    private boolean isQueryInConfigurationSchema(Class<?> clazz) {
        for (Class<?> inteface : clazz.getInterfaces()) {
            if (inteface.isAnnotationPresent(ConfigRepository.class)) {
                return true;
            }
        }
        return false;
    }

}
