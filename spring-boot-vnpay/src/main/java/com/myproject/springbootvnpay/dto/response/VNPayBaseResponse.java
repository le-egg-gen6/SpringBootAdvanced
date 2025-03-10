package com.myproject.springbootvnpay.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 11:20 AM Mon 3/10/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VNPayBaseResponse {
	@JsonProperty("RspCode")
	private String responseCode;

	@JsonProperty("Message")
	private String message;
}
