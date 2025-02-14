package com.reitzel.invoiceApproval.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reitzel.invoiceApproval.dto.IRNResponseDTO;
import com.reitzel.invoiceApproval.dto.InvoiceResponseDTO;
import com.reitzel.invoiceApproval.dto.PayloadDTO;
import com.reitzel.invoiceApproval.entity.EInvoiceVO;
import com.reitzel.invoiceApproval.entity.IRNResponseVO;
import com.reitzel.invoiceApproval.entity.InvoiceResponseVO;
import com.reitzel.invoiceApproval.repo.EInvoiceRepo;
import com.reitzel.invoiceApproval.repo.IRNResponseRepo;
import com.reitzel.invoiceApproval.repo.InvoiceResponseRepo;
import com.reitzel.invoiceApproval.service.EInvoiceService;

@RestController
@RequestMapping("/api/auth")
public class EinvoiceAuthController {

	@Autowired
	EInvoiceService eInvoiceService;
	
	@Autowired
	EInvoiceRepo eInvoiceRepo;
	
	@Autowired
	InvoiceResponseRepo invoiceResponseRepo;
	
	@Autowired
	IRNResponseRepo irnResponseRepo;

	@Value("${einv.client.id}")
	private String clientId;

	@Value("${einv.client.secret}")
	private String clientSecret;

	@Value("${einv.gstin}")
	private String gstin;

	@Value("${einv.user.name}")
	private String userName;

	@Value("${einv.auth.token}")
	private String authToken;

	@Value("${einv.sek}")
	private String sek1;

