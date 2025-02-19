package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransDetailsDTO {

	@JsonProperty("Distance")
	private long distance;

	@JsonProperty("TransModel")
	private String transModel;

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
	
	@JsonProperty("vehType")
	private String VehType;

}
