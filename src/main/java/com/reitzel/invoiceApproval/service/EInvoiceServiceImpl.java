package com.reitzel.invoiceApproval.service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reitzel.invoiceApproval.dto.BchDtlsDTO;
import com.reitzel.invoiceApproval.dto.BuyerDetailsDTO;
import com.reitzel.invoiceApproval.dto.DispatchDetailsDTO;
import com.reitzel.invoiceApproval.dto.DocumentDetailsDTO;
import com.reitzel.invoiceApproval.dto.EInvoiceDTO;
import com.reitzel.invoiceApproval.dto.EInvoiceGetToketDTO;
import com.reitzel.invoiceApproval.dto.EWayBillDetailsDTO;
import com.reitzel.invoiceApproval.dto.EwayBillDTO;
import com.reitzel.invoiceApproval.dto.EwayBillNonIRNDTO;
import com.reitzel.invoiceApproval.dto.EwayBillResponseDTO;
import com.reitzel.invoiceApproval.dto.ExpShipDetailsDTO;
import com.reitzel.invoiceApproval.dto.ExportDetailsDTO;
import com.reitzel.invoiceApproval.dto.GenerateTokenDTO;
import com.reitzel.invoiceApproval.dto.IRNResponseDTO;
import com.reitzel.invoiceApproval.dto.InvoiceResponseDTO;
import com.reitzel.invoiceApproval.dto.ItemDTO;
import com.reitzel.invoiceApproval.dto.ItemListDTO;
import com.reitzel.invoiceApproval.dto.PayloadDTO;
import com.reitzel.invoiceApproval.dto.SelletDetailsDTO;
import com.reitzel.invoiceApproval.dto.ShippingDetailsDTO;
import com.reitzel.invoiceApproval.dto.TransactionDetailsDTO;
import com.reitzel.invoiceApproval.dto.ValueDetailsDTO;
import com.reitzel.invoiceApproval.entity.EInvoiceVO;
import com.reitzel.invoiceApproval.entity.EwayBillResponseVO;
import com.reitzel.invoiceApproval.entity.EwayResponseVO;
import com.reitzel.invoiceApproval.entity.HeaderDetailsVO;
import com.reitzel.invoiceApproval.entity.IRNResponseVO;
import com.reitzel.invoiceApproval.entity.InvoiceResponseVO;
import com.reitzel.invoiceApproval.repo.EInvoiceRepo;
import com.reitzel.invoiceApproval.repo.EwayBillResponseRepo;
import com.reitzel.invoiceApproval.repo.EwayHeadersRepo;
import com.reitzel.invoiceApproval.repo.EwayResponseRepo;
import com.reitzel.invoiceApproval.repo.HeaderDetailsRepo;
import com.reitzel.invoiceApproval.repo.IRNResponseRepo;
import com.reitzel.invoiceApproval.repo.InvoiceResponseRepo;

@Service
public class EInvoiceServiceImpl implements EInvoiceService {

	@Autowired
	EInvoiceRepo eInvoiceRepo;

	@Autowired
	InvoiceResponseRepo invoiceResponseRepo;

	@Autowired
	HeaderDetailsRepo headerDetailsRepo;

	@Autowired
	IRNResponseRepo irnResponseRepo;

	@Autowired
	EwayBillResponseRepo ewayBillResponseRepo;

	@Autowired
	EwayResponseRepo ewayResponseRepo;

	@Autowired
	EwayHeadersRepo ewayHeadersRepo;

	static byte[] appKey1 = null;

	@Override
	public List<EInvoiceVO> getEInvoiceByDocId(String docId) {

		List<EInvoiceVO> invoiceVO = eInvoiceRepo.findBydocid(docId);
		return invoiceVO;
	}

