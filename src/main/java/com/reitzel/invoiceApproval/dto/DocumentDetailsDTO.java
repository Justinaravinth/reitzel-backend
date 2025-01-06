package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDetailsDTO {
	
	@JsonProperty("Typ")
    private String Typ;

    @JsonProperty("No")
    private String No;

    @JsonProperty("Dt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private String Dt;

}
