package com.reitzel.invoiceApproval.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateTokenDTO {
	
	@JsonProperty("UserName")
	private String userName;
	@JsonProperty("Password")
	private String password;
	@JsonProperty("AppKey")
	private String appKey;
	@JsonProperty("ForceRefreshAccessToken")
	private boolean forceRefreshAccessToken=true;

}
