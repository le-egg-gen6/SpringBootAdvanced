package com.myproject.springbootzalopay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nguyenle
 * @since 3:21 PM Fri 4/18/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListBackDTO {
	@JsonProperty(value = "returncode")
	private int returnCode;
	@JsonProperty(value = "returnmessage")
	private String returnMessage;
	@JsonProperty(value = "banks")
	private Map<Integer, List<BankDTO>> banks;
}
