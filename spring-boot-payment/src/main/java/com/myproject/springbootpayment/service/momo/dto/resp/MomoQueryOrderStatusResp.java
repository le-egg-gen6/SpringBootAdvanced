package com.myproject.springbootpayment.service.momo.dto.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myproject.springbootpayment.service.momo.dto.RefundTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author nguyenle
 * @since 12:49 AM Tue 4/22/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MomoQueryOrderStatusResp {

    @JsonProperty("partnerCode")
    private String partnerCode;

    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("extraData")
    private String extraData;

    @JsonProperty("amount")
    private long amount;

    @JsonProperty("transId")
    private long transId;

    @JsonProperty("payType")
    private String payType;

    @JsonProperty("resultCode")
    private int resultCode;

    @JsonProperty("refundTrans")
    private List<RefundTransaction> refundTrans;

    @JsonProperty("message")
    private String message;

    @JsonProperty("responseTime")
    private long responseTime;

}