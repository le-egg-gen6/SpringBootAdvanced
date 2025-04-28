package com.myproject.springbootpayment.service.zalopay;

import com.myproject.springbootpayment.abstracts.core.AbstractCryptoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author nguyenle
 * @since 5:24 PM Tue 4/22/2025
 */
@Service
public class ZaloPayCryptoService extends AbstractCryptoService {

	@Value("${payment.zalopay.key1}")
	private String key1;

	@Value("${payment.zalopay.key2}")
	private String key2;

	@PostConstruct
	@Override
	public void initialize() {
		initMac(key1, key2);
	}

}
