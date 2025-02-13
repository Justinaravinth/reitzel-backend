package com.reitzel.invoiceApproval.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EInvoiceDTO {
	
	@JsonProperty("Version")
    private String Version = "1.1";

    @JsonProperty("TranDtls")
    private TransactionDetailsDTO TranDtls;

    @JsonProperty("DocDtls")
    private DocumentDetailsDTO DocDtls;

    @JsonProperty("SellerDtls")
    private SelletDetailsDTO SellerDtls;

    @JsonProperty("BuyerDtls")
    private BuyerDetailsDTO BuyerDtls;

    @JsonProperty("DispDtls")
    private DispatchDetailsDTO DispDtls=null;

    @JsonProperty("ShipDtls")
    private ShippingDetailsDTO ShipDtls=null;
    
    @JsonProperty("ItemList")
    private List<ItemDTO> ItemList;

    @JsonProperty("ValDtls")
    private ValueDetailsDTO ValDtls;

    @JsonProperty("ExpDtls")
    private ExportDetailsDTO ExpDtls;

    @JsonProperty("EwbDtls")
    private EWayBillDetailsDTO EwbDtls=null;

    

}
