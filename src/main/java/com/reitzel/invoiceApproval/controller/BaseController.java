package com.reitzel.invoiceApproval.controller;


import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reitzel.invoiceApproval.common.CommonConstant;
import com.reitzel.invoiceApproval.dto.ResponseDTO;


/**
 * Base Controller class service to create response information
 * 
 * 
 */

public class BaseController {

	/** Logger instance for {@link BaseController} */
	public static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

	/**
	 * Utility method to create the service response
	 * 
	 * @param resObjects Instance of {Map}
	 * @return responseDTO - It returns response DTO
	 * 
	 */
	public ResponseDTO createServiceResponse(final Map<String, Object> resObjects) {
		String methodName = "createServiceResponse()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		ResponseDTO responseDTO = null;
		try {
			responseDTO = new ResponseDTO();
			responseDTO.setStatusFlag(null);
			responseDTO.setStatusFlag(ResponseDTO.OK);
			responseDTO.setStatus(true);
			Set<String> keys = resObjects.keySet();
			for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
				String key = iterator.next();
				Object obj = resObjects.get(key);
				responseDTO.addObject(obj, key);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return responseDTO;
	}

	/**
	 * Utility method to create the error service response
	 * 
	 * @param resObjects Instance of {Map}
	 * @param msg        - Error message
	 * @return responseDTO - It returns response DTO
	 */
	public ResponseDTO createServiceResponseError(final Map<String, Object> resObjects, String msg, String errorMsg) {
		String methodName = "createServiceResponseError()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		ResponseDTO responseDTO = null;
		try {
			responseDTO = new ResponseDTO();
			responseDTO.setStatusFlag(ResponseDTO.ERROR);
			responseDTO.setStatus(false);
			responseDTO.addObject(msg, CommonConstant.RESPONSE_MESSAGE);
			responseDTO.addObject(errorMsg, CommonConstant.ERROR_MESSAGE);
			Set<String> keys = resObjects.keySet();
			for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
				String key = iterator.next();
				Object obj = resObjects.get(key);
				responseDTO.addObject(obj, key);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return responseDTO;
	}

	/**
	 * Utility method to create the success service response
	 * 
	 * @param resObjects Instance of {Map}
	 * @param msg        - success message
	 * @return responseDTO - It returns response DTO
	 */
	public ResponseDTO createServiceResponseMsg(final Map<String, Object> resObjects, String msg) {
		String methodName = "createServiceResponseMsg()";
		LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
		ResponseDTO responseDTO = null;
		try {
			responseDTO = new ResponseDTO();
			responseDTO.setStatusFlag(ResponseDTO.OK);
			responseDTO.setStatus(true);
			responseDTO.addObject(msg, CommonConstant.RESPONSE_MESSAGE);
			Set<String> keys = resObjects.keySet();
			for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
				String key = iterator.next();
				Object obj = resObjects.get(key);
				responseDTO.addObject(obj, key);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
		return responseDTO;
	}

//	public String getErrorMessage(Exception exception, String methodName) {
//		LOGGER.error(CommonConstant.ERR_MSG_FORMAT, methodName, exception.getMessage());
//		String errorMsg = CommonConstant.SERVICE_UNAVAILABLE;
//		if (exception instanceof ApplicationException || exception instanceof BadCredentialsException || exception instanceof ParseException) {
//			errorMsg = exception.getMessage();
//		}
//		return errorMsg;
//	}
}
