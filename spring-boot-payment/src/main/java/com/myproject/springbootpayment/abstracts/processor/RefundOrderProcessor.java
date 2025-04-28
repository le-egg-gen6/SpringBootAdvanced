package com.myproject.springbootpayment.abstracts.processor;

/**
 * @author nguyenle
 * @since 2:51 PM Tue 4/22/2025
 */
public interface RefundOrderProcessor<T, V> {

	T refund(V v);

}
