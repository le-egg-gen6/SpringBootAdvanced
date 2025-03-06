package org.myproject.springbootjpaadvanced.audit_log.audit;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author nguyenle
 * @since 11:01 AM Thu 3/6/2025
 */
@Configuration
@EnableJpaAuditing(
	auditorAwareRef = "auditorAware",
	dateTimeProviderRef = "dateTimeProvider"
)
public class AuditingConfig {

}
