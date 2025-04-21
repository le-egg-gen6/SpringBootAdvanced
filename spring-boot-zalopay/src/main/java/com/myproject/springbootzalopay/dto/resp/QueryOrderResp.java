package com.myproject.springbootzalopay.dto.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 12:24 AM Mon 4/21/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryOrderResp {

    @JsonProperty(value = "return_code")
    private int returnCode;

    @JsonProperty(value = "return_message")
    private String returnMessage;

    @JsonProperty(value = "sub_return_code")
    private String subReturnCode;

    @JsonProperty(value = "sub_return_message")
    private String subReturnMessage;

    @JsonProperty(value = "is_processing")
    private boolean isProcessing;

    @JsonProperty(value = "amount")
    private long amount;

    @JsonProperty(value = "discount_amount")
    private long discountAmount;

    @JsonProperty(value = "zp_trans_id")
    private String zaloPayTransactionId;

}
