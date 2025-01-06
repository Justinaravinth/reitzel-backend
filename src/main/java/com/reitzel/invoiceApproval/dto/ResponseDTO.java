package com.reitzel.invoiceApproval.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResponseDTO {

	private static final long serialVersionUID = 1L;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static final String OK = "Ok";

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static final String ERROR = "Error";

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<ResponseErrors> errors;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String statusFlag;

	private boolean status;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Map<String, Object> paramObjectsMap = new HashMap<>();

	public final void addObject(final Object obj, final String key) {
		paramObjectsMap.put(key, obj);
	}

	public final Object getObject(final String key) {
		return this.paramObjectsMap.get(key);
	}

	public void addAllObject(Map<String, Object> mapObj) {
		this.paramObjectsMap.putAll(mapObj);
	}

	public final void clearParamMap(final String key) {
		paramObjectsMap.remove(key);
	}

	public final void setErrors(final List<ResponseErrors> errors) {
		this.errors = errors;
	}

	public final List<ResponseErrors> getErrors() {
		if (errors == null) {
			errors = new ArrayList<>();
		}

		return errors;
	}


}
