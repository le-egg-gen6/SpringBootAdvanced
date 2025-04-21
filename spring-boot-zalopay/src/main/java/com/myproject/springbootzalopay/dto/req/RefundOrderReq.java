package com.myproject.springbootzalopay.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 12:45 AM Mon 4/21/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefundOrderReq {

    private String refundId;

    private long amount;

    private String zaloPayTransactionId;

    private long refundFeeAmount;

    private String description;

}