	@Override
	public EInvoiceDTO getEInvoicePayloadByDocId(String docIds) {

		EInvoiceDTO eInvoiceDTO = new EInvoiceDTO();
		// Iterate through each docId in the set

		String docId = docIds;
		Object[] headerDetails = eInvoiceRepo.getHeaderDetails(docId);
		Object[] header = eInvoiceRepo.getHeaders(docId);

		if (headerDetails.length > 0 && headerDetails[0] instanceof Object[]) {
			Object[] nestedArray = (Object[]) headerDetails[0];
			Object[] head = (Object[]) header[0];

			// Create and populate an EInvoiceDTO object

			// Populate TransactionDetailsDTO
			TransactionDetailsDTO transactionDetailsDTO = new TransactionDetailsDTO();
			transactionDetailsDTO.setSupTyp(nestedArray[2].toString());
			transactionDetailsDTO.setTaxSch(head[0].toString());
			transactionDetailsDTO.setRegRev(head[1].toString());
			transactionDetailsDTO.setIgstOnIntra(head[2].toString());
			eInvoiceDTO.setTranDtls(transactionDetailsDTO);

			// Populate DocumentDetailsDTO
			DocumentDetailsDTO documentDetailsDTO = new DocumentDetailsDTO();
			documentDetailsDTO.setNo(nestedArray[0].toString());
			String dateString = nestedArray[1].toString();
			String formattedDate = formatDate(dateString);

			// Set the formatted date
			documentDetailsDTO.setDt(formattedDate);
			documentDetailsDTO.setTyp(nestedArray[3].toString());
			eInvoiceDTO.setDocDtls(documentDetailsDTO);

			SelletDetailsDTO selletDetailsDTO = new SelletDetailsDTO();
			selletDetailsDTO.setGstin(head[3].toString());
			selletDetailsDTO.setLglNm(head[4].toString());
			selletDetailsDTO.setTrdNm(head[5].toString());
			selletDetailsDTO.setAddr1(head[6].toString());
			selletDetailsDTO.setAddr2(head[7].toString());
			selletDetailsDTO.setLoc(head[8].toString());
			selletDetailsDTO.setPin(Integer.parseInt(head[9].toString()));
			selletDetailsDTO.setStcd(head[10].toString());
			selletDetailsDTO.setPh(null);
			selletDetailsDTO.setEm(null);
			eInvoiceDTO.setSellerDtls(selletDetailsDTO);

			DispatchDetailsDTO dd = new DispatchDetailsDTO();

			// Populate BuyerDetailsDTO
			BuyerDetailsDTO buyerDetailsDTO = new BuyerDetailsDTO();
			buyerDetailsDTO.setGstin(nestedArray[4].toString());
			buyerDetailsDTO.setLglNm(nestedArray[5].toString());
			buyerDetailsDTO.setTrdNm(nestedArray[6].toString());
			buyerDetailsDTO.setPos(nestedArray[7].toString());
			buyerDetailsDTO.setAddr1(nestedArray[8].toString());
			buyerDetailsDTO.setAddr2(nestedArray[9].toString());
			buyerDetailsDTO.setLoc(nestedArray[10].toString());
			buyerDetailsDTO.setPin(Integer.parseInt(nestedArray[11].toString()));
			buyerDetailsDTO.setStcd(nestedArray[12].toString());
			buyerDetailsDTO.setPh(null);
			buyerDetailsDTO.setEm(null);
			eInvoiceDTO.setBuyerDtls(buyerDetailsDTO);

			ExportDetailsDTO expDtls = new ExportDetailsDTO();
			eInvoiceDTO.setExpDtls(expDtls);

			EWayBillDetailsDTO eWayBillDetailsDTO = new EWayBillDetailsDTO();

			ShippingDetailsDTO sd = new ShippingDetailsDTO();

			// Populate ValueDetailsDTO
			ValueDetailsDTO valueDetailsDTO = new ValueDetailsDTO();
			valueDetailsDTO.setAssVal(Double.parseDouble(nestedArray[13].toString()));
			valueDetailsDTO.setIgstVal(Double.parseDouble(nestedArray[14].toString()));
			valueDetailsDTO.setCgstVal(Double.parseDouble(nestedArray[15].toString()));
			valueDetailsDTO.setSgstVal(Double.parseDouble(nestedArray[16].toString()));
			valueDetailsDTO.setOthChrg(Double.parseDouble(nestedArray[17].toString()));
			valueDetailsDTO.setTotInvVal(Double.parseDouble(nestedArray[18].toString()));
			eInvoiceDTO.setValDtls(valueDetailsDTO);

			// Fetch item details (assuming a list of items)
			List<Object[]> itemDetailsList = eInvoiceRepo.getChargeDetails(docId);
			List<EInvoiceVO> eInvoiceVO = eInvoiceRepo.getDocidDetails(docId);
//				if (eInvoiceVO != null && !eInvoiceVO.isEmpty()) {
//				    for (EInvoiceVO eInvoiceVO2 : eInvoiceVO) {
//				        eInvoiceVO2.setAckno("12345"); // Update the field
//				    }
//				    eInvoiceRepo.saveAll(eInvoiceVO); // Save the updated list
//				}
			List<ItemDTO> itemList = new ArrayList<>();

			for (Object[] item : itemDetailsList) {
				ItemDTO itemDTO = new ItemDTO();
				itemDTO.setSlNo(item[0].toString());
				itemDTO.setPrdDesc(item[1].toString());
				itemDTO.setIsServc(item[2].toString());
				itemDTO.setHsnCd(item[3].toString());
				itemDTO.setQty(Integer.parseInt(item[4].toString()));
				itemDTO.setUnitPrice(Double.parseDouble(item[5].toString()));
				itemDTO.setTotAmt(Double.parseDouble(item[6].toString()));
				itemDTO.setAssAmt(Double.parseDouble(item[7].toString()));
				itemDTO.setGstRt(Double.parseDouble(item[8].toString()));
				itemDTO.setIgstAmt(Double.parseDouble(item[9].toString()));
				itemDTO.setSgstAmt(Double.parseDouble(item[10].toString()));
				itemDTO.setCgstAmt(Double.parseDouble(item[11].toString()));
				itemDTO.setTotItemVal(Double.parseDouble(item[12].toString()));
				itemDTO.setUnit(item[13].toString());
				BchDtlsDTO bch = new BchDtlsDTO();
				itemList.add(itemDTO);
			}

			eInvoiceDTO.setItemList(itemList);

			// Add the populated EInvoiceDTO to the list
		}
		return eInvoiceDTO;
	}

//		

	private String formatDate(String dateString) {
		try {
			// Parse the incoming date string (adjust format if necessary)
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			Date date = inputFormat.parse(dateString);

			// Define the desired output format
			SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

			// Return the formatted date
			return outputFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null; // In case of parsing error, you can return a default or error value
		}
	}

