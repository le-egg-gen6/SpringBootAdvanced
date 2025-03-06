package org.myproject.springbootjpaadvanced.audit_log.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.myproject.springbootjpaadvanced.audit_log.shared.AuditAction;

/**
 * @author nguyenle
 * @since 11:15 AM Thu 3/6/2025
 */
@Table(
	name = "t_audit_log"
)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String entityName;

	private Integer entityId;

	@Enumerated(EnumType.STRING)
	private AuditAction action;

	private String modifiedBy;

	private LocalDateTime modifiedDate;

}
