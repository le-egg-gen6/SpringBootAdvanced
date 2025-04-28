package com.myproject.springbootpayment.shared.constant.vnpay;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nguyenle
 * @since 8:05 AM Thu 4/24/2025
 */
@Getter
public enum VNPayTransactionStatus {
    SUCCESS(0),
    PENDING(1),
    FAILED(2),
    REVERSED(4),
    REFUND_PROCESSING(5),
    REFUND_REQUESTED(6),
    FRAUD_SUSPECTED(7),
    REFUND_REJECTED(9),
    UNKNOWN(-1);

    private static final Map<Integer, VNPayTransactionStatus> map = new HashMap<>();

    static {
        for (VNPayTransactionStatus status : VNPayTransactionStatus.values()) {
            map.put(status.getCode(), status);
        }
    }

    public static VNPayTransactionStatus fromValue(int code) {
        return map.getOrDefault(code, UNKNOWN);
    }

    private int code;
    VNPayTransactionStatus(int code) {
        this.code = code;
    }

}