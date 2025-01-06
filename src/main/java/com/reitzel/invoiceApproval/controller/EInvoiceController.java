package com.reitzel.invoiceApproval.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reitzel.invoiceApproval.common.CommonConstant;
import com.reitzel.invoiceApproval.common.UserConstants;
import com.reitzel.invoiceApproval.dto.EInvoiceDTO;
import com.reitzel.invoiceApproval.dto.ResponseDTO;
import com.reitzel.invoiceApproval.entity.EInvoiceVO;
import com.reitzel.invoiceApproval.service.EInvoiceService;

@RestController
public class EInvoiceController extends BaseController  {
	public static final Logger LOGGER = LoggerFactory.getLogger(EInvoiceController.class);
	
	@Autowired
	EInvoiceService eInvoiceService;
	
	@GetMapping("/getAllInvoice")
	public ResponseEntity<ResponseDTO> getAllInvoice(@RequestParam String docid) {
		String methodName = "getAllInvoice()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<EInvoiceVO> invoiceVO= new ArrayList<>();
		try {
			invoiceVO = eInvoiceService.getEInvoiceByDocId(docid);

		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Approved Approval2 Details  found Successfullly");
			responseObjectsMap.put("invoiceVO", invoiceVO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Approved Approval2 Details information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getEInvoiceByDocId")
	public ResponseEntity<List<EInvoiceDTO>> getEInvoiceByDocId(@RequestParam List<String> docId) {
		String methodName = "getEInvoiceByDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		List<EInvoiceDTO> eInvoiceDTO= new ArrayList<>();
		try {
			eInvoiceDTO = eInvoiceService.getEInvoicePayloadByDocId(docId);

		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EInvoice Details information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(eInvoiceDTO);
	}

}
