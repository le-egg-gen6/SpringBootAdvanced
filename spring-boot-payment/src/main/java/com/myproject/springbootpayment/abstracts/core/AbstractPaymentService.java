package com.myproject.springbootpayment.abstracts.core;

import com.myproject.springbootpayment.service.HttpRestClientService;
import com.myproject.springbootpayment.shared.constant.StringConstant;
import lombok.RequiredArgsConstructor;

/**
 * @author nguyenle
 * @since 3:27 PM Tue 4/22/2025
 */
@RequiredArgsConstructor
public abstract class AbstractPaymentService {

	protected AbstractCryptoService cryptoService;
	protected HttpRestClientService httpRestClientService;

	protected String buildUrl(String baseUrl, String endpoint) {
		return String.format("%s%s%s", baseUrl, StringConstant.SLASH, endpoint);
	}
}
