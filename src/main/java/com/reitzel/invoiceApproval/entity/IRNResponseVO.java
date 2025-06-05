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
@Table(name="Irninvoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IRNResponseVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Irninvoicegen")
	@SequenceGenerator(name = "Irninvoicegen", sequenceName = "Irninvoiceseq", initialValue = 1000000001, allocationSize = 1)
	private Long id;
	
	private String AckNo;

	private String AckDt;

	private String Irn;

	@Lob
	@Column(columnDefinition = "CLOB")
	private String SignedInvoice;

	@Lob
	@Column(columnDefinition = "CLOB")
	private String SignedQRCode;

	private String status;
	
	private String docid;
	
//	private String cancel;
	
//	private String canceldate;
	
	private LocalDateTime createdOn= LocalDateTime.now();

}
