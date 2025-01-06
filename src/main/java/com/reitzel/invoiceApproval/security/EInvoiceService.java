package com.reitzel.invoiceApproval.security;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reitzel.invoiceApproval.dto.EInvoiceDTO;

@Service
public interface EInvoiceService {
	
	
	List<EInvoiceDTO> getEInvoiceByDocId(String docId);

}
