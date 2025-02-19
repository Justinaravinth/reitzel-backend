package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EwayBillDTO {
	
	@JsonProperty("IRN")
	private String irn;
	
	@JsonProperty("TransDetails")
	private TransDetailsDTO transDetailsDTO;
	
	@JsonProperty("ExpShipDetails")
	private ExpShipDetailsDTO expShipDetails;
	
	@JsonProperty("DispatchDetails")
	private DispatchDetailsDTO dispatchDetails;

}
