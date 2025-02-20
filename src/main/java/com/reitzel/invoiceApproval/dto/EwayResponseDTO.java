package com.reitzel.invoiceApproval.dto;

import lombok.Data;

@Data
public class EwayResponseDTO {

	private Long EwbNo;

	private String EwbDt;

	private String EwValidTill;

	private String status;
}
