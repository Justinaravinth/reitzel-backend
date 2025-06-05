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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reitzel.invoiceApproval.common.CommonConstant;
import com.reitzel.invoiceApproval.common.UserConstants;
import com.reitzel.invoiceApproval.dto.CancelIRNDTO;
import com.reitzel.invoiceApproval.dto.EInvoiceDTO;
import com.reitzel.invoiceApproval.dto.EInvoiceGetToketDTO;
import com.reitzel.invoiceApproval.dto.EwayBillDTO;
import com.reitzel.invoiceApproval.dto.EwayBillNonIRNDTO;
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
	public ResponseEntity<EInvoiceDTO> getEInvoiceByDocId(@RequestParam String docId) {
		String methodName = "getEInvoiceByDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		EInvoiceDTO eInvoiceDTO= new EInvoiceDTO();
		try {
			eInvoiceDTO = eInvoiceService.getEInvoicePayloadByDocId(docId);

		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isEmpty(errorMsg)) {
			responseObjectsMap.put("eInvoiceDTO", eInvoiceDTO);
			
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EInvoice Details information receive failed",
					errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(eInvoiceDTO);
	}

	@GetMapping("/getEwayBillByDocId")
	public ResponseEntity<EwayBillDTO> getEwayBillByDocId(@RequestParam String docid) {
		String methodName = "getEwayBillByDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		EwayBillDTO ewayBillDTO = new EwayBillDTO();
		try {
			ewayBillDTO = eInvoiceService.getEWayBillByDocIdnew(docid);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EWayBill Information Get Successfully");
			responseObjectsMap.put("ewayBillDTO", ewayBillDTO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EWayBill Information Get Filed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(ewayBillDTO);
	}
	
//	@GetMapping("/getEWayBillByDocId")
//	public ResponseEntity<EwayBillDTO> getEWayBillByDocId() {
//		String methodName = "getEWayBillByDocId()";
//		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
//		String errorMsg = null;
//		Map<String, Object> responseObjectsMap = new HashMap<>();
//		ResponseDTO responseDTO = null;
//		EwayBillDTO ewayBillDTO= new EwayBillDTO();
//		try {
//			ewayBillDTO = eInvoiceService.getEWayBillByDocId();
//
//		} catch (Exception e) {
//			errorMsg = e.getMessage();
//			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
//		}
//		if (StringUtils.isEmpty(errorMsg)) {
//			responseObjectsMap.put("ewayBillDTO", ewayBillDTO);
//			
//		} else {
//			responseDTO = createServiceResponseError(responseObjectsMap, "EwayBillDTO Details information receive failed",
//					errorMsg);
//		}
//		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
//		return ResponseEntity.ok().body(ewayBillDTO);
//	}
	
	@PostMapping("/getToken")
	public ResponseEntity<ResponseDTO> generateToken(@RequestBody List<EInvoiceGetToketDTO> eInvoiceGetToketDTO1 ) {
		String methodName = "generateToken()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		Map<String,Object> ewayBillDTO = new HashMap<>();
		try {
			ewayBillDTO = eInvoiceService.generateToken(eInvoiceGetToketDTO1);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Token Information Get Successfully");
			responseObjectsMap.put("ewayBillDTO", ewayBillDTO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "Token Information Get Filed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@GetMapping("/getEwayBillNonIRNByDocId")
	public ResponseEntity<EwayBillNonIRNDTO> getEwayBillNonIRNByDocId(@RequestParam String docid) {
		String methodName = "getEwayBillNonIRNByDocId()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		EwayBillNonIRNDTO ewayBillDTO = new EwayBillNonIRNDTO();
		try {
			ewayBillDTO = eInvoiceService.generateEwayBillByNonIRN(docid);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EWayBill Information Get Successfully");
			responseObjectsMap.put("ewayBillDTO", ewayBillDTO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EWayBill Information Get Filed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(ewayBillDTO);
	}
	
	@GetMapping("/cancelIRN")
	public ResponseEntity<CancelIRNDTO> cancelIRN(@RequestParam String docid) {
		String methodName = "cancelIRN()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		CancelIRNDTO cancelIRNDTO = new CancelIRNDTO();
		try {
			cancelIRNDTO = eInvoiceService.cancelIRN(docid);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		if (StringUtils.isBlank(errorMsg)) {
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EWayBill Information Get Successfully");
			responseObjectsMap.put("cancelIRNDTO", cancelIRNDTO);
			responseDTO = createServiceResponse(responseObjectsMap);
		} else {
			responseDTO = createServiceResponseError(responseObjectsMap, "EWayBill Information Get Filed", errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(cancelIRNDTO);
	}
}
