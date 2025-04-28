package com.myproject.springbootpayment.service.momo.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 10:54 AM Tue 4/22/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MomoQueryOrderStatusReq {

	@JsonProperty("partnerCode")
	private String partnerCode;

	@JsonProperty("requestId")
	private String requestId;

	@JsonProperty("orderId")
	private String orderId;

	@JsonProperty("signature")
	private String signature;

	@JsonProperty("lang")
	private String lang;
}
