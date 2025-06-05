package com.reitzel.invoiceApproval.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reitzel.invoiceApproval.common.CommonConstant;
import com.reitzel.invoiceApproval.common.UserConstants;
import com.reitzel.invoiceApproval.dto.EInvoiceGetToketDTO;
import com.reitzel.invoiceApproval.dto.ResponseDTO;
import com.reitzel.invoiceApproval.service.EInvoiceService;


@RestController
@RequestMapping("/api/auth")
public class EinvoiceAuthController extends BaseController {

	@Autowired
	EInvoiceService eInvoiceService;
	
	
	@PostMapping("/createEInvoice")
	public ResponseEntity<ResponseDTO> createEInvoice(@RequestParam List<String> docId) {
		String methodName = "createEInvoice()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> irnResponseDTO = eInvoiceService.createEinvoice(docId);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Einvoice Generated Successfully");
			responseObjectsMap.put("irnResponseDTO", irnResponseDTO);
			responseDTO = createServiceResponse(irnResponseDTO);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PostMapping("/generateIRN")
	public ResponseEntity<ResponseDTO> generateIRN(@RequestParam List<String> docId) {
		String methodName = "generateIRN()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			String totalIRNGenarteDocid = eInvoiceService.generateIRN(docId);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Einvoice Generated Successfully");
			responseObjectsMap.put("totalIRNGenarteDocid", totalIRNGenarteDocid);
			responseDTO = createServiceResponse(responseObjectsMap);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}

	
	
	@PostMapping("/createEWayBill")
	public ResponseEntity<ResponseDTO> createEWayBill(@RequestParam List<String> docId) {
		String methodName = "createEWayBill()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> ewayResponseDTO = eInvoiceService.createEWayBill(docId);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EwayBill Generated Successfully");
			responseObjectsMap.put("ewayResponseDTO", ewayResponseDTO);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	@PostMapping("/createEWayBillNonIRN")
	public ResponseEntity<ResponseDTO> createEWayBillNonIRN(@RequestParam List<String> docId) {
		String methodName = "createEWayBillNonIRN()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> ewayResponseDTO = eInvoiceService.createEWayBillNonIRN(docId);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EwayBill Generated Successfully");
			responseObjectsMap.put("ewayResponseDTO", ewayResponseDTO);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	
	
	@PostMapping("/cancelIRNdetails")
	public ResponseEntity<ResponseDTO> cancelIRNdetails(@RequestParam List<String> docId) {
		String methodName = "cancelIRNdetails()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		String errorMsg = null;
		Map<String, Object> responseObjectsMap = new HashMap<>();
		ResponseDTO responseDTO = null;
		try {
			Map<String, Object> ewayResponseDTO = eInvoiceService.cancelIRNInvoice(docId);
			responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "EwayBill Generated Successfully");
			responseObjectsMap.put("ewayResponseDTO", ewayResponseDTO);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return ResponseEntity.ok().body(responseDTO);
	}
	

}
