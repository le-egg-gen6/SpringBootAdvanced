package org.myproject.springbootmomo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 12:51 AM Tue 4/22/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefundTransaction {
    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("amount")
    private long amount;

    @JsonProperty("createdTime")
    private long createdTime;

    @JsonProperty("resultCode")
    private int resultCode;

    @JsonProperty("transId")
    private long transId;
}
