package com.myproject.springbootpayment.abstracts.processor;

/**
 * @author nguyenle
 * @since 5:30 PM Wed 4/23/2025
 */
public interface IpnRequestProcessor<T, V> {

	T processIpnRequest(V v);

}
