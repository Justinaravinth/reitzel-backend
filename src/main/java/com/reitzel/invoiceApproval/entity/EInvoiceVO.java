package com.reitzel.invoiceApproval.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="einvoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EInvoiceVO {
	
	@Id
	@Column(name="einvoiceid")
	private Long id;
	
	private String docid;
	
	private LocalDate docdate;
	
	private String suptype;
	
	private String documenttype;
	
	private String buyergstin;
	
	private String buyerlegalname;
	
	private String buyertradename;
	
	private String buyerpos;
	
	private String buyeradd1;
	
	private String add2;
	
	private String buyerlocation;
	
	private String buyerpincode;
	
	private String buyerstcd;
	
	private double tottaxablevalue;
	
	private double vigstamt;
	
	private double vcgstamt;
	
	private double vsgstamt;
	
	private double vothercharges;
	
	private double totinvvalue;
	
	private int slno;
	
	private String productiondesc;
	
	private String isservice;
	
	private String hsncode;
	
	private int quantity;
	
	private double unitprice;
	
	private double grossamount;
	
	private double taxablevalue;
	
	private double gstrate;
	
	private double igstamt;
	
	private double sgstamt;
	
	private double cgstamt;
	
	private double itemtotal;
	
	private String ackno;
	
	private String ackdate;
	
	private String irn;
	
	private String geneinvoice;
	
	private String genewaybill;
	
	private String apicall;
	
	private String eapicall;
	
	private String irnstatus;
	
	private String ewaystatus;
	
	private String ewbno;

	private String ewbdate;

	private String ewbvalidtill;
	
//	private String canceldate;
	
	
	
	@Lob
	@Column(columnDefinition = "CLOB")
	private String signedqrcode;



	
	

}
