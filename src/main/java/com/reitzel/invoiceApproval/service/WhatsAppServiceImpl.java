package com.reitzel.invoiceApproval.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.reitzel.invoiceApproval.repo.EInvoiceRepo;

@Service
public class WhatsAppServiceImpl implements WhatsAppService {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	EInvoiceRepo  eInvoiceRepo;
	
	
	@Value("${whatsapp.service.url}")
    private String whatsappServiceUrl;

	@Override
	public String sendBirthdayMessage(String number, String name) {
	    try {
	    	String url=whatsappServiceUrl + "/send-whatsapp";
	        List<Object[]> ackData = eInvoiceRepo.getAckDateWiseCount();

	        StringBuilder messageBuilder = new StringBuilder("📅 *Date-wise IRN Counts:*\n\n");

	        for (Object[] row : ackData) {
	            String date = String.valueOf(row[0]);  // e.g., "30-05-2025"
	            String count = String.valueOf(row[1]); // e.g., "5"
	            messageBuilder.append("🗓 ").append(date).append(": ").append(count).append("\n");
	        }

	        String message = messageBuilder.toString();  // ✅ Use the built message

	        // Create headers
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        // Prepare request body
	        Map<String, String> requestBody = new HashMap<>();
	        requestBody.put("number", number);      // WhatsApp number
	        requestBody.put("message", message);    // Final message

	        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

	        // Send to WhatsApp Node.js service
	        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

	        return response.getBody();

	    } catch (ResourceAccessException e) {
	        throw new RuntimeException("WhatsApp service unreachable. Ensure Node.js is running on port 3001.");
	    } catch (HttpClientErrorException e) {
	        throw new RuntimeException("WhatsApp service error: " + e.getResponseBodyAsString());
	    }
	}
	
	


}
