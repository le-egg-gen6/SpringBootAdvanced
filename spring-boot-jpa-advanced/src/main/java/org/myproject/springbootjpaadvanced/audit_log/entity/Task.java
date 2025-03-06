package org.myproject.springbootjpaadvanced.audit_log.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author nguyenle
 * @since 11:00 AM Thu 3/6/2025
 */
@Table(
	name = "t_task"
)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String title;

	private String author;

	@CreatedBy
	private String createdBy;

	@CreatedDate
	private LocalDateTime createdDate;

	@LastModifiedBy
	private String lastModifiedBy;

	@LastModifiedDate
	private LocalDateTime lastModifiedDate;

}
