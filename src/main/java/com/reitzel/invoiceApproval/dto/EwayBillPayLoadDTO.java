package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class EwayBillPayLoadDTO {

	@JsonProperty("Data")
    private String Data;
	
	@JsonProperty("action")
    private String action;
   
}