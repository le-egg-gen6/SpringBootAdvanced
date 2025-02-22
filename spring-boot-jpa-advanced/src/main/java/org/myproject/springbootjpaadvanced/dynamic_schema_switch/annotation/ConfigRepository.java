package org.myproject.springbootjpaadvanced.dynamic_schema_switch.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nguyenle
 * @since 9:06 PM Sat 2/22/2025
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigRepository {
}
