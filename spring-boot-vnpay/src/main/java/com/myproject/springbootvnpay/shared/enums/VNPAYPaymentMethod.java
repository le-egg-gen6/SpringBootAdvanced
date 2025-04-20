package com.myproject.springbootvnpay.shared.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * @author nguyenle
 * @since 2:40 PM Fri 4/18/2025
 */
@Getter
public enum VNPAYPaymentMethod {
	QR("QR"),
	DOMESTIC_BANK("VNPAYBANK"),
	INTERNATIONAL_CARD("INTCARD")
	;

	public static final Map<String, VNPAYPaymentMethod> paymentMethodMap = new HashMap<String, VNPAYPaymentMethod>();

	static {
		for (VNPAYPaymentMethod paymentMethod : VNPAYPaymentMethod.values()) {
			paymentMethodMap.put(paymentMethod.name(), paymentMethod);
		}
	}

	public static VNPAYPaymentMethod fromValue(String value) {
		return paymentMethodMap.get(value);
	}

	private String value;
	VNPAYPaymentMethod(String value) {
		this.value = value;
	}
}
