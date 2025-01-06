package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShippingDetailsDTO {
	
	@JsonProperty("Gstin")
    private String gstin = "29AACCR4566P1ZL";

    @JsonProperty("LglNm")
    private String lglNm = "REITZEL INDIA PRIVATE LIMITED";

    @JsonProperty("TrdNm")
    private String trdNm = "REITZEL INDIA PRIVATE LIMITED";

    @JsonProperty("Addr1")
    private String addr1 = "SY NO. 98 AND 99, KIADB INDUSTRIAL AREA,ANCHEPALYA VILLAGE, KUNIGAL TALUK";

    @JsonProperty("Addr2")
    private String addr2 = "TUMAKURU";

    @JsonProperty("Loc")
    private String loc = "TUMAKURU";

    @JsonProperty("Pin")
    private int pin = 572126;

    @JsonProperty("Stcd")
    private String stcd = "29";

}
