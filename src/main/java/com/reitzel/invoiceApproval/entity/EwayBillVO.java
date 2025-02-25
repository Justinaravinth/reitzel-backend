package com.reitzel.invoiceApproval.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ewaybill")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EwayBillVO {

	@Id
	@Column(name="ewaybillid")
	private Long id;
	
	private String ewapicall;
	
	private Long ewbno;

	private String ewbdt;
	
	private String ewvalidtill;
	
	private String status;
	
	private String irn;
	
}
