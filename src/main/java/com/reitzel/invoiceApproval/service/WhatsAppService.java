package com.reitzel.invoiceApproval.service;

import org.springframework.stereotype.Service;

@Service
public interface WhatsAppService {
	
	String sendBirthdayMessage(String number, String name);

}
