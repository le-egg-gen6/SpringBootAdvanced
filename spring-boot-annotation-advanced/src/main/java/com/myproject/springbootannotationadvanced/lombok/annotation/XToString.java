package com.myproject.springbootannotationadvanced.lombok.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nguyenle
 * @since 4:41 PM Wed 2/19/2025
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface XToString {

    boolean includeFieldName() default true;

    String[] exclude() default {};

}
