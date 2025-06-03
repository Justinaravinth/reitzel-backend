package com.reitzel.invoiceApproval.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EwayBillNonIRNDTO {
	
	@JsonProperty("supplyType")
	private String supplyType;
	

	@JsonProperty("subSupplyType")
	private String subSupplyType;

	@JsonProperty("subSupplyDesc")
	private String subSupplyDesc;

	@JsonProperty("docType")
	private String docType;
	
	@JsonProperty("docNo")
	private String docNo;

	 @JsonProperty("docDate")
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private String docDate;
	
	@JsonProperty("fromGstin")
	private String fromGstin;
	
	@JsonProperty("fromTrdName")
	private String fromTrdName;
	
	@JsonProperty("fromAddr1")
	private String fromAddr1;
	
	@JsonProperty("fromAddr2")
	private String fromAddr2;
	
	@JsonProperty("fromPlace")
	private String fromPlace;
	
	@JsonProperty("fromPincode")
	private int fromPincode;
	
	@JsonProperty("actFromStateCode")
	private int actFromStateCode;
	
	@JsonProperty("fromStateCode")
	private int fromStateCode;
	
	@JsonProperty("toGstin")
	private String toGstin;
	
	@JsonProperty("toTrdName")
	private String toTrdName;
	
	@JsonProperty("toAddr1")
	private String toAddr1;
	
	@JsonProperty("toAddr2")
	private String toAddr2;
	
	@JsonProperty("toPlace")
	private String toPlace;
	
	@JsonProperty("toPincode")
	private int toPincode;
	
	@JsonProperty("actToStateCode")
	private int actToStateCode;
	
	@JsonProperty("toStateCode")
	private int toStateCode;
	
	@JsonProperty("transactionType")
	private int transactionType;
	
	@JsonProperty("otherValue")
	private String otherValue;
	
	@JsonProperty("totalValue")
    private double totalValue;
	
	@JsonProperty("cgstValue")
    private double cgstValue;
	
	@JsonProperty("sgstValue")
    private double sgstValue;
	
	@JsonProperty("igstValue")
    private double igstValue;
	
	@JsonProperty("cessValue")
    private double cessValue;
	
	@JsonProperty("cessNonAdvolValue")
    private double cessNonAdvolValue;
	
	@JsonProperty("totInvValue")
    private double totInvValue;
	
	@JsonProperty("transporterId")
	private String transporterId;
	
	@JsonProperty("transporterName")
	private String transporterName;
	
	@JsonProperty("transDocNo")
	private String transDocNo;
	
	@JsonProperty("transMode")
	private String transMode;
	
	@JsonProperty("transDistance")
	private String transDistance;
	
	@JsonProperty("transDocDate")
	private String transDocDate;
	
	@JsonProperty("vehicleNo")
	private String vehicleNo;
	
	@JsonProperty("vehicleType")
	private String vehicleType;

	@JsonProperty("itemList")
	private List<ItemListDTO> itemList;
	

}
