package com.reitzel.invoiceApproval.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ewayheader")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EwayHeadersVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ewayheadergen")
	@SequenceGenerator(name = "ewayheadergen", sequenceName = "ewayheaderseq", initialValue = 1000000001, allocationSize = 1)
	private Long id;
	
	private String clientId;
	private String clientSecret;
	private String gstin;
	private String userName;
	private String authtoken;
	private String Sek;
}
