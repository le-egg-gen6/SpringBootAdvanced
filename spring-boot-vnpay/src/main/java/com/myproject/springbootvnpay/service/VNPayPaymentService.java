package com.myproject.springbootvnpay.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author nguyenle
 * @since 11:26 AM Mon 3/10/2025
 */
@Service
@RequiredArgsConstructor
public class VNPayPaymentService {

	private final VNPayCryptoService cryptoService;

	@Value("${payment.vnpay.init-payment-url}")
	private String initPaymentPrefixUrl;

	@Value("${payment.vnpay.return-url}")
	private String returnUrlFormat;

	@Value("${payment.vnpay.timeout}")
	private Integer paymentTimeout;

}
