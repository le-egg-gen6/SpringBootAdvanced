package com.myproject.springbootpayment.abstracts.processor;

/**
 * @author nguyenle
 * @since 2:49 PM Tue 4/22/2025
 */
public interface CreateOrderProcessor<T, V> {
	T createOrder(V v);
}
