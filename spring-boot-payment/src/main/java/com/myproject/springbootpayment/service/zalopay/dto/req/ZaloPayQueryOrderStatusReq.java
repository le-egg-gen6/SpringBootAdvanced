package com.myproject.springbootpayment.service.zalopay.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 5:23 PM Tue 4/22/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZaloPayQueryOrderStatusReq {
	private String appTransactionId;
}
