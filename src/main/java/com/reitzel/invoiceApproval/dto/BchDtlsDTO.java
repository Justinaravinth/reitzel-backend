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
    private String nm ;

    @JsonProperty("ExpDt")
    private String expDt ;

    @JsonProperty("WrDt")
    private String wrDt ;

}
