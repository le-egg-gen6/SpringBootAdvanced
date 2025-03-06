package org.myproject.springbootjpaadvanced.audit_log.audit;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.myproject.springbootjpaadvanced.audit_log.entity.AuditLog;
import org.myproject.springbootjpaadvanced.audit_log.repository.AuditLogRepository;
import org.myproject.springbootjpaadvanced.audit_log.shared.AuditAction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author nguyenle
 * @since 11:21 AM Thu 3/6/2025
 */
@Component
@RequiredArgsConstructor
public class AuditLogListener {

	private final AuditLogRepository auditLogRepository;

	@PostPersist
	public void onCreate(Object entity) {
		logChange(entity, AuditAction.CREATE);
	}

	@PostUpdate
	public void onUpdate(Object entity) {
		logChange(entity, AuditAction.UPDATE);
	}

	@PostRemove
	public void onDelete(Object entity) {
		logChange(entity, AuditAction.DELETE);
	}

	private void logChange(Object entity, AuditAction auditAction) {
		AuditLog log = new AuditLog();
		log.setEntityName(entity.getClass().getSimpleName());
		log.setEntityId(getEntityId(entity));
		log.setAction(auditAction);
		log.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
		log.setModifiedDate(LocalDateTime.now(ZoneId.of("UTC")));

		auditLogRepository.save(log);
	}

	private Integer getEntityId(Object entity) {
		try {
			return (Integer) entity.getClass().getMethod("getId").invoke(entity);
		} catch (Exception e) {
			throw new RuntimeException("Cannot retrieve entity ID", e);
		}
	}

}
