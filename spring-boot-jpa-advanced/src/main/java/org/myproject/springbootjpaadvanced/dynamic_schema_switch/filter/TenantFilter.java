package org.myproject.springbootjpaadvanced.dynamic_schema_switch.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.myproject.springbootjpaadvanced.dynamic_schema_switch.context.TenantContext;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author nguyenle
 * @since 9:28 PM Sat 2/22/2025
 */
public class TenantFilter extends OncePerRequestFilter {

    private static final String TENANT_REQUEST_HEADER = "X-Tenant-ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tenantId = request.getHeader(TENANT_REQUEST_HEADER);

        TenantContext.setTenantId(tenantId);

        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            // some logging :v
        } finally {
            TenantContext.clear();
        }

    }

}
