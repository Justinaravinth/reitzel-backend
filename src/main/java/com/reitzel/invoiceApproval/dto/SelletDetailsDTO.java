package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelletDetailsDTO {
	
	@JsonProperty("Gstin")
	private String Gstin="29AACCR4566P1ZL";
	@JsonProperty("LglNm")
    private String LglNm="REITZEL INDIA PRIVATE LIMITED";
	@JsonProperty("TrdNm")
    private String TrdNm="REITZEL INDIA PRIVATE LIMITED";
	@JsonProperty("Addr1")
    private String Addr1="SY NO. 98 AND 99, KIADB INDUSTRIAL AREA,ANCHEPALYA VILLAGE, KUNIGAL TALUK";
	@JsonProperty("Addr2")
    private String Addr2="TUMAKURU";
	@JsonProperty("Loc")
    private String Loc="TUMAKURU";
	@JsonProperty("Pin")
    private int Pin=572126;
	@JsonProperty("Stcd")
    private String Stcd="29";
	@JsonProperty("Ph")
    private String Ph="9876543210";
	@JsonProperty("Em")
    private String Em="abc@gmail.com";

}
