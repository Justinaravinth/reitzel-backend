package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DispatchDetailsDTO {
	
	@JsonProperty("Nm")
    private String nm ;

    @JsonProperty("Addr1")
    private String addr1 ;

    @JsonProperty("Addr2")
    private String addr2 ;

    @JsonProperty("Loc")
    private String loc ;

    @JsonProperty("Pin")
    private int pin ;

    @JsonProperty("Stcd")
    private String stcd ;

}
