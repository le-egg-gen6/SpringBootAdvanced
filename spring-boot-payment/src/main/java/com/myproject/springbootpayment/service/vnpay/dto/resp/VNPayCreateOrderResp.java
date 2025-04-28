package com.myproject.springbootpayment.service.vnpay.dto.resp;

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
public class VNPayCreateOrderResp {

	private String paymentUrl;

}
