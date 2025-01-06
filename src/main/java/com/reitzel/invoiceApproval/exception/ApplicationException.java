package com.reitzel.invoiceApproval.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApplicationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6587075753703182359L;
	/** Logger instance for {@link AuthPassAPI} */
	public static final Logger LOGGER = LoggerFactory.getLogger(ApplicationException.class);

	/**
	 * @param msg
	 */
	public ApplicationException(String msg) {

		super(msg);
		LOGGER.info("{}", msg);
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public ApplicationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * @return
	 */
	public <T> ResponseEntity<T> getResponse() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