	@Override
	public Map<String, Object> createEinvoice(List<String> docIds) throws JsonProcessingException {

		String message = null;
		List<IRNResponseDTO> irnResponse = new ArrayList<>();
		for (String docId : docIds) {

			List<EInvoiceVO> eInvoiceVOs = eInvoiceRepo.getDocidDetails(docId);
			List<EInvoiceVO> updatedEInvoiceVOs = new ArrayList<>();

			String userName = "";
			String gstin = "";
			String clientId = "";
			String clientSecret = "";
			String authToken = "";
			String sek = "";

			Set<Object[]> headerDetails = headerDetailsRepo.getHeaderDetails(docId);
			if (!headerDetails.isEmpty()) {
				Object[] firstRow = headerDetails.iterator().next(); // Get the first row

				userName = firstRow[0].toString();
				gstin = firstRow[1].toString();
				clientId = firstRow[2].toString();
				clientSecret = firstRow[3].toString();
				authToken = firstRow[4].toString();
				sek = firstRow[5].toString();
			}

			IRNResponseVO irnResponseVO = new IRNResponseVO();

			InvoiceResponseDTO invoiceResponseDTO = new InvoiceResponseDTO();
			PayloadDTO payloadDTO = new PayloadDTO();

			Object eInvoicePayload = getEInvoicePayloadByDocId(docId);

			// Convert object to JSON string
			ObjectMapper objectMapper = new ObjectMapper();
			String name = objectMapper.writeValueAsString(eInvoicePayload);
			String encryptedName = encryptBySymmetricKey(name, sek);

			payloadDTO.setData(encryptedName);
			// SandBox API
//			String url = "https://einv1api.gstsandbox.nic.in/eicore/v1.03/Invoice";
			// Live API
			 String url = "https://api.einvoice1.gst.gov.in/eicore/v1.03/Invoice";
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
				InvoiceResponseVO invoiceResponseVO = new InvoiceResponseVO();
				invoiceResponseVO.setDocid(docId);
				invoiceResponseVO.setResponse(response.getBody());
				ObjectMapper objectMapper5 = new ObjectMapper();
				Map<String, Object> mp1 = objectMapper5.readValue(response.getBody(),
						new TypeReference<Map<String, Object>>() {
						});
				if (mp1.get("Status").equals(0)) {
					invoiceResponseVO.setIserror("Y");
					Object errorDetailsObj = mp1.get("ErrorDetails");
					if (errorDetailsObj instanceof List) {
						List<?> errorDetailsList = (List<?>) errorDetailsObj;
						if (!errorDetailsList.isEmpty() && errorDetailsList.get(0) instanceof Map) {
							Map<?, ?> firstError = (Map<?, ?>) errorDetailsList.get(0);
							Object errorCode = firstError.get("ErrorCode");
							Object errorMessage = firstError.get("ErrorMessage");
							if (errorCode != null) {
								invoiceResponseVO.setMessage("ErrorCode: " + errorCode.toString());
								invoiceResponseVO.setErrordetails(errorMessage.toString());
							}
						}
					}

				} else {
					invoiceResponseVO.setIserror("N");
					invoiceResponseVO.setMessage("IRN Generated");
				}
				for (EInvoiceVO eInvoiceVO : eInvoiceVOs) {
					eInvoiceVO.setApicall("T");
					updatedEInvoiceVOs.add(eInvoiceVO);
				}
				eInvoiceRepo.saveAll(updatedEInvoiceVOs);
				invoiceResponseRepo.save(invoiceResponseVO);
				// Convert JSON response to a Map
				ObjectMapper objectMapper1 = new ObjectMapper();
				Map<String, Object> mp = objectMapper1.readValue(response.getBody(),
						new TypeReference<Map<String, Object>>() {
						});

				// Map to InvoiceResponse object
				invoiceResponseDTO
						.setStatus(mp.get("Status") != null ? Integer.parseInt(mp.get("Status").toString()) : 0);
				invoiceResponseDTO
						.setErrorDetails(mp.get("ErrorDetails") != null ? mp.get("ErrorDetails").toString() : null);

				// Convert Data field if present
				if (mp.get("Data") != null) {
					String datas = mp.get("Data").toString();
					byte[] dt = datas.getBytes(StandardCharsets.UTF_8);
					invoiceResponseDTO.setData(dt);
					if (invoiceResponseDTO.getData() != null) {
						String decryptedText = decryptBySymmetricKey(datas, sek);
						ObjectMapper objectMapper3 = new ObjectMapper();
						Map<String, Object> decryptedMap = objectMapper3.readValue(decryptedText, Map.class);

						IRNResponseDTO iRNResponseDTO = new IRNResponseDTO();
						// Extract the 'AckNo' value from the map
						iRNResponseDTO.setAckNo(decryptedMap.get("AckNo").toString());
						iRNResponseDTO.setAckDt(decryptedMap.get("AckDt").toString());
						iRNResponseDTO.setStatus(decryptedMap.get("Status").toString());
						iRNResponseDTO.setIrn(decryptedMap.get("Irn").toString());
						String signedInvoice = decryptedMap.get("SignedInvoice").toString();
//					byte[] signedInvoiceBytes = signedInvoice.getBytes(StandardCharsets.UTF_8);
						iRNResponseDTO.setSignedInvoice(signedInvoice);
						String signedQRCode = decryptedMap.get("SignedQRCode").toString();
//					byte[] signedQRCodeBytes = signedQRCode.getBytes(StandardCharsets.UTF_8);
						iRNResponseDTO.setSignedQRCode(signedQRCode);

						irnResponse.add(iRNResponseDTO);
						irnResponseVO.setAckNo(decryptedMap.get("AckNo").toString());
						irnResponseVO.setAckDt(decryptedMap.get("AckDt").toString());
						irnResponseVO.setStatus(decryptedMap.get("Status").toString());
						irnResponseVO.setIrn(decryptedMap.get("Irn").toString());
						irnResponseVO.setDocid(docId);
						irnResponseVO.setSignedInvoice(signedInvoice);
						irnResponseVO.setSignedQRCode(signedQRCode);
						irnResponseRepo.save(irnResponseVO);

						for (EInvoiceVO eInvoiceVO : eInvoiceVOs) {
							eInvoiceVO.setAckno(irnResponseVO.getAckNo());
							eInvoiceVO.setAckdate(irnResponseVO.getAckDt());
							eInvoiceVO.setIrn(irnResponseVO.getIrn());
							eInvoiceVO.setIrnstatus("T");
							eInvoiceVO.setSignedqrcode(irnResponseVO.getSignedQRCode());

							updatedEInvoiceVOs.add(eInvoiceVO); // ✅ Add to a separate list
						}

						eInvoiceRepo.saveAll(updatedEInvoiceVOs);
					}
				} else {
					for (EInvoiceVO eInvoiceVO : eInvoiceVOs) {
						eInvoiceVO.setIrnstatus("F");
						updatedEInvoiceVOs.add(eInvoiceVO);
					}
					eInvoiceRepo.saveAll(updatedEInvoiceVOs);
				}
				message = "IRN Genaretd Successfully";
			} catch (Exception e) {
				e.printStackTrace();
				return null; // Handle errors properly based on your business logic
			}
		}
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return response;
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

