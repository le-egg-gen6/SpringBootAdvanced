package com.myproject.springbootpayment.service.vnpay.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 3:20 PM Wed 4/23/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VNPayRefundOrderReq {
    private String requestId;
    private String transactionType;
    private String appTransactionId;
    private long amount;
    private String orderInfo;
    private String vnpayTransactionId;
    private long createdTime;
    private String createdBy;
}
