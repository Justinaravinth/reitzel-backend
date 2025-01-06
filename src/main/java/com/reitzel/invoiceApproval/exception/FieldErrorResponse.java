package com.reitzel.invoiceApproval.exception;

import java.time.Instant;
import java.util.Map;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class FieldErrorResponse {
	
	private int status;
	private Instant timestamp;
	private String title;
	private Map<String, String> fieldValidationError;
	
}