	@Scheduled(fixedRate = 2000)
	public void processEInvoices() throws JsonProcessingException {
		System.out.println("Running E-Invoice service every 1 minute...");
		// Replace with actual branchCode

		List<Object[]> getPendingIrnDetails = eInvoiceRepo.getPendingIRNDetails();
		if (getPendingIrnDetails != null) {

			int length = getPendingIrnDetails.size();
			System.out.println("Length of the list: " + length);
			// Extract docIds from the list
			List<String> docIds = new ArrayList<>();
			for (Object[] record : getPendingIrnDetails) {
				if (record != null && record.length > 0) {
					String docId = record[0].toString(); // Assuming docId is the first column
					docIds.add(docId);
				}
			}
			// Call the service method with the collected docIds
			if (!docIds.isEmpty()) {
				System.out.println(" Process Success.");
				createEinvoice(docIds);

			} else {
				System.out.println("No docIds found to process.");
			}
		} else {
			System.out.println("List is null.");
		}

	}

	@Override
	public String generateIRN(List<String> docid) {
		int i = 0;
		for (String docId : docid) {
			List<EInvoiceVO> eInvoiceVOs = new ArrayList<>();
			List<EInvoiceVO> eInvoiceVO = eInvoiceRepo.getDocidDetails(docId);
			for (EInvoiceVO eInvoiceVO2 : eInvoiceVO) {
				eInvoiceVO2.setGeneinvoice("T");
				eInvoiceVOs.add(eInvoiceVO2);
				i++;
			}
			eInvoiceRepo.saveAll(eInvoiceVOs);
		}
		return "Successfull Docid" + i;

	}

	// Safely extract Integer values from Object[]
	private int getIntValue(Object[] array, int index) {
		if (array != null && array.length > index && array[index] != null) {
			try {
				return Integer.parseInt(array[index].toString().trim());
			} catch (NumberFormatException e) {
				System.err.println("Error parsing integer at index " + index + ": " + array[index]);
			}
		}
		return 0; // Return default value instead of null
	}

