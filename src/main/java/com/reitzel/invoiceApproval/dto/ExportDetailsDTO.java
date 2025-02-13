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
    private String ShipBNo = null;

    @JsonProperty("ShipBDt")
    private String ShipBDt = null;

    @JsonProperty("Port")
    private String Port = null;

    @JsonProperty("RefClm")
    private String RefClm = null;

    @JsonProperty("ForCur")
    private String ForCur = null;

    @JsonProperty("CntCode")
    private String CntCode = "NA";

    @JsonProperty("ExpDuty")
    private double ExpDuty = 0;

}
