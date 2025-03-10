package com.myproject.springbootvnpay.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 11:12 AM Mon 3/10/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InitPaymentRequest {

	@JsonProperty("requestId")
	private String requestId;

	@JsonProperty("ipAddress")
	private String ipAddress;

	@JsonProperty("userId")
	private Long userId;

	@JsonProperty("txnRef")
	private String txnRef;

	@JsonProperty("amount")
	private long amount;

}
