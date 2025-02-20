package com.myproject.springbootaopadvanced.api_rate_limit.config;

import com.myproject.springbootaopadvanced.api_rate_limit.annotation.XRateLimiter;
import java.time.Instant;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author nguyenle
 * @since 1:43 PM Thu 2/20/2025
 */
@Aspect
@Component
public class RateLimitingAspect {

	private final Map<String, ConcurrentLinkedQueue<Long>> requestMap = new ConcurrentHashMap<>();

	@Around("@annotation(rateLimiter)")
	public Object enforceRateLimit(ProceedingJoinPoint joinPoint, XRateLimiter rateLimiter) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String key = signature.getDeclaringTypeName() + "." + signature.getMethod().getName();

		int maxRequests = rateLimiter.maxRequests();
		int windowSize = rateLimiter.windowSeconds();

		long now = Instant.now().getEpochSecond();

		requestMap.putIfAbsent(key, new ConcurrentLinkedQueue<>());
		Queue<Long> requestQueue = requestMap.get(key);
		while (!requestQueue.isEmpty() && requestQueue.peek() < now - windowSize) {
			requestQueue.poll();
		}

		if (requestQueue.size() >= maxRequests) {
			throw new RuntimeException("Request limit exceeded. Try again later.");
		}

		requestQueue.add(now);

		return joinPoint.proceed();
	}

}
