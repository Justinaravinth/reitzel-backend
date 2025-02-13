package com.reitzel.invoiceApproval.dto;

import javax.persistence.Column;
import javax.persistence.Lob;

import lombok.Data;

@Data
public class IRNResponseDTO {
	
	private  String AckNo;
	
	private  String AckDt;
	
	private String Irn;
	
	@Lob
	@Column(columnDefinition = "CLOB")
	private String SignedInvoice;
	
	@Lob
	@Column(columnDefinition = "CLOB")
	private String SignedQRCode;
	
	private String status;

}
