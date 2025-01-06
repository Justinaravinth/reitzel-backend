
package com.reitzel.invoiceApproval.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ObjectNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @param msg
	 */
	public ObjectNotFoundException(String msg) {
		super(msg);
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public ObjectNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * @return
	 */
	public <T> ResponseEntity<T> getResponse() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