	@PostMapping("/encrypt")
	public IRNResponseDTO EncryptionUtil(String docId) throws JsonProcessingException {
		
		IRNResponseVO irnResponseVO= new IRNResponseVO();
		IRNResponseDTO irnResponse = new IRNResponseDTO();
		InvoiceResponseDTO invoiceResponseDTO = new InvoiceResponseDTO();
		PayloadDTO payloadDTO = new PayloadDTO();
		String sek = sek1;

		Object eInvoicePayload = eInvoiceService.getEInvoicePayloadByDocId(docId);

		// Convert object to JSON string
		ObjectMapper objectMapper = new ObjectMapper();
		String name = objectMapper.writeValueAsString(eInvoicePayload);
		String encryptedName = encryptBySymmetricKey(name, sek);

		payloadDTO.setData(encryptedName);

		String url = "https://einv1api.gstsandbox.nic.in/eicore/v1.03/Invoice";
		HttpHeaders headers = new HttpHeaders();
		headers.set("client_id", clientId);
		headers.set("client_secret", clientSecret);
		headers.set("gstin", gstin);
		headers.set("user_name", userName);
		headers.set("authtoken", authToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<PayloadDTO> request = new HttpEntity<>(payloadDTO, headers);
		RestTemplate restTemplate = new RestTemplate();
		try {
		    ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		    System.out.println("Raw Response: " + response.getBody());
		    InvoiceResponseVO invoiceResponseVO= new InvoiceResponseVO();
		    invoiceResponseVO.setDocid(docId);
		    invoiceResponseVO.setResponse(response.getBody());
		    invoiceResponseRepo.save(invoiceResponseVO);
		    // Convert JSON response to a Map
		    ObjectMapper objectMapper1 = new ObjectMapper();
		    Map<String, Object> mp = objectMapper1.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});

		    // Map to InvoiceResponse object
		    invoiceResponseDTO.setStatus(mp.get("Status") != null ? Integer.parseInt(mp.get("Status").toString()) : 0);
		    invoiceResponseDTO.setErrorDetails(mp.get("ErrorDetails") != null ? mp.get("ErrorDetails").toString() : null);

		    // Convert Data field if present
		    if (mp.get("Data") != null) {
		        String datas = mp.get("Data").toString();
		        byte[] dt = datas.getBytes(StandardCharsets.UTF_8);
		        invoiceResponseDTO.setData(dt);
		        if(invoiceResponseDTO.getData()!=null)
		        {
		        	String decryptedText = decryptBySymmetricKey(datas, sek);
					ObjectMapper objectMapper3 = new ObjectMapper();
					Map<String, Object> decryptedMap = objectMapper3.readValue(decryptedText, Map.class);

					// Extract the 'AckNo' value from the map
					irnResponse.setAckNo(decryptedMap.get("AckNo").toString());
					irnResponse.setAckDt(decryptedMap.get("AckDt").toString());
					irnResponse.setStatus(decryptedMap.get("Status").toString());
					irnResponse.setIrn(decryptedMap.get("Irn").toString());
					String signedInvoice = decryptedMap.get("SignedInvoice").toString();
//					byte[] signedInvoiceBytes = signedInvoice.getBytes(StandardCharsets.UTF_8);
					irnResponse.setSignedInvoice(signedInvoice);
					String signedQRCode = decryptedMap.get("SignedQRCode").toString();
//					byte[] signedQRCodeBytes = signedQRCode.getBytes(StandardCharsets.UTF_8);
					irnResponse.setSignedQRCode(signedQRCode);
					
					irnResponseVO.setAckNo(decryptedMap.get("AckNo").toString());
					irnResponseVO.setAckDt(decryptedMap.get("AckDt").toString());
					irnResponseVO.setStatus(decryptedMap.get("Status").toString());
					irnResponseVO.setIrn(decryptedMap.get("Irn").toString());
					irnResponseVO.setDocid(docId);
					irnResponseVO.setSignedInvoice(signedInvoice);
					irnResponseVO.setSignedQRCode(signedQRCode);					
					irnResponseRepo.save(irnResponseVO);
					
					List<EInvoiceVO> eInvoiceVOs = eInvoiceRepo.getDocidDetails(docId);
					List<EInvoiceVO> updatedEInvoiceVOs = new ArrayList<>();

					for (EInvoiceVO eInvoiceVO : eInvoiceVOs) {  
					    eInvoiceVO.setAckno(irnResponseVO.getAckNo());
					    eInvoiceVO.setAckdate(irnResponseVO.getAckDt());
					    eInvoiceVO.setIrn(irnResponseVO.getIrn());
					    eInvoiceVO.setSignedqrcode(irnResponseVO.getSignedQRCode());
					    
					    updatedEInvoiceVOs.add(eInvoiceVO); // ✅ Add to a separate list
					}

					eInvoiceRepo.saveAll(updatedEInvoiceVOs);
					
		        }
		    }
		    return irnResponse;
		} catch (Exception e) {
		    e.printStackTrace();
		    return null;  // Handle errors properly based on your business logic
		}

	}

	// Encrypt method using symmetric encryption (AES)
	public String encryptBySymmetricKey(String textToEncrypt, String decryptedSek) {
		try {
			// Decode the secret key (AES key) from Base64 string
			byte[] sekByte = Base64.getDecoder().decode(decryptedSek);
			SecretKey aesKey = new SecretKeySpec(sekByte, "AES");

			// Initialize the AES cipher in encryption mode with PKCS5Padding
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

			cipher.init(Cipher.ENCRYPT_MODE, aesKey);

			// Encrypt the text (the name or any string) as bytes
			byte[] encryptedBytes = cipher.doFinal(textToEncrypt.getBytes(StandardCharsets.UTF_8));

			// Return the encrypted bytes as a Base64-encoded string
			return Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return "Encryption error: " + e.getMessage();
		}
	}

	@GetMapping("/decrypt")
	public IRNResponseDTO decryptUtil() {
		IRNResponseDTO irnResponse = new IRNResponseDTO();
		String decryptedText = null;
		String decryptedSek = "onU2halpHlCHLUlTbhBfRY+fZ/x0oy15oKlaaDvUsDg="; // Secret AES Key (Base64 encoded)
		String enc = "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";

		try {
			// Decrypt the encrypted string
			decryptedText = decryptBySymmetricKey(enc, decryptedSek);
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> decryptedMap = objectMapper.readValue(decryptedText, Map.class);

			// Extract the 'AckNo' value from the map
			irnResponse.setAckNo(decryptedMap.get("AckNo").toString());
			irnResponse.setAckDt(decryptedMap.get("AckDt").toString());
			irnResponse.setStatus(decryptedMap.get("Status").toString());
			irnResponse.setIrn(decryptedMap.get("Irn").toString());
			String signedInvoice = decryptedMap.get("SignedInvoice").toString();
			byte[] signedInvoiceBytes = signedInvoice.getBytes(StandardCharsets.UTF_8);
			irnResponse.setSignedInvoice(signedInvoice);
			String signedQRCode = decryptedMap.get("SignedQRCode").toString();
			byte[] signedQRCodeBytes = signedQRCode.getBytes(StandardCharsets.UTF_8);
			irnResponse.setSignedQRCode(signedQRCode);

		} catch (Exception e) {
			e.printStackTrace();
			IRNResponseDTO errorResponse = new IRNResponseDTO();
			errorResponse.setStatus("Error");

		}
		return irnResponse;
	}

	public String decryptBySymmetricKey(String encryptedText, String decryptedSek) throws Exception {
		// Decode the AES key from Base64
		byte[] sekByte = Base64.getDecoder().decode(decryptedSek);
		SecretKey aesKey = new SecretKeySpec(sekByte, "AES");

		// Initialize AES cipher for decryption
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, aesKey);

		// Decode and decrypt the encrypted text
		byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));

		// Convert decrypted bytes to a string and return
		return new String(decryptedBytes, StandardCharsets.UTF_8);
	}

	public static void main(String[] args) {
		// Provided encrypted secret key (Base64-encoded)

	}

}
