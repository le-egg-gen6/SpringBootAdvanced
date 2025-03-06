package org.myproject.springbootjpaadvanced.audit_log.repository;

import org.myproject.springbootjpaadvanced.audit_log.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author nguyenle
 * @since 11:20 AM Thu 3/6/2025
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {

}
