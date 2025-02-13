package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailsDTO {
	
	@JsonProperty("TaxSch")
    private String TaxSch;

    @JsonProperty("SupTyp")
    private String SupTyp;

    @JsonProperty("IgstOnIntra")
    private String IgstOnIntra;

    @JsonProperty("RegRev")
    private String RegRev ;

    @JsonProperty("EcmGstin")
    private String EcmGstin = null;

}
