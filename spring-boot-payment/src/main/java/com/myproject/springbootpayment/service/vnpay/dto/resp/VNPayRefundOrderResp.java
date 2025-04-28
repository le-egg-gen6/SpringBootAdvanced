package com.myproject.springbootpayment.service.vnpay.dto.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 3:20 PM Wed 4/23/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VNPayRefundOrderResp {
    @JsonProperty("vnp_ResponseId")
    private String vnPayResponseId;

    @JsonProperty("vnp_Command")
    private String command;

    @JsonProperty("vnp_TmnCode")
    private String tmnCode;

    @JsonProperty("vnp_TxnRef")
    private String appTransactionId;

    @JsonProperty("amount")
    private long amount;

    @JsonProperty("vnp_OrderInfo")
    private String orderInfo;

    @JsonProperty("vnp_ResponseCode")
    private int responseCode;

    @JsonProperty("vnp_Message")
    private String message;

    @JsonProperty("vnp_BankCode")
    private String bankCode;

    @JsonProperty("vnp_PayDate")
    private String payDate;

    @JsonProperty("vnp_TransactionNo")
    private String vnpayTransactionId;

    @JsonProperty("vnp_TransactionType")
    private String transactionType;

    @JsonProperty("vnp_TransactionStatus")
    private int transactionStatus;

    @JsonProperty("vnp_SecureHash")
    private String secureHash;
}
