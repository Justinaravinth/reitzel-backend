package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShippingDetailsDTO {
	
	@JsonProperty("Gstin")
    private String gstin;

    @JsonProperty("LglNm")
    private String lglNm ;

    @JsonProperty("TrdNm")
    private String trdNm ;

    @JsonProperty("Addr1")
    private String addr1;

    @JsonProperty("Addr2")
    private String addr2;

    @JsonProperty("Loc")
    private String loc;

    @JsonProperty("Pin")
    private int pin;

    @JsonProperty("Stcd")
    private String stcd;

}
