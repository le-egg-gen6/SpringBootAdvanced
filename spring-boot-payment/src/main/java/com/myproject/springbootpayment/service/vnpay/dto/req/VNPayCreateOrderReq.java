package com.myproject.springbootpayment.service.vnpay.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 3:18 PM Wed 4/23/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VNPayCreateOrderReq {

	private long amount;

	private String bankCode;

	private String currency;

	private String ipAddress;

	private String language;

	private String orderInfo;

	private String orderType;

	private String appTransactionId;

	private long createdTime;

}
