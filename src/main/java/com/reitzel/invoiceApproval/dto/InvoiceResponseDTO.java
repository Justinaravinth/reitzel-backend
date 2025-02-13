package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDTO {
	
	private int Status;
    private String ErrorDetails;
    private byte[] Data;
    

}
