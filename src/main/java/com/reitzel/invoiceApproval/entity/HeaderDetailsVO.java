package com.reitzel.invoiceApproval.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="einvoiceheader")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeaderDetailsVO {
	
	@Id	
	@Column(name="einvoiceheaderid")
	private Long id;
	private String clientId;
	private String clientSecret;
	private String gstin;
	private String userName;
	private String authtoken;
	private String sek;
	private String tokenExpiry;
	private String pwd;
	

}