	// Format Date
	private String formatDate1(String dateString) {
		try {
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			Date date = inputFormat.parse(dateString);

			SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
			return outputFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Map<String, Object> createEWayBill(List<String> docId) throws JsonProcessingException {
		String message = null;
		for (String irn : docId) {

			List<EInvoiceVO> eInvoiceVOs = eInvoiceRepo.getIrnDetails(irn);
			List<EInvoiceVO> updatedEInvoiceVOs = new ArrayList<>();

			String userName = "";
			String gstin = "";
			String clientId = "";
			String clientSecret = "";
			String authToken = "";
			String sek = "";

			Set<Object[]> headerDetails = eInvoiceRepo.getEwayHeaderDetails(irn);
			if (!headerDetails.isEmpty()) {
				Object[] firstRow = headerDetails.iterator().next(); // Get the first row

				userName = firstRow[0].toString();
				gstin = firstRow[1].toString();
				clientId = firstRow[2].toString();
				clientSecret = firstRow[3].toString();
				authToken = firstRow[4].toString();
				System.out.println("Auth Token :" + authToken);
				sek = firstRow[5].toString();
				System.out.println("SEK  :" + sek);

			}


			EwayBillResponseDTO ewayBillResponseDTO = new EwayBillResponseDTO();

			PayloadDTO payloadDTO = new PayloadDTO();

			Object eWayPaload = getEWayBillByDocIdnew(irn);

			// Convert object to JSON string
			ObjectMapper objectMapper = new ObjectMapper();
			String name = objectMapper.writeValueAsString(eWayPaload);
			String encryptedName = encryptBySymmetricKey1(name, sek);
			payloadDTO.setData(encryptedName);
			// Live API
			String url ="https://api.einvoice1.gst.gov.in/eiewb/v1.03/ewayapi";
			// SandBox API
//			String url = "https://einv1api.gstsandbox.nic.in/eiewb/v1.03/ewaybill";
			HttpHeaders headers = new HttpHeaders();
			headers.set("client_id", clientId);
			headers.set("client_secret", clientSecret);
			headers.set("gstin", gstin);
			headers.set("user_name", userName);
			headers.set("authtoken", authToken);
			System.out.println("TEST tOKEN :" + authToken);
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<PayloadDTO> request = new HttpEntity<>(payloadDTO, headers);
			RestTemplate restTemplate = new RestTemplate();
			try {
				ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

				System.out.println("Raw Response: " + response.getBody());
				EwayBillResponseVO ewayBillResponseVO = new EwayBillResponseVO();
				ewayBillResponseVO.setDocid(irn);
				ewayBillResponseVO.setResponse(response.getBody());
				ObjectMapper objectMapper5 = new ObjectMapper();
				Map<String, Object> mp1 = objectMapper5.readValue(response.getBody(),
						new TypeReference<Map<String, Object>>() {
						});
				if (mp1.get("Status").equals(0)) {
					ewayBillResponseVO.setIserror("Y");
					Object errorDetailsObj = mp1.get("ErrorDetails");
					if (errorDetailsObj instanceof List) {
						List<?> errorDetailsList = (List<?>) errorDetailsObj;
						if (!errorDetailsList.isEmpty() && errorDetailsList.get(0) instanceof Map) {
							Map<?, ?> firstError = (Map<?, ?>) errorDetailsList.get(0);
							Object errorCode = firstError.get("ErrorCode");
							Object errorMessage = firstError.get("ErrorMessage");
							if (errorCode != null) {
								ewayBillResponseVO.setMessage("ErrorCode: " + errorCode.toString());
								ewayBillResponseVO.setErrordetails(errorMessage.toString());
							}
						}
					}

				} else {
					ewayBillResponseVO.setIserror("N");
					ewayBillResponseVO.setMessage("E-Way Generated");
				}
				for (EInvoiceVO eInvoiceVO : eInvoiceVOs) {
					eInvoiceVO.setEapicall("T");
					updatedEInvoiceVOs.add(eInvoiceVO);
				}
				eInvoiceRepo.saveAll(updatedEInvoiceVOs);
				ewayBillResponseRepo.save(ewayBillResponseVO);
				// Convert JSON response to a Map
				ObjectMapper objectMapper1 = new ObjectMapper();
				Map<String, Object> mp = objectMapper1.readValue(response.getBody(),
						new TypeReference<Map<String, Object>>() {
						});

				// Map to InvoiceResponse object
				ewayBillResponseDTO
						.setStatus(mp.get("Status") != null ? Integer.parseInt(mp.get("Status").toString()) : 0);

				// Convert Data field if present
				if (mp.get("Data") != null) {
					String datas = mp.get("Data").toString();
					byte[] dt = datas.getBytes(StandardCharsets.UTF_8);
					ewayBillResponseDTO.setData(dt);
					if (ewayBillResponseDTO.getData() != null) {
						String decryptedText = decryptBySymmetricKey(datas, sek);
						ObjectMapper objectMapper3 = new ObjectMapper();
						Map<String, Object> decryptedMap = objectMapper3.readValue(decryptedText, Map.class);
						System.out.println("Decrypted Data " + decryptedMap);
						EwayResponseVO ewayResponseVO1 = new EwayResponseVO();
						ewayResponseVO1.setEwbdate(
								decryptedMap.get("EwbDt") != null ? decryptedMap.get("EwbDt").toString() : "");
						ewayResponseVO1.setEwbno(
								decryptedMap.get("EwbNo") != null ? decryptedMap.get("EwbNo").toString() : "");
						ewayResponseVO1.setEwvalidtill(
								decryptedMap.get("EwbValidTill") != null ? decryptedMap.get("EwbValidTill").toString()
										: "");
						ewayResponseVO1.setIrn(irn);

						for (EInvoiceVO eInvoiceVO1 : eInvoiceVOs) {
							eInvoiceVO1.setEwbno(
									decryptedMap.get("EwbNo") != null ? decryptedMap.get("EwbNo").toString() : "");
							eInvoiceVO1.setEwbdate(
									decryptedMap.get("EwbDt") != null ? decryptedMap.get("EwbDt").toString() : "");
							eInvoiceVO1.setEwbvalidtill(decryptedMap.get("EwbValidTill") != null
									? decryptedMap.get("EwbValidTill").toString()
									: "");
							ewayResponseVO1.setDocid(eInvoiceVO1.getDocid());
							eInvoiceVO1.setEwaystatus("T");
							updatedEInvoiceVOs.add(eInvoiceVO1);
							;
						}
						eInvoiceRepo.saveAll(updatedEInvoiceVOs);

						ewayResponseRepo.save(ewayResponseVO1);

					} else {
						for (EInvoiceVO eInvoiceVO : eInvoiceVOs) {
							eInvoiceVO.setEwaystatus("F");
							updatedEInvoiceVOs.add(eInvoiceVO);
						}
						eInvoiceRepo.saveAll(updatedEInvoiceVOs);
					}
				}
				message = "EwayBill Genaretd Successfully";
			} catch (Exception e) {
				e.printStackTrace();
				return null; // Handle errors properly based on your business logic
			}
		}
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return response;
	}

	public String encryptBySymmetricKey1(String textToEncrypt, String decryptedSek) {
		try {
			if (decryptedSek == null || decryptedSek.isEmpty()) {
				throw new IllegalArgumentException("Secret key (SEK) is empty or null");
			}

			byte[] sekByte = Base64.getDecoder().decode(decryptedSek);
			SecretKey aesKey = new SecretKeySpec(sekByte, "AES");

			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);

			byte[] encryptedBytes = cipher.doFinal(textToEncrypt.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return "Encryption error: " + e.getMessage();
		}
	}

	public String decryptBySymmetricKey1(String encryptedText, String decryptedSek) throws Exception {
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

	@Override
	public EwayBillDTO getEWayBillByDocIdnew(String docId) {
		EwayBillDTO ewayBillDTOs = new EwayBillDTO();
		String irn = docId;
		Set<Object[]> headerDetails = eInvoiceRepo.getEwayBillDetails(irn);

		if (headerDetails != null && !headerDetails.isEmpty()) {
			for (Object[] header : headerDetails) {
				EwayBillDTO ewayBillDTO = new EwayBillDTO();
				// Set EwayBillDTO fields with null checks
				ewayBillDTO.setIrn(header[0] != null ? header[0].toString() : null);
				ewayBillDTO.setDistance(header[1] != null ? Integer.parseInt(header[1].toString()) : 0); // Default to 0
																											// if null
				ewayBillDTO.setTransMode(header[2] != null ? header[2].toString() : null);
				ewayBillDTO.setTransId(header[3] != null ? header[3].toString() : null);
				ewayBillDTO.setTransName(header[4] != null ? header[4].toString() : null);
				ewayBillDTO.setTransDocNo(header[5] != null ? header[5].toString() : null);
//				String dateString = header[6].toString();
//				String formattedDate = formatDate(dateString);
//				ewayBillDTO.setTransDocDt(formattedDate);
				String dateString = header[6] != null ? header[6].toString().trim() : null;
				if (dateString == null || dateString.isEmpty()) {
					ewayBillDTO.setTransDocDt(null);
				} else {
					String formattedDate = formatDate(dateString);
					ewayBillDTO.setTransDocDt(formattedDate);
				}
				ewayBillDTO.setVehNo(header[7] != null ? header[7].toString() : null);
				ewayBillDTO.setVehType(header[8] != null ? header[8].toString() : null);

				// ExpShipDetailsDTO (Buyer Details)
				ExpShipDetailsDTO expShipDetailsDTO = new ExpShipDetailsDTO();
				expShipDetailsDTO.setAddr1(header[9] != null ? header[9].toString() : null);
				expShipDetailsDTO.setAddr2(header[10] != null ? header[10].toString() : null);
				expShipDetailsDTO.setLoc(header[11] != null ? header[11].toString() : null);
				expShipDetailsDTO.setPin(header[12] != null ? Integer.parseInt(header[12].toString()) : 0); // Default
																											// to 0 if
																											// null
				expShipDetailsDTO.setStcd(header[13] != null ? header[13].toString() : null);
				ewayBillDTO.setExpShipDetails(expShipDetailsDTO);

				// DispatchDetailsDTO (Seller Details)
				DispatchDetailsDTO dispatchDetailsDTO = new DispatchDetailsDTO();
				dispatchDetailsDTO.setNm(header[14] != null ? header[14].toString() : null);
				dispatchDetailsDTO.setAddr1(header[15] != null ? header[15].toString() : null);
				dispatchDetailsDTO.setAddr2(header[16] != null ? header[16].toString() : null);
				dispatchDetailsDTO.setLoc(header[17] != null ? header[17].toString() : null);
				dispatchDetailsDTO.setPin(header[18] != null ? Integer.parseInt(header[18].toString()) : 0); // Default
																												// to 0
																												// if
																												// null
				dispatchDetailsDTO.setStcd(header[19] != null ? header[19].toString() : null);
				ewayBillDTO.setDispatchDetails(dispatchDetailsDTO);
				ewayBillDTOs = ewayBillDTO;
			}
		}

		// Return the populated EwayBillDTO object
		return ewayBillDTOs;
	}

	@Value("${public.key.path}")
	private String publicKeyPath;

	private PublicKey publicKey;

	@Override
	public Map<String, Object> generateToken(List<EInvoiceGetToketDTO> eInvoiceGetToketDTO1) throws Exception {

		Map<String, Object> token = new HashMap<>();

		for (EInvoiceGetToketDTO eInvoiceGetToketDTO : eInvoiceGetToketDTO1) {

			// Convert the byte array to a Base64 string
			String appKey2 = "LAz2aeV0irbbTrjtl3uKAAXeVJig91kjbracM3DWfO8=";
			System.out.println("AppKey: " + appKey2);

			// Convert hex string to byte array
//        byte[] apk = hexStringToByteArray(hexString);
			publicKey = loadPublicKey(publicKeyPath);
			HeaderDetailsVO headerDetailsVO = headerDetailsRepo.findByUserName(eInvoiceGetToketDTO.getUserName());

			String appKey = appKey2;
			String gstin = headerDetailsVO.getGstin();
			String clientId = headerDetailsVO.getClientId();
			String clientSecret = headerDetailsVO.getClientSecret();
			GenerateTokenDTO generateTokenDTO = new GenerateTokenDTO();
			generateTokenDTO.setUserName(eInvoiceGetToketDTO.getUserName());
			generateTokenDTO.setPassword(eInvoiceGetToketDTO.getPassword());
			generateTokenDTO.setAppKey(appKey);
			generateTokenDTO.setForceRefreshAccessToken(true);
			ObjectMapper objectMapper = new ObjectMapper();
			String payload = objectMapper.writeValueAsString(generateTokenDTO);

			// Encode the payload into Base64
			String base64Payload = Base64.getEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8));

			// Encrypt the Base64 encoded payload using RSA (for sending the payload)
			String encryptedPayload = encryptWithRSA(base64Payload.getBytes(StandardCharsets.UTF_8), publicKey);
			System.out.println("Encrypted Payload " + encryptedPayload);
			PayloadDTO payloadDTO = new PayloadDTO();
			payloadDTO.setData(encryptedPayload);

			// SandBox API
//			String url = "https://einv1api.gstsandbox.nic.in/eivital/v1.04/auth";
			// Live API
			 String url = "https://api.einvoice1.gst.gov.in/eivital/v1.04/auth";
			HttpHeaders headers = new HttpHeaders();
			headers.set("client_id", clientId);
			headers.set("client_secret", clientSecret);
			headers.set("gstin", gstin);
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<PayloadDTO> request = new HttpEntity<>(payloadDTO, headers);
			RestTemplate restTemplate = new RestTemplate();
			try {
				ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

				System.out.println("Raw Response: " + response.getBody());

				JsonNode jsonNode = objectMapper.readTree(response.getBody());
				String encryptedSek = jsonNode.get("Data").get("Sek").asText();

				ObjectMapper objectMapper1 = new ObjectMapper();
				Map<String, Object> mp = objectMapper1.readValue(response.getBody(),
						new TypeReference<Map<String, Object>>() {
						});
				if (mp.get("Data") != null) {

					Map<String, Object> dataMap = (Map<String, Object>) mp.get("Data");

					String ClientId = (String) dataMap.get("ClientId");
					String UserName = (String) dataMap.get("UserName");
					String AuthToken = (String) dataMap.get("AuthToken");
					String Sek1 = (String) dataMap.get("Sek");
					System.out.println("Encrypted Sek: " + Sek1);

					String TokenExpiry = (String) dataMap.get("TokenExpiry");

					byte[] decodedBytes = Base64.getDecoder().decode(appKey);
					SecretKeySpec secretKey = new SecretKeySpec(decodedBytes, "AES");
//	            System.out.println("AES Key: " + bytesToHex(secretKey.getEncoded()));
					byte[] decryptedSekBytes = decryptWithAppKey(encryptedSek, appKey);

					// Convert the decrypted SEK byte array to a human-readable hex format
					String base64DecryptedSek = bytesToBase64(decryptedSekBytes);
					System.out.println("Decrypted SEK (Base64): " + base64DecryptedSek);
					headerDetailsVO.setSek(base64DecryptedSek);
					headerDetailsVO.setAuthtoken(AuthToken);
					headerDetailsVO.setTokenExpiry(TokenExpiry);
					headerDetailsRepo.save(headerDetailsVO);
					token.put("ClientId", ClientId);
					token.put("UserName", UserName);
					token.put("AuthToken", AuthToken);
					token.put("Sek", base64DecryptedSek);
					token.put("TokenExpiry", TokenExpiry);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		// Return the encrypted data and encrypted AES key
		Map<String, Object> response = new HashMap<>();
		response.put("TokenDetails", token);
		System.out.println("Decrypted Response: " + response);
		return response;
	}

	public static byte[] decryptWithAppKey(String encryptedSek, String appKey) throws Exception {
		// Decode the AppKey (Base64) and the encrypted SEK (Base64)
		byte[] appKeyBytes = Base64.getDecoder().decode(appKey);
		byte[] encryptedSekBytes = Base64.getDecoder().decode(encryptedSek);

		// Initialize the AES cipher for decryption with the AppKey
		SecretKeySpec secretKey = new SecretKeySpec(appKeyBytes, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // AES ECB mode with padding
		cipher.init(Cipher.DECRYPT_MODE, secretKey);

		// Decrypt the SEK and return the raw byte array
		return cipher.doFinal(encryptedSekBytes);
	}

	// Utility to convert byte array to Base64
	public static String bytesToBase64(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}

	// RSA Encryption for the AES key and data
	private static String encryptWithRSA(byte[] data, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedBytes = cipher.doFinal(data);
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	// Load RSA Public Key from File
	private static PublicKey loadPublicKey(String filePath) throws Exception {
		String key = new String(Files.readAllBytes(Paths.get(filePath)));
		key = key.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replaceAll("\\s",
				""); // Remove new lines and spaces
		byte[] decodedKey = Base64.getDecoder().decode(key);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(spec);
	}

	@Scheduled(fixedRate = 2000)
	public void processTokenAutomation() throws Exception {
		System.out.println("Running Token Automation service every 1 Sec...");
		// Replace with actual branchCode

		List<Object[]> getTokenDetails = headerDetailsRepo.getAutomationTokenDetails();
		if (getTokenDetails != null) {

			int length = getTokenDetails.size();
			System.out.println("Length of the list: " + length);
			// Extract docIds from the list
			List<EInvoiceGetToketDTO> docIds = new ArrayList<>();
			for (Object[] record : getTokenDetails) {
				if (record != null && record.length > 0) {
					EInvoiceGetToketDTO dto = new EInvoiceGetToketDTO();
					String userName = record[0].toString(); // Assuming docId is the first column
					HeaderDetailsVO details = headerDetailsRepo.findByUserName(userName);
					dto.setUserName(details.getUserName());
					dto.setPassword(details.getPwd());
					docIds.add(dto);
				}
			}
			// Call the service method with the collected docIds
			if (!docIds.isEmpty()) {

				generateToken(docIds);

			} else {
				System.out.println("No docIds found to process.");
			}
		} else {
			System.out.println("List is null.");
		}

	}
	
	@Scheduled(fixedRate = 2000)
	public void processEWayBill() throws JsonProcessingException {
		System.out.println("Running E-Way service every 1 Sec...");
		// Replace with actual branchCode

		List<Object[]> getPendingEwayDetails = eInvoiceRepo.getPendingEwayDetails();
		if (getPendingEwayDetails != null) {

			int length = getPendingEwayDetails.size();
			System.out.println("Length of the list: " + length);
			// Extract docIds from the list
			List<String> docIds = new ArrayList<>();
			for (Object[] record : getPendingEwayDetails) {
				if (record != null && record.length > 0) {
					String docId = record[0].toString(); // Assuming docId is the first column
					docIds.add(docId);
				}
			}
			// Call the service method with the collected docIds
			if (!docIds.isEmpty()) {
				System.out.println(" Process Success.");
				createEWayBill(docIds);

			} else {
				System.out.println("No docIds found to process.");
			}
		} else {
			System.out.println("List is null.");
		}

	}

	@Override
	public EwayBillNonIRNDTO generateEwayBillByNonIRN() {
		EwayBillNonIRNDTO ewayBillNonIRNDTO= new EwayBillNonIRNDTO();
		List<ItemListDTO>itemListDTO= new ArrayList<>();
		ItemListDTO itemListDTO2= new ItemListDTO();
		itemListDTO.add(itemListDTO2);
		ewayBillNonIRNDTO.setItemList(itemListDTO);
		return ewayBillNonIRNDTO;
	}
	
//	@Override
//	public Map<String, Object> createEWayBillNonIRN(EwayBillNonIRNDTO ewayBillNonIRNDTO) throws JsonProcessingException {
//		String message = null;
////		for (String irn : docId) {
//
////			List<EInvoiceVO> eInvoiceVOs = eInvoiceRepo.getIrnDetails(irn);
////			List<EInvoiceVO> updatedEInvoiceVOs = new ArrayList<>();
//
//			String userName = "";
//			String gstin = "";
//			String clientId = "";
//			String clientSecret = "";
//			String authToken = "";
//			String sek = "";
//
//			Set<Object[]> headerDetails = eInvoiceRepo.getEwayHeaderDetails(irn);
//			if (!headerDetails.isEmpty()) {
//				Object[] firstRow = headerDetails.iterator().next(); // Get the first row
//
//				userName = firstRow[0].toString();
//				gstin = firstRow[1].toString();
//				clientId = firstRow[2].toString();
//				clientSecret = firstRow[3].toString();
//				authToken = firstRow[4].toString();
//				System.out.println("Auth Token :" + authToken);
//				sek = firstRow[5].toString();
//				System.out.println("SEK  :" + sek);
//
//			}
//
//
//			EwayBillResponseDTO ewayBillResponseDTO = new EwayBillResponseDTO();
//
//			PayloadDTO payloadDTO = new PayloadDTO();
//
//			Object eWayPaload = generateEwayBillByNonIRN();
//
//			// Convert object to JSON string
//			ObjectMapper objectMapper = new ObjectMapper();
//			String name = objectMapper.writeValueAsString(eWayPaload);
//			String encryptedName = encryptBySymmetricKey1(name, sek);
//			payloadDTO.setData(encryptedName);
//			String url = "https://einv1api.gstsandbox.nic.in/eiewb/v1.03/ewaybill";
//			HttpHeaders headers = new HttpHeaders();
//			headers.set("client_id", clientId);
//			headers.set("client_secret", clientSecret);
//			headers.set("gstin", gstin);
//			headers.set("user_name", userName);
//			headers.set("authtoken", authToken);
//			System.out.println("TEST tOKEN :" + authToken);
//			headers.setContentType(MediaType.APPLICATION_JSON);
//
//			HttpEntity<PayloadDTO> request = new HttpEntity<>(payloadDTO, headers);
//			RestTemplate restTemplate = new RestTemplate();
//			try {
//				ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
//
//				System.out.println("Raw Response: " + response.getBody());
//				EwayBillResponseVO ewayBillResponseVO = new EwayBillResponseVO();
//				ewayBillResponseVO.setDocid(irn);
//				ewayBillResponseVO.setResponse(response.getBody());
//				ObjectMapper objectMapper5 = new ObjectMapper();
//				Map<String, Object> mp1 = objectMapper5.readValue(response.getBody(),
//						new TypeReference<Map<String, Object>>() {
//						});
//				if (mp1.get("Status").equals(0)) {
//					ewayBillResponseVO.setIserror("Y");
//					Object errorDetailsObj = mp1.get("ErrorDetails");
//					if (errorDetailsObj instanceof List) {
//						List<?> errorDetailsList = (List<?>) errorDetailsObj;
//						if (!errorDetailsList.isEmpty() && errorDetailsList.get(0) instanceof Map) {
//							Map<?, ?> firstError = (Map<?, ?>) errorDetailsList.get(0);
//							Object errorCode = firstError.get("ErrorCode");
//							Object errorMessage = firstError.get("ErrorMessage");
//							if (errorCode != null) {
//								ewayBillResponseVO.setMessage("ErrorCode: " + errorCode.toString());
//								ewayBillResponseVO.setErrordetails(errorMessage.toString());
//							}
//						}
//					}
//
//				} else {
//					ewayBillResponseVO.setIserror("N");
//					ewayBillResponseVO.setMessage("E-Way Generated");
//				}
//				for (EInvoiceVO eInvoiceVO : eInvoiceVOs) {
//					eInvoiceVO.setEapicall("T");
//					updatedEInvoiceVOs.add(eInvoiceVO);
//				}
//				eInvoiceRepo.saveAll(updatedEInvoiceVOs);
//				ewayBillResponseRepo.save(ewayBillResponseVO);
//				// Convert JSON response to a Map
//				ObjectMapper objectMapper1 = new ObjectMapper();
//				Map<String, Object> mp = objectMapper1.readValue(response.getBody(),
//						new TypeReference<Map<String, Object>>() {
//						});
//
//				// Map to InvoiceResponse object
//				ewayBillResponseDTO
//						.setStatus(mp.get("Status") != null ? Integer.parseInt(mp.get("Status").toString()) : 0);
//
//				// Convert Data field if present
//				if (mp.get("Data") != null) {
//					String datas = mp.get("Data").toString();
//					byte[] dt = datas.getBytes(StandardCharsets.UTF_8);
//					ewayBillResponseDTO.setData(dt);
//					if (ewayBillResponseDTO.getData() != null) {
//						String decryptedText = decryptBySymmetricKey(datas, sek);
//						ObjectMapper objectMapper3 = new ObjectMapper();
//						Map<String, Object> decryptedMap = objectMapper3.readValue(decryptedText, Map.class);
//						System.out.println("Decrypted Data " + decryptedMap);
//						EwayResponseVO ewayResponseVO1 = new EwayResponseVO();
//						ewayResponseVO1.setEwbdate(
//								decryptedMap.get("EwbDt") != null ? decryptedMap.get("EwbDt").toString() : "");
//						ewayResponseVO1.setEwbno(
//								decryptedMap.get("EwbNo") != null ? decryptedMap.get("EwbNo").toString() : "");
//						ewayResponseVO1.setEwvalidtill(
//								decryptedMap.get("EwbValidTill") != null ? decryptedMap.get("EwbValidTill").toString()
//										: "");
//						ewayResponseVO1.setIrn(irn);
//
//						for (EInvoiceVO eInvoiceVO1 : eInvoiceVOs) {
//							eInvoiceVO1.setEwbno(
//									decryptedMap.get("EwbNo") != null ? decryptedMap.get("EwbNo").toString() : "");
//							eInvoiceVO1.setEwbdate(
//									decryptedMap.get("EwbDt") != null ? decryptedMap.get("EwbDt").toString() : "");
//							eInvoiceVO1.setEwbvalidtill(decryptedMap.get("EwbValidTill") != null
//									? decryptedMap.get("EwbValidTill").toString()
//									: "");
//							ewayResponseVO1.setDocid(eInvoiceVO1.getDocid());
//							eInvoiceVO1.setEwaystatus("T");
//							updatedEInvoiceVOs.add(eInvoiceVO1);
//							;
//						}
//						eInvoiceRepo.saveAll(updatedEInvoiceVOs);
//
//						ewayResponseRepo.save(ewayResponseVO1);
//
//					} else {
//						for (EInvoiceVO eInvoiceVO : eInvoiceVOs) {
//							eInvoiceVO.setEwaystatus("F");
//							updatedEInvoiceVOs.add(eInvoiceVO);
//						}
//						eInvoiceRepo.saveAll(updatedEInvoiceVOs);
//					}
//				}
//				message = "EwayBill Genaretd Successfully";
//			} catch (Exception e) {
//				e.printStackTrace();
//				return null; // Handle errors properly based on your business logic
//			}
////		}
//		Map<String, Object> response = new HashMap<>();
//		response.put("message", message);
//		return response;
//	}
}
