package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EInvoiceGetToketDTO {
	
	@JsonProperty("UserName")
	private String userName;
	@JsonProperty("Password")
	private String password;

}
