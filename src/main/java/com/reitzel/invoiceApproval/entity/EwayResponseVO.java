package com.reitzel.invoiceApproval.entity;

import java.time.LocalDateTime;

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
@Table(name = "ewayresponse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EwayResponseVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ewayresponsegen")
	@SequenceGenerator(name = "ewayresponsegen", sequenceName = "ewayresponseseq", initialValue = 1000000001, allocationSize = 1)
	private Long id;

	private String Ewbno;
	
	private String remarks;

	private String Ewbdate;

	private String Ewvalidtill;

	private String status;

	private String docid;
	
	private String irn;
	
	private String alert;
	
	private String type;

	private LocalDateTime createdOn= LocalDateTime.now();
}
