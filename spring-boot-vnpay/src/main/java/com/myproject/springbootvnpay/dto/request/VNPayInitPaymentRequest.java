package com.myproject.springbootvnpay.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myproject.springbootvnpay.shared.constant.VNPAYCoreConstant;
import com.myproject.springbootvnpay.shared.constant.VNPAYCommandConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 11:19 AM Mon 3/10/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VNPayInitPaymentRequest {

	@JsonProperty("vnp_Version")
	public static final String VERSION = VNPAYCoreConstant.VNPAY_SERVICE_VERSION;

	@JsonProperty("vnp_Command")
	public static final String COMMAND = VNPAYCommandConstant.QUERY_PAYMENT;

	@JsonProperty("vnp_RequestId")
	private String requestId;

	@JsonProperty("vnp_TmnCode")
	private String tmnCode;

	@JsonProperty("vnp_TxnRef")
	private String txnRef;

	@JsonProperty("vnp_CreateDate")
	private String createdDate;

	@JsonProperty("vnp_IpAddr")
	private String ipAddress;

	@JsonProperty("vnp_OrderInfo")
	private String orderInfo;

	@JsonProperty("vnp_SecureHash")
	private String secureHash;
}
