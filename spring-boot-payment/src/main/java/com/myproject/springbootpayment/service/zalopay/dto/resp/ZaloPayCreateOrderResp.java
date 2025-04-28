package com.myproject.springbootpayment.service.zalopay.dto.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 12:19 AM Mon 4/21/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZaloPayCreateOrderResp {

    @JsonProperty(value = "return_code")
    private int returnCode;

    @JsonProperty(value = "return_message")
    private String returnMessage;

    @JsonProperty(value = "sub_return_code")
    private int subReturnCode;

    @JsonProperty(value = "sub_return_message")
    private String subReturnMessage;

    @JsonProperty(value = "order_url")
    private String orderUrl;

    @JsonProperty(value = "zp_trans_token")
    private String zaloPayTransactionToken;

    @JsonProperty(value = "order_token")
    private String orderToken;

    @JsonProperty(value = "qr_code")
    private String qrCode;

}
