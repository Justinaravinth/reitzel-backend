package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EWayBillDetailsDTO {
	
	@JsonProperty("TransId")
    private String transId = null;

    @JsonProperty("TransName")
    private String transName = null;

    @JsonProperty("Distance")
    private int distance = 100;

    @JsonProperty("TransDocNo")
    private String transDocNo = "DOC01";

    @JsonProperty("TransDocDt")
    private String transDocDt = "18/08/2020";

    @JsonProperty("VehNo")
    private String vehNo = "ka123456";

    @JsonProperty("VehType")
    private String vehType = "R";

    @JsonProperty("TransMode")
    private String transMode = "1";


}
