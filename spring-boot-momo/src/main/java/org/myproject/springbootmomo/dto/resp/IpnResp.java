package org.myproject.springbootmomo.dto.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 12:57 AM Tue 4/22/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IpnResp {

    @JsonProperty("partnerCode")
    private String partnerCode;

    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("resultCode")
    private int resultCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("responseTime")
    private long responseTime;

    @JsonProperty("extraData")
    private String extraData;

    @JsonProperty("signature")
    private String signature;
}
