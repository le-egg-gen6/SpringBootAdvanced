package com.myproject.springbootpayment.shared.constant.vnpay;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nguyenle
 * @since 7:58 AM Thu 4/24/2025
 */
@Getter
public enum VNPayResponseCode {
    SUCCESS(0),
    INVALID_CONNECTION_ID(2),
    INVALID_FORMAT(3),
    TRANSACTION_NOT_FOUND(91),
    DUPLICATE_REQUEST(94),
    TRANSACTION_FAILED_REFUND_REJECTED(95),
    INVALID_CHECKSUM(97),
    OTHER_ERRORS(99),
    UNKNOWN(-1);

    private static final Map<Integer, VNPayResponseCode> map = new HashMap<>();

    static {
        for (VNPayResponseCode value : VNPayResponseCode.values()) {
            map.put(value.getCode(), value);
        }
    }

    public static VNPayResponseCode fromValue(int code) {
        return map.getOrDefault(code, UNKNOWN);
    }

    private int code;
    VNPayResponseCode(int code) {
        this.code = code;
    }
}
