package com.myproject.springbootpayment.service.vnpay.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 3:19 PM Wed 4/23/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VNPayQueryOrderStatusReq {
    private String requestId;
    private String appTransactionId;
    private String orderInfo;
    private String vnpayTransactionId;
    private long createdTime;
}
