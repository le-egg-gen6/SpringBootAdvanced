package com.myproject.springbootzalopay.shared.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * @author nguyenle
 * @since 9:14 AM Mon 4/21/2025
 */
@Getter
public enum ZaloPaySubReturnCode {
	// Common Errors
	APPID_INVALID(-2),
	HMAC_INVALID(-53),
	DUPLICATE_APPS_TRANS_ID(-68),
	ILLEGAL_DATA_REQUEST(-401),
	ILLEGAL_APP_REQUEST(-402),
	ILLEGAL_SIGNATURE_REQUEST(-403),
	ILLEGAL_CLIENT_REQUEST(-405),
	LIMIT_REQUEST_REACH(-429),
	SYSTEM_ERROR(-500),

	// Payment Status Query
	TIME_INVALID(-54),
	ZPW_BALANCE_NOT_ENOUGH(-63),
	APPTRANSID_INVALID(-92),
	ORDER_NOT_EXIST(-101),

	// Refund Transaction
	MAC_INVALID(-3),
	REFUND_EXPIRE_TIME(-13),
	INVALID_MERCHANT_REFUNDID_FORMAT(-24),
	INVALID_MERCHANT_REFUNDID_DATE(-25),
	INVALID_MERCHANT_REFUNDID_APPID(-26),
	ORDER_NOT_FOUND(-101), // trùng mã với ORDER_NOT_EXIST
	DUPLICATE_REFUND(-23),

	// Refund Status Query
	REFUND_PENDING(-1),
	REFUND_TYPE_INVALID(-2),
	PMC_NOT_SUPPORT_REFUND(-4),
	TRANS_NOT_COMPLELE(-5),
	TRANS_FAIL(-6),
	TRANSTYPE_NOT_SUPPORT_REFUND(-9),
	TRANS_ALREADY_SUCCESS(-11),
	REQUEST_FORMAT_INVALID(-12),
	REFUND_AMOUNT_INVALID(-14),
	INSERT_REFUND_PARTIAL_HISTORY_FAIL(-15),
	INSERT_REFUND_LOG_AR_FAIL(-16),
	UPDATE_REFUND_LOG_AR_FAIL(-17),
	TRANS_STATUS_NOT_SUPPORT_REFUND(-18),
	INSERT_REFUND_TRANSLOG_FAIL(-19),
	UPDATE_TOTAL_REFUND_AMOUNT_FAIL(-20),
	REFUND_NOT_FOUND(-21),
	REFUND_STATUS_NOT_SUPPORT_REFUND(-22),
	CLIENTID_INVALID(-27),
	TRANS_INVALID(-28),
	NOT_FOUND_BANKSYSTEM(-29),
	REFUND_CACHE_AND_DB_INCONSISTENT(-30),
	REFUND_OVER_TIME(-31)
	;

	private static final Map<Integer, ZaloPaySubReturnCode> map = new HashMap<>();

	static {
		for (ZaloPaySubReturnCode code : ZaloPaySubReturnCode.values()) {
			map.put(code.getCode(), code);
		}
	}

	public static ZaloPaySubReturnCode fromValue(int value) {
		return map.get(value);
	}

	private int code;

	ZaloPaySubReturnCode(int code) {
		this.code = code;
	}
}

