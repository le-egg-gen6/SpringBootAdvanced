package org.myproject.springbootmomo.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 12:46 AM Tue 4/22/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderReq {

	@JsonProperty("partnerCode")
	private String partnerCode;

	@JsonProperty("partnerName")
	private String partnerName;

	@JsonProperty("storeId")
	private String storeId;

	@JsonProperty("requestId")
	private String requestId;

	@JsonProperty("amount")
	private long amount;

	@JsonProperty("orderId")
	private String orderId;

	@JsonProperty("orderInfo")
	private String orderInfo;

	@JsonProperty("autoCapture")
	private boolean autoCapture;

	@JsonProperty("redirectUrl")
	private String redirectUrl;

	@JsonProperty("ipnUrl")
	private String ipnUrl;

	@JsonProperty("requestType")
	private String requestType;

	@JsonProperty("extraData")
	private String extraData;

	@JsonProperty("lang")
	private String lang;

	@JsonProperty("signature")
	private String signature;
}

