package org.myproject.springbootmomo.shared.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum MomoReturnCode {

    SUCCESS(0, "Transaction successful.", true),
    CONFIRMED(9000, "Transaction successfully confirmed.", false),
    NEED_USER_CONFIRM_AGAIN(8000, "Transaction pending user's re-confirmation.", false),
    PROCESSING(7000, "Transaction is being processed.", false),
    INITIATED(1000, "Transaction initialized, waiting for user confirmation.", false),

    ACCESS_DENIED(11, "Access denied.", false),
    UNSUPPORTED_API_VERSION(12, "Unsupported API version.", false),
    AUTH_FAILED(13, "Merchant authentication failed.", false),

    INVALID_FORMAT(20, "Malformed request.", false),
    INVALID_AMOUNT(21, "Invalid transaction amount.", false),

    DUPLICATE_REQUEST_ID(40, "Duplicate RequestId.", false),
    DUPLICATE_ORDER_ID(41, "Duplicate OrderId.", false),
    INVALID_ORDER_ID(42, "Invalid or not found OrderId.", false),
    TRANSACTION_CONFLICT(43, "Request denied due to processing conflict.", false),

    INSUFFICIENT_FUNDS(1001, "Payment failed due to insufficient user funds.", true),
    CARD_DECLINED(1002, "Payment declined by issuing bank.", true),
    TRANSACTION_CANCELLED(1003, "Transaction canceled.", true),
    EXCEED_LIMIT(1004, "Payment amount exceeds user limit.", true),
    EXPIRED_URL_OR_QR(1005, "URL or QR code expired.", true),
    USER_DECLINED(1006, "User declined payment.", true),
    USER_ACCOUNT_LOCKED(1007, "Payment declined due to locked user account.", true),
    PROMOTION_RESTRICTED(1026, "Payment restricted by promotion rules.", true),

    REFUND_ORIGINAL_NOT_FOUND(1080, "Refund declined – original transaction not found.", true),
    REFUND_ALREADY_DONE(1081, "Refund declined – already refunded or exceeds allowed amount.", true),

    LINK_ERROR(2001, "Transaction failed due to invalid linking information.", true),
    LINK_TEMP_LOCKED(2007, "Transaction failed – link temporarily locked.", true),
    LINK_USER_DECLINED(3001, "Linking failed – user declined confirmation.", true),
    LINK_RULES_VIOLATION(3002, "Linking denied – rules not met.", true),
    LINK_CANCEL_LIMIT_EXCEEDED(3003, "Unlinking denied – limit exceeded.", true),
    LINK_CANNOT_CANCEL_WITH_PENDING(3004, "Cannot unlink – pending transactions exist.", true),

    USER_NOT_VERIFIED(4001, "Transaction restricted – user not verified.", true),
    OTP_FAILED(4010, "OTP verification failed.", true),
    OTP_EXPIRED(4011, "OTP not sent or expired.", true),
    USER_NOT_LOGGED_IN(4100, "Transaction failed – user login unsuccessful.", true),
    VERIFY_3DS_FAILED(4015, "3DS verification failed.", true),

    SYSTEM_MAINTENANCE(10, "System under maintenance.", false),
    UNKNOWN_ERROR(99, "Unknown error.", true);

    private static final Map<Integer, MomoReturnCode> map = new HashMap<>();

    static {
        for (MomoReturnCode value : MomoReturnCode.values()) {
            map.put(value.getCode(), value);
        }
    }

    public static MomoReturnCode fromValue(int code) {
        return map.get(code);
    }

    private final int code;
    private final String description;
    private final boolean finalStatus;


    MomoReturnCode(int code, String description, boolean finalStatus) {
        this.code = code;
        this.description = description;
        this.finalStatus = finalStatus;
    }
}
