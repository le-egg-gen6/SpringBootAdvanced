package org.myproject.springbootjpaadvanced.audit_log.audit;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author nguyenle
 * @since 11:03 AM Thu 3/6/2025
 */
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.of("SYSTEM");
		}

		return Optional.of(authentication.getName());
	}
}
