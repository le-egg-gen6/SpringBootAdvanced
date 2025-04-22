package org.myproject.springbootmomo.dto.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 12:54 AM Tue 4/22/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefundOrderResp {

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

    @JsonProperty("resultCode")
    private int resultCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("responseTime")
    private long responseTime;
}
