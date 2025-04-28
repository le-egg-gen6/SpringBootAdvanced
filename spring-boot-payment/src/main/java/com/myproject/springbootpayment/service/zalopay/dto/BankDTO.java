package com.myproject.springbootpayment.service.zalopay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 1:57 PM Fri 4/18/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankDTO {
	@JsonProperty(value = "bankcode")
	private String bankCode;
	@JsonProperty(value = "name")
	private String name;
	@JsonProperty(value = "displayorder")
	private int displayOrder;
	@JsonProperty(value = "pmcid")
	private int pmcid;
	@JsonProperty(value = "minamount")
	private long minAmount;
	@JsonProperty(value = "maxamount")
	private long maxAmount;
}
