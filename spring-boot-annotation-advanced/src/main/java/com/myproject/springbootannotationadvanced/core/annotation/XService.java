package com.myproject.springbootannotationadvanced.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nguyenle
 * @since 2:44 PM Wed 2/19/2025
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@XComponent
public @interface XService {
	String value() default "";
}
