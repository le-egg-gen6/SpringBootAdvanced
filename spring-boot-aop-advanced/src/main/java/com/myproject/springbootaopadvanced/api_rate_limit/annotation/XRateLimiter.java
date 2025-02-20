package com.myproject.springbootaopadvanced.api_rate_limit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nguyenle
 * @since 1:41 PM Thu 2/20/2025
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XRateLimiter {

	int maxRequests() default 60;

	int windowSeconds() default 60;

	//meaning maxRequests per windowSeconds

}
