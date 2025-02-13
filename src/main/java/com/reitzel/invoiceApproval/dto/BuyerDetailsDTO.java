package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyerDetailsDTO {
	
	@JsonProperty("Gstin")
    private String Gstin;

    @JsonProperty("LglNm")
    private String LglNm;

    @JsonProperty("TrdNm")
    private String TrdNm;

    @JsonProperty("Pos")
    private String Pos;

    @JsonProperty("Addr1")
    private String Addr1;

    @JsonProperty("Addr2")
    private String Addr2;

    @JsonProperty("Loc")
    private String Loc;

    @JsonProperty("Pin")
    private int Pin;

    @JsonProperty("Stcd")
    private String Stcd;

    @JsonProperty("Ph")
    private String Ph;

    @JsonProperty("Em")
    private String Em ;

}
