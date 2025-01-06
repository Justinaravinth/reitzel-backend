
 package com.reitzel.invoiceApproval.exception;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ApplicationException.class)
	protected ResponseEntity<ErrorResponse> handleGlobalException(ApplicationException ae, Locale locale) {
		LOGGER.error("Application Business Error Thrown :: {}", ae.getMessage());
		return ResponseEntity.badRequest()
				.body(ErrorResponse.builder().message(ae.getMessage()).timestamp(Instant.now())
						.title("Application Business Error.").status(HttpStatus.BAD_REQUEST.value()).build());
	}

	@ExceptionHandler({ ResponseStatusException.class, ObjectNotFoundException.class })
	protected ResponseEntity<ErrorResponse> handleResponseStatusException(Exception e, Locale locale) {
		LOGGER.error("Resoure Not Found Thrown from api :: {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder().message(e.getMessage())
				.timestamp(Instant.now()).title("Resource Not Found").status(HttpStatus.NOT_FOUND.value()).build());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		LOGGER.error("handleMethodArgumentNotValid :: {}", ex.getMessage());
		Map<String, String> detailError = ex.getFieldErrors().stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
		return ResponseEntity.badRequest()
				.body(FieldErrorResponse.builder().fieldValidationError(detailError).timestamp(Instant.now())
						.title("Application Validation Error.").status(HttpStatus.BAD_REQUEST.value()).build());
	}
	
	@ExceptionHandler({ Exception.class })
	protected ResponseEntity<ErrorResponse> handleException(Exception e, Locale locale) {
		LOGGER.error("API Runtime Error Thrown :: {}", e.getMessage());
		return ResponseEntity.internalServerError()
				.body(ErrorResponse.builder().message(e.getMessage()).timestamp(Instant.now())
						.title("API Runtime Error.").status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build());
	}
}
