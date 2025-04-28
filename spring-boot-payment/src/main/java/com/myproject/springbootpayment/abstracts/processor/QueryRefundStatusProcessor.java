package com.myproject.springbootpayment.abstracts.processor;

/**
 * @author nguyenle
 * @since 2:52 PM Tue 4/22/2025
 */
public interface QueryRefundStatusProcessor<T, V> {
	T queryRefundStatus(V v);
}
