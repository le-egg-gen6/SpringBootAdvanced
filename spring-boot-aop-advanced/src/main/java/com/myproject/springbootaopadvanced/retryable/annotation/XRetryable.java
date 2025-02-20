package com.myproject.springbootaopadvanced.retryable.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nguyenle
 * @since 2:28 PM Thu 2/20/2025
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XRetryable {

	int maxAttempts() default 3;

	long delayMillis() default 3000;

}
