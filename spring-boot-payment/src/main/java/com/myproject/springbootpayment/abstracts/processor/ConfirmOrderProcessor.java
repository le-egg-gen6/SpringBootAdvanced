package com.myproject.springbootpayment.abstracts.processor;

/**
 * @author nguyenle
 * @since 10:24 PM Tue 4/22/2025
 */
public interface ConfirmOrderProcessor<T, V> {

    T confirmOrder(V v);

}
