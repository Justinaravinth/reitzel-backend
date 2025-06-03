package com.reitzel.invoiceApproval.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ewaybillresponse")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EwayBillResponseVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ewaybillresponsegen")
	@SequenceGenerator(name = "ewaybillresponsegen", sequenceName = "ewaybillresponseseeq", initialValue = 1000000001, allocationSize = 1)
	private Long id;
	
	private LocalDateTime createdOn= LocalDateTime.now();
	private String docid;	
	private String iserror;
	private String message;
	private String errordetails;
	
	@Lob
	@Column(columnDefinition = "CLOB")
	private String response;
	
	
}
