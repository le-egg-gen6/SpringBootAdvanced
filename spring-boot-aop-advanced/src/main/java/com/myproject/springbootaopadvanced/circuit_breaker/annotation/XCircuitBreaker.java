package com.myproject.springbootaopadvanced.circuit_breaker.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nguyenle
 * @since 2:36 PM Thu 2/20/2025
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XCircuitBreaker {

	int failureThreshold() default 3;

	long resetTimeMillis() default 10000;

	String fallbackMethod() default "";

}
