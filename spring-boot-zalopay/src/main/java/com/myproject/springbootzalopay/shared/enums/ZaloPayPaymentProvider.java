package com.myproject.springbootzalopay.shared.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * @author nguyenle
 * @since 1:51 PM Fri 4/18/2025
 */
@Getter
public enum ZaloPayPaymentProvider {

	VISA_MASTERCARD_JCB(36),
	BANK(37),
	ZALO_PAY(38),
	ATM(39),
	VISA_MASTER_DEBIT(40)
	;

	private static final Map<Integer, ZaloPayPaymentProvider> paymentProviderMap = new HashMap<Integer, ZaloPayPaymentProvider>();

	static {
		for (ZaloPayPaymentProvider zaloPayPaymentProvider : ZaloPayPaymentProvider.values()) {
			paymentProviderMap.put(zaloPayPaymentProvider.getValue(), zaloPayPaymentProvider);
		}
	}

	public static ZaloPayPaymentProvider fromValue(int value) {
		return paymentProviderMap.get(value);
	}

	private int value;

	ZaloPayPaymentProvider(int value) {
		this.value = value;
	}

}
