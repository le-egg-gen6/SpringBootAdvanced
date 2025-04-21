package com.myproject.springbootzalopay.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author nguyenle
 * @since 12:39 AM Mon 4/21/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderReq {

    private String userId;

    private String transactionId;

    private long amount;

    private List<String> item;

    private String bankCode;

    private String deviceInfo;

    private String title;

    private String currency;

    private String phone;

    private String email;

    private String address;

}
