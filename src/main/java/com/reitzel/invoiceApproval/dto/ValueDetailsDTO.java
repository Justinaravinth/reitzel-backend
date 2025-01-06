package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValueDetailsDTO {
	
	@JsonProperty("AssVal")
    private double AssVal;

    @JsonProperty("IgstVal")
    private double IgstVal;

    @JsonProperty("CgstVal")
    private double CgstVal;

    @JsonProperty("SgstVal")
    private double SgstVal;

    @JsonProperty("CesVal")
    private double CesVal = 0;

    @JsonProperty("StCesVal")
    private double StCesVal = 0;

    @JsonProperty("Discount")
    private double Discount = 0;

    @JsonProperty("OthChrg")
    private double OthChrg;

    @JsonProperty("RndOffAmt")
    private double RndOffAmt = 0;

    @JsonProperty("TotInvVal")
    private double TotInvVal;

}
