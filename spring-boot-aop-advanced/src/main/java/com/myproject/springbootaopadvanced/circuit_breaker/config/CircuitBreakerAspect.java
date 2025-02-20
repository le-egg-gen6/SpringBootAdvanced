package com.myproject.springbootaopadvanced.circuit_breaker.config;

import com.myproject.springbootaopadvanced.circuit_breaker.annotation.XCircuitBreaker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class CircuitBreakerAspect {

	private final ConcurrentHashMap<String, AtomicInteger> failureCounts = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, Long> lastFailureTime = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, Boolean> isTestingRecovery = new ConcurrentHashMap<>();

	@Around("@annotation(circuitBreaker)")
	public Object applyCircuitBreaker(ProceedingJoinPoint joinPoint, XCircuitBreaker circuitBreaker) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String key = signature.getDeclaringTypeName() + "." + signature.getMethod().getName();

		int failureThreshold = circuitBreaker.failureThreshold();
		long resetTime = circuitBreaker.resetTimeMillis();
		String fallbackMethodName = circuitBreaker.fallbackMethod();

		if (lastFailureTime.containsKey(key)) {
			long timeSinceLastFailure = System.currentTimeMillis() - lastFailureTime.get(key);
			if (timeSinceLastFailure < resetTime) {
				return invokeFallback(joinPoint, fallbackMethodName);
			} else if (Boolean.TRUE.equals(isTestingRecovery.get(key))) {
				return invokeFallback(joinPoint, fallbackMethodName);
			} else {
				isTestingRecovery.put(key, true);
				return attemptRecovery(joinPoint, key, fallbackMethodName);
			}
		}

		return executeMethod(joinPoint, key, failureThreshold, fallbackMethodName);
	}

	private Object executeMethod(ProceedingJoinPoint joinPoint, String key, int failureThreshold, String fallbackMethodName) throws Throwable {
		try {
			Object result = joinPoint.proceed();
			failureCounts.remove(key);
			return result;
		} catch (Exception ex) {
			failureCounts.putIfAbsent(key, new AtomicInteger(0));
			int failures = failureCounts.get(key).incrementAndGet();

			if (failures >= failureThreshold) {
				lastFailureTime.put(key, System.currentTimeMillis()); // Open circuit
				return invokeFallback(joinPoint, fallbackMethodName);
			}
			throw ex;
		}
	}

	private Object attemptRecovery(ProceedingJoinPoint joinPoint, String key, String fallbackMethodName) {
		try {
			Object result = joinPoint.proceed(); // Try executing the original method again
			failureCounts.remove(key); // Reset failure count
			lastFailureTime.remove(key); // Close the circuit
			isTestingRecovery.remove(key);
			return result; // Recovery successful, return the result
		} catch (Throwable ex) {
			lastFailureTime.put(key, System.currentTimeMillis()); // Keep circuit open
			isTestingRecovery.remove(key);
			return invokeFallback(joinPoint, fallbackMethodName);
		}
	}

	private Object invokeFallback(ProceedingJoinPoint joinPoint, String fallbackMethodName) {
		Object target = joinPoint.getTarget();
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();

		try {
			Method fallbackMethod = target.getClass().getMethod(fallbackMethodName, signature.getParameterTypes());
			return fallbackMethod.invoke(target, joinPoint.getArgs());
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Fallback method '" + fallbackMethodName + "' not found");
		} catch (Exception e) {
			throw new RuntimeException("Error invoking fallback method: " + e.getMessage());
		}
	}
}
