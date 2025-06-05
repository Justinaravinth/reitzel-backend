package com.reitzel.invoiceApproval.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="EWAYBILL_REQUEST")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EwayBillDirectVO {
	
	@Id
	private Long ewaybillRequestid;
	
	private String docno;
	
	private String genewaybill;
	
	private String ewaystatus;
	
	private String eapicall;
	
	private String ewbno;
	
	private String ewbdate;
	
	private String ewbvalidtill;
	

}
