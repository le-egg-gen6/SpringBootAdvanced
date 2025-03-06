package org.myproject.springbootjpaadvanced.audit_log.audit;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

/**
 * @author nguyenle
 * @since 11:02 AM Thu 3/6/2025
 */
@Component
public class UTCDateTimeProvider implements DateTimeProvider {

	@Override
	public Optional<TemporalAccessor> getNow() {
		return Optional.of(Instant.now().atZone(ZoneOffset.UTC));
	}
}
