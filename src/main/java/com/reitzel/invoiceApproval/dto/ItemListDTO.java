package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemListDTO {
	
	@JsonProperty("productName")
	private String productName;
	
	@JsonProperty("productDesc")
	private String productDesc;
	
	@JsonProperty("hsnCode")
	private Number hsnCode;
	
	@JsonProperty("quantity")
	private Number quantity;
	
	@JsonProperty("qtyUnit")
	private String qtyUnit;
	
	@JsonProperty("cgstRate")
    private double cgstRate;
	
	@JsonProperty("sgstRate")
    private double sgstRate;
	
	@JsonProperty("igstRate")
    private double igstRate;
	
	@JsonProperty("cessRate")
    private double cessRate;
	
	@JsonProperty("cessNonadvol")
    private double cessNonadvol;
	
	@JsonProperty("taxableAmount")
    private double taxableAmount;
}
