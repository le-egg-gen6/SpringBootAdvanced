package org.myproject.springbootjpaadvanced.dynamic_schema_switch.entity.config;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @author nguyenle
 * @since 9:01 PM Sat 2/22/2025
 */
@Table(
        name = "tenant_config"
)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TenantConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String tenantId;

    @Column(unique = true, nullable = false)
    private String schemaName;

}

