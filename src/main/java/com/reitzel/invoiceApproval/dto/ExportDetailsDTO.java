package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportDetailsDTO {
	
	@JsonProperty("ShipBNo")
    private String ShipBNo = "A-248";

    @JsonProperty("ShipBDt")
    private String ShipBDt = "01/08/2024";

    @JsonProperty("Port")
    private String Port = "INABG1";

    @JsonProperty("RefClm")
    private String RefClm = "N";

    @JsonProperty("ForCur")
    private String ForCur = "AED";

    @JsonProperty("CntCode")
    private String CntCode = "AE";

    @JsonProperty("ExpDuty")
    private double ExpDuty = 0;

}
