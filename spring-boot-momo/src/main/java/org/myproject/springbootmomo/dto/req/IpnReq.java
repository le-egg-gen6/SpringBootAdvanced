package org.myproject.springbootmomo.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 12:55 AM Tue 4/22/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IpnReq {

    @JsonProperty("partnerCode")
    private String partnerCode;

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("amount")
    private long amount;

    @JsonProperty("orderInfo")
    private String orderInfo;

    @JsonProperty("orderType")
    private String orderType;

    @JsonProperty("transId")
    private long transId;

    @JsonProperty("resultCode")
    private int resultCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("payType")
    private String payType;

    @JsonProperty("responseTime")
    private long responseTime;

    @JsonProperty("extraData")
    private String extraData;

    @JsonProperty("signature")
    private String signature;
}
