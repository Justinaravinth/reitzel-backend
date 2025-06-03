package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EwayBillDTO {
	
	@JsonProperty("Irn")
	private String irn;
	
	@JsonProperty("Distance")
	private long distance;

	@JsonProperty("TransMode")
	private String transMode;

	@JsonProperty("TransId")
	private String transId;

	@JsonProperty("TransName")
	private String transName;

	@JsonProperty("TransDocDt")
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private String transDocDt;
	
	@JsonProperty("TransDocNo")
	private String transDocNo;
	
	@JsonProperty("VehNo")
	private String vehNo;
	
	@JsonProperty("VehType")
	private String VehType;
	
	
	@JsonProperty("ExpShipDtls")
	private ExpShipDetailsDTO expShipDetails;
	
	@JsonProperty("DispDtls")
	private DispatchDetailsDTO dispatchDetails;

}
