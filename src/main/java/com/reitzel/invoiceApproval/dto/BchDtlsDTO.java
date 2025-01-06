package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BchDtlsDTO {
	
	@JsonProperty("Nm")
    private String nm = "123456";

    @JsonProperty("ExpDt")
    private String expDt = "01/08/2024";

    @JsonProperty("WrDt")
    private String wrDt = "01/09/2024";

}
