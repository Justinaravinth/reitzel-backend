package com.reitzel.invoiceApproval.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reitzel.invoiceApproval.dto.EInvoiceDTO;
import com.reitzel.invoiceApproval.entity.EInvoiceVO;

@Service
public interface EInvoiceService {
	
	
	List<EInvoiceVO> getEInvoiceByDocId(String docId);
	
	EInvoiceDTO getEInvoicePayloadByDocId(String docId);

}
