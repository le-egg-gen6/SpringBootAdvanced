package com.myproject.springbootaopadvanced.retryable.config;

import com.myproject.springbootaopadvanced.retryable.annotation.XRetryable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author nguyenle
 * @since 2:31 PM Thu 2/20/2025
 */
@Aspect
@Component
public class RetryAspect {

	@Around("@annotation(retryable)")
	public Object retryMethod(ProceedingJoinPoint joinPoint, XRetryable retryable) throws Throwable {
		int attemps = 0;
		int maxAttempts = retryable.maxAttempts();

		long delay = retryable.delayMillis();

		while (attemps < maxAttempts) {
			try {
				return joinPoint.proceed();
			} catch (Throwable e) {
				attemps++;
				if (attemps >= maxAttempts) {
					throw e;
				}
			}
			Thread.sleep(delay);
		}
		return null;
	}

}
