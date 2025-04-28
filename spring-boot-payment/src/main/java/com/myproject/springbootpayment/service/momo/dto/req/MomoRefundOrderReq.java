package com.myproject.springbootpayment.service.momo.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 10:56 AM Tue 4/22/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MomoRefundOrderReq {

	@JsonProperty("partnerCode")
	private String partnerCode;

	@JsonProperty("orderId")
	private String orderId;

	@JsonProperty("requestId")
	private String requestId;

	@JsonProperty("amount")
	private long amount;

	@JsonProperty("transId")
	private long transId;

	@JsonProperty("lang")
	private String lang;

	@JsonProperty("description")
	private String description;

	@JsonProperty("signature")
	private String signature;
}
