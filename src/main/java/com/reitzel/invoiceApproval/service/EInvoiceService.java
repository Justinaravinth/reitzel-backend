package com.reitzel.invoiceApproval.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reitzel.invoiceApproval.dto.CancelIRNDTO;
import com.reitzel.invoiceApproval.dto.EInvoiceDTO;
import com.reitzel.invoiceApproval.dto.EInvoiceGetToketDTO;
import com.reitzel.invoiceApproval.dto.EWayGetToketDTO;
import com.reitzel.invoiceApproval.dto.EwayBillDTO;
import com.reitzel.invoiceApproval.dto.EwayBillNonIRNDTO;
import com.reitzel.invoiceApproval.entity.EInvoiceVO;

@Service
public interface EInvoiceService {

	
	List<EInvoiceVO> getEInvoiceByDocId(String docId);

	EInvoiceDTO getEInvoicePayloadByDocId(String docId);

	Map<String, Object> createEinvoice(List<String> docId) throws JsonProcessingException;

	String generateIRN(List<String> docid);

	Map<String, Object> createEWayBill(List<String> irn) throws JsonProcessingException;

	EwayBillDTO getEWayBillByDocIdnew(String docIds);
	
//	Map<String, Object> generateToken(EInvoiceGetToketDTO eInvoiceGetToketDTO) throws JsonProcessingException, Exception;

	Map<String, Object> generateToken(List<EInvoiceGetToketDTO> eInvoiceGetToketDTO1) throws Exception;

	EwayBillNonIRNDTO generateEwayBillByNonIRN(String docIds);

	Map<String, Object> createEWayBillNonIRN(List<String> docId) throws JsonProcessingException;

	CancelIRNDTO cancelIRN(String docIds);

	Map<String, Object> cancelIRNInvoice(List<String> docIds) throws JsonProcessingException;

	

}
