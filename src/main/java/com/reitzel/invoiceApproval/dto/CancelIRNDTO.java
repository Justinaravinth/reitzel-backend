package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelIRNDTO {
	
	@JsonProperty("Irn")
	private String irn;
	
	@JsonProperty("CnlRsn")
	private String CnlRsn;

	@JsonProperty("CnlRem")
	private String CnlRem;

}
