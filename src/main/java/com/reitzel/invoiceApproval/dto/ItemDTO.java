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
    private String Barcde ;

    @JsonProperty("Qty")
    private double Qty;

    @JsonProperty("FreeQty")
    private int FreeQty ;

    @JsonProperty("Unit")
    private String Unit;

    @JsonProperty("UnitPrice")
    private double UnitPrice;

    @JsonProperty("TotAmt")
    private double TotAmt;

    @JsonProperty("Discount")
    private double Discount;

    @JsonProperty("PreTaxVal")
    private double PreTaxVal;

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
    private double CesRt;

    @JsonProperty("CesAmt")
    private double CesAmt;

    @JsonProperty("CesNonAdvlAmt")
    private double CesNonAdvlAmt;

    @JsonProperty("StateCesRt")
    private double StateCesRt;

    @JsonProperty("StateCesAmt")
    private double StateCesAmt;

    @JsonProperty("StateCesNonAdvlAmt")
    private double StateCesNonAdvlAmt;

    @JsonProperty("OthChrg")
    private double OthChrg;

    @JsonProperty("TotItemVal")
    private double TotItemVal;

    @JsonProperty("BchDtls")
    private BchDtlsDTO BchDtls=null;

}
