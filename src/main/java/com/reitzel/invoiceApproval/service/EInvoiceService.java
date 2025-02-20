package com.reitzel.invoiceApproval.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reitzel.invoiceApproval.dto.EInvoiceDTO;
import com.reitzel.invoiceApproval.dto.EwayBillDTO;
import com.reitzel.invoiceApproval.entity.EInvoiceVO;

@Service
public interface EInvoiceService {

	List<EInvoiceVO> getEInvoiceByDocId(String docId);

	EInvoiceDTO getEInvoicePayloadByDocId(String docId);

	Map<String, Object> createEinvoice(List<String> docId) throws JsonProcessingException;

	String generateIRN(List<String> docid);

	// EWAYBILL

	EwayBillDTO getEWayBillByDocId(String docId);

	Map<String, Object> createEWayBill(List<String> docId) throws JsonProcessingException;

	EwayBillDTO getEWayBillByDocId();

}
