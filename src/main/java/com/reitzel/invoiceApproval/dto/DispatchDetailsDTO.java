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
    private String nm = "ABC company pvt ltd";

    @JsonProperty("Addr1")
    private String addr1 = "7th block, kuvempu layout";

    @JsonProperty("Addr2")
    private String addr2 = "kuvempu layout";

    @JsonProperty("Loc")
    private String loc = "Banagalore";

    @JsonProperty("Pin")
    private int pin = 562160;

    @JsonProperty("Stcd")
    private String stcd = "29";

}
