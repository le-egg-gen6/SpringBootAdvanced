package org.myproject.springbootjpaadvanced.dynamic_schema_switch.context;

/**
 * @author nguyenle
 * @since 9:12 PM Sat 2/22/2025
 */
public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static String getTenantId() {
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }

}
