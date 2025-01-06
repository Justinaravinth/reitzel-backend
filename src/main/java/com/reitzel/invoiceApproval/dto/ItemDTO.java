package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
	
	@JsonProperty("SlNo")
    private String SlNo;

    @JsonProperty("PrdDesc")
    private String PrdDesc;

    @JsonProperty("IsServc")
    private String IsServc;

    @JsonProperty("HsnCd")
    private String HsnCd;

    @JsonProperty("Barcde")
    private String Barcde ="12345";

    @JsonProperty("Qty")
    private int Qty;

    @JsonProperty("FreeQty")
    private int FreeQty = 0;

    @JsonProperty("Unit")
    private String Unit = "NOS";

    @JsonProperty("UnitPrice")
    private double UnitPrice;

    @JsonProperty("TotAmt")
    private double TotAmt;

    @JsonProperty("Discount")
    private double Discount = 0;

    @JsonProperty("PreTaxVal")
    private double PreTaxVal = 0;

    @JsonProperty("AssAmt")
    private double AssAmt;

    @JsonProperty("GstRt")
    private double GstRt;

    @JsonProperty("IgstAmt")
    private double IgstAmt;

    @JsonProperty("CgstAmt")
    private double CgstAmt;

    @JsonProperty("SgstAmt")
    private double SgstAmt;

    @JsonProperty("CesRt")
    private double CesRt = 0;

    @JsonProperty("CesAmt")
    private double CesAmt = 0;

    @JsonProperty("CesNonAdvlAmt")
    private double CesNonAdvlAmt = 0;

    @JsonProperty("StateCesRt")
    private double StateCesRt = 0;

    @JsonProperty("StateCesAmt")
    private double StateCesAmt = 0;

    @JsonProperty("StateCesNonAdvlAmt")
    private double StateCesNonAdvlAmt = 0;

    @JsonProperty("OthChrg")
    private double OthChrg = 0;

    @JsonProperty("TotItemVal")
    private double TotItemVal;

    @JsonProperty("BchDtls")
    private BchDtlsDTO BchDtls;

}
