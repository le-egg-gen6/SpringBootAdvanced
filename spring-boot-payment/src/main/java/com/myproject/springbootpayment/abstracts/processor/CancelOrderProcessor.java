package com.myproject.springbootpayment.abstracts.processor;

/**
 * @author nguyenle
 * @since 10:13 AM Mon 4/28/2025
 */
public interface CancelOrderProcessor<T, V> {
	T cancelOrder(V v);
}
