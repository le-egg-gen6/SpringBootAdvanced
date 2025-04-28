package com.myproject.springbootpayment.abstracts.processor;

/**
 * @author nguyenle
 * @since 2:50 PM Tue 4/22/2025
 */
public interface QueryOrderStatusProcessor<T, V> {
	T queryOrderStatus(V v);
}
