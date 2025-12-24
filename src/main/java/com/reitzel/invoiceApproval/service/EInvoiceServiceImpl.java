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
import com.reitzel.invoiceApproval.dto.CancelIRNDTO;
import com.reitzel.invoiceApproval.dto.DispatchDetailsDTO;
import com.reitzel.invoiceApproval.dto.DocumentDetailsDTO;
import com.reitzel.invoiceApproval.dto.EInvoiceDTO;
import com.reitzel.invoiceApproval.dto.EInvoiceGetToketDTO;
import com.reitzel.invoiceApproval.dto.EWayBillDetailsDTO;
import com.reitzel.invoiceApproval.dto.EwayBillDTO;
import com.reitzel.invoiceApproval.dto.EwayBillNonIRNDTO;
import com.reitzel.invoiceApproval.dto.EwayBillPayLoadDTO;
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
import com.reitzel.invoiceApproval.entity.EwayBillDirectVO;
import com.reitzel.invoiceApproval.entity.EwayBillResponseVO;
import com.reitzel.invoiceApproval.entity.EwayResponseVO;
import com.reitzel.invoiceApproval.entity.HeaderDetailsVO;
import com.reitzel.invoiceApproval.entity.IRNResponseVO;
import com.reitzel.invoiceApproval.entity.InvoiceResponseVO;
import com.reitzel.invoiceApproval.repo.EInvoiceRepo;
import com.reitzel.invoiceApproval.repo.EwayBillDirectRepo;
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
	EwayBillDirectRepo ewayBillDirectRepo;

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
				itemDTO.setQty(Double.parseDouble(item[4].toString()));
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
			String url = "https://einv1api.gstsandbox.nic.in/eicore/v1.03/Invoice";
			// Live API
//			 String url = "https://api.einvoice1.gst.gov.in/eicore/v1.03/Invoice";
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
			EwayBillDTO dto = getEWayBillByDocIdnew(irn);
			Object eWayPaload = getEWayBillByDocIdnew(irn);

			// Convert object to JSON string
			ObjectMapper objectMapper = new ObjectMapper();
			String name = objectMapper.writeValueAsString(eWayPaload);
			String encryptedName = encryptBySymmetricKey1(name, sek);
			payloadDTO.setData(encryptedName);

			// SandBox API
			String url = "https://einv1api.gstsandbox.nic.in/eiewb/v1.03/ewaybill";
			// Live API
//			String url ="https://api.einvoice1.gst.gov.in/eiewb/v1.03/ewaybill";
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
						if (decryptedMap != null) {
							EwayResponseVO ewayResponseVO1 = new EwayResponseVO();
							ewayResponseVO1.setEwbdate(
									decryptedMap.get("EwbDt") != null ? decryptedMap.get("EwbDt").toString() : "");
							ewayResponseVO1.setEwbno(
									decryptedMap.get("EwbNo") != null ? decryptedMap.get("EwbNo").toString() : "");
							ewayResponseVO1.setEwvalidtill(decryptedMap.get("EwbValidTill") != null
									? decryptedMap.get("EwbValidTill").toString()
									: "");
							ewayResponseVO1.setRemarks(decryptedMap.get("Remarks") != null
									? decryptedMap.get("Remarks").toString()
									: "");
							if (mp.get("InfoDtls") != null) {
				                List<Map<String, Object>> infoDtlsList = (List<Map<String, Object>>) mp.get("InfoDtls");
				                if (!infoDtlsList.isEmpty()) {
				                    Object desc = infoDtlsList.get(0).get("Desc");
				                    ewayResponseVO1.setAlert(desc != null ? desc.toString() : "");
				                }
							}
							ewayResponseVO1.setIrn(dto.getIrn());

							for (EInvoiceVO eInvoiceVO1 : eInvoiceVOs) {
								eInvoiceVO1.setEwbno(
										decryptedMap.get("EwbNo") != null ? decryptedMap.get("EwbNo").toString() : "");
								eInvoiceVO1.setEwbdate(
										decryptedMap.get("EwbDt") != null ? decryptedMap.get("EwbDt").toString() : "");
								eInvoiceVO1.setEwbvalidtill(decryptedMap.get("EwbValidTill") != null
										? decryptedMap.get("EwbValidTill").toString()
										: "");
								ewayResponseVO1.setDocid(eInvoiceVO1.getDocid());
								ewayResponseVO1.setType("IRN");
								eInvoiceVO1.setEwaystatus("T");
								updatedEInvoiceVOs.add(eInvoiceVO1);
								;
							}
							eInvoiceRepo.saveAll(updatedEInvoiceVOs);

							ewayResponseRepo.save(ewayResponseVO1);
						}

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

				String supType = eInvoiceRepo.getSupType(irn);
				if (!supType.equals("B2B")) {
					// ExpShipDetailsDTO (Buyer Details)
					ExpShipDetailsDTO expShipDetailsDTO = new ExpShipDetailsDTO();
					expShipDetailsDTO.setAddr1(header[9] != null ? header[9].toString() : null);
					expShipDetailsDTO.setAddr2(header[10] != null ? header[10].toString() : null);
					expShipDetailsDTO.setLoc(header[11] != null ? header[11].toString() : null);
					expShipDetailsDTO.setPin(header[12] != null ? Integer.parseInt(header[12].toString()) : 0); // Default
					expShipDetailsDTO.setStcd(header[13] != null ? header[13].toString() : null);
					ewayBillDTO.setExpShipDetails(expShipDetailsDTO);
					DispatchDetailsDTO dispatchDetailsDTO = new DispatchDetailsDTO();
					dispatchDetailsDTO.setNm(header[14] != null ? header[14].toString() : null);
					dispatchDetailsDTO.setAddr1(header[15] != null ? header[15].toString() : null);
					dispatchDetailsDTO.setAddr2(header[16] != null ? header[16].toString() : null);
					dispatchDetailsDTO.setLoc(header[17] != null ? header[17].toString() : null);
					dispatchDetailsDTO.setPin(header[18] != null ? Integer.parseInt(header[18].toString()) : 0); // Default
					dispatchDetailsDTO.setStcd(header[19] != null ? header[19].toString() : null);
					ewayBillDTO.setDispatchDetails(dispatchDetailsDTO);
				}

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
			String url = "https://einv1api.gstsandbox.nic.in/eivital/v1.04/auth";
			// Live API
//			 String url = "https://api.einvoice1.gst.gov.in/eivital/v1.04/auth";
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
	public EwayBillNonIRNDTO generateEwayBillByNonIRN(String docIds) {
		EwayBillNonIRNDTO ewayBillNonIRNDTO = new EwayBillNonIRNDTO();

		String docId = docIds;
		Object[] headerDetails1 = ewayBillResponseRepo.getHeaderDetails(docId);

		System.out.println("headerDetails Count: " + headerDetails1.length);

		if (headerDetails1 != null && headerDetails1.length > 0) {
			Object[] headerDetails = (Object[]) headerDetails1[0];
			System.out.println("SupplyType: " + headerDetails[0].toString());
			// Populate TransactionDetailsDTO
			ewayBillNonIRNDTO.setSupplyType(headerDetails[0].toString());
			ewayBillNonIRNDTO.setSubSupplyType(headerDetails[1].toString());
			ewayBillNonIRNDTO.setSubSupplyDesc(headerDetails[2].toString());
			ewayBillNonIRNDTO.setDocType(headerDetails[3].toString());
			ewayBillNonIRNDTO.setDocNo(headerDetails[4].toString());
			String dateString = headerDetails[5].toString();
			String formattedDate = formatDate(dateString);
			ewayBillNonIRNDTO.setDocDate(formattedDate);
			ewayBillNonIRNDTO.setFromGstin(headerDetails[6].toString());
			ewayBillNonIRNDTO.setFromTrdName(headerDetails[7].toString());
			ewayBillNonIRNDTO.setFromAddr1(headerDetails[8].toString());
			ewayBillNonIRNDTO.setFromAddr2(headerDetails[9].toString());
			ewayBillNonIRNDTO.setFromPlace(headerDetails[10].toString());
			ewayBillNonIRNDTO.setFromPincode(Integer.parseInt(headerDetails[11].toString()));
			ewayBillNonIRNDTO.setActFromStateCode(Integer.parseInt(headerDetails[12].toString()));
			ewayBillNonIRNDTO.setFromStateCode(Integer.parseInt(headerDetails[13].toString()));
			ewayBillNonIRNDTO.setToGstin(headerDetails[14].toString());
			ewayBillNonIRNDTO.setToTrdName(headerDetails[15].toString());
			ewayBillNonIRNDTO.setToAddr1(headerDetails[16].toString());
			ewayBillNonIRNDTO.setToAddr2(headerDetails[17].toString());
			ewayBillNonIRNDTO.setToPlace(headerDetails[18].toString());
			ewayBillNonIRNDTO.setToPincode(Integer.parseInt(headerDetails[19].toString()));
			ewayBillNonIRNDTO.setActToStateCode(Integer.parseInt(headerDetails[20].toString()));
			ewayBillNonIRNDTO.setToStateCode(Integer.parseInt(headerDetails[21].toString()));
			ewayBillNonIRNDTO.setTransactionType(Integer.parseInt(headerDetails[22].toString()));
			ewayBillNonIRNDTO.setOtherValue(headerDetails[23].toString());
			ewayBillNonIRNDTO.setCgstValue(Double.parseDouble(headerDetails[24].toString()));
			ewayBillNonIRNDTO.setSgstValue(Double.parseDouble(headerDetails[25].toString()));
			ewayBillNonIRNDTO.setIgstValue(Double.parseDouble(headerDetails[26].toString()));
			ewayBillNonIRNDTO.setCessValue(Double.parseDouble(headerDetails[27].toString()));
			ewayBillNonIRNDTO.setCessNonAdvolValue(Double.parseDouble(headerDetails[28].toString()));
			ewayBillNonIRNDTO.setTotInvValue(Double.parseDouble(headerDetails[29].toString()));
			ewayBillNonIRNDTO.setTransporterId(headerDetails[30] != null ? headerDetails[30].toString() : null);
			ewayBillNonIRNDTO.setTransporterName(headerDetails[31] != null ? headerDetails[31].toString() : null);
			ewayBillNonIRNDTO.setTransDocNo(headerDetails[32] != null ? headerDetails[32].toString() : null);
			ewayBillNonIRNDTO.setTransMode(headerDetails[33].toString());
			ewayBillNonIRNDTO.setTransDistance(headerDetails[34].toString());
			ewayBillNonIRNDTO.setTransDocDate(headerDetails[35] != null ? headerDetails[35].toString() : null);
			ewayBillNonIRNDTO.setVehicleNo(headerDetails[36].toString());
			ewayBillNonIRNDTO.setVehicleType(headerDetails[37].toString());

			// Fetch item details (assuming a list of items)
			List<Object[]> itemList = ewayBillResponseRepo.getItemListDetails(docId);
			List<ItemListDTO> itemListDTOs = new ArrayList<>();

			for (Object[] item : itemList) {
				ItemListDTO itemDTO = new ItemListDTO();
				itemDTO.setProductName(item[0].toString());
				itemDTO.setProductDesc(item[1].toString());
				itemDTO.setHsnCode(Long.parseLong(item[2].toString())); // Assuming HSN code is numeric
				itemDTO.setQuantity(Double.parseDouble(item[3].toString()));
				itemDTO.setQtyUnit(item[4].toString());
				itemDTO.setCgstRate(Double.parseDouble(item[5].toString()));
				itemDTO.setSgstRate(Double.parseDouble(item[6].toString()));
				itemDTO.setIgstRate(Double.parseDouble(item[7].toString()));
				itemDTO.setCessRate(Double.parseDouble(item[8].toString()));
				itemDTO.setCessNonadvol(Double.parseDouble(item[9].toString()));
				itemDTO.setTaxableAmount(Double.parseDouble(item[10].toString()));
				itemListDTOs.add(itemDTO);
			}
			ewayBillNonIRNDTO.setItemList(itemListDTOs); // Add the populated EInvoiceDTO to the list
		}

		return ewayBillNonIRNDTO;
	}

	@Override
	public Map<String, Object> createEWayBillNonIRN(List<String> docId) throws JsonProcessingException {
		String message = null;
		for (String docid : docId) {
			String gstin = "";
			String clientId = "";
			String clientSecret = "";
			String authToken = "";
			String sek = "";
			List<EwayBillDirectVO> ewayBillDirectVOs = ewayBillDirectRepo.getDocidDetails(docId);
			List<EwayBillDirectVO> updatedEwayBillDirectVOs = new ArrayList<>();
			Set<Object[]> headerDetails = ewayBillResponseRepo.getEwayHeaderDetails(docid);
			if (!headerDetails.isEmpty()) {
				Object[] firstRow = headerDetails.iterator().next(); // Get the first row
				gstin = firstRow[1].toString();
				clientId = firstRow[2].toString();
				clientSecret = firstRow[3].toString();
				authToken = firstRow[4].toString();
				sek = firstRow[5].toString();
			}

			EwayBillResponseDTO ewayBillResponseDTO = new EwayBillResponseDTO();
			EwayBillPayLoadDTO payloadDTO = new EwayBillPayLoadDTO();

//			EwayBillNonIRNDTO billNonIRNDTO = generateEwayBillByNonIRN(docid);

			Object eWayPaload = generateEwayBillByNonIRN(docid);

			// Convert object to JSON string
			ObjectMapper objectMapper = new ObjectMapper();
			String name = objectMapper.writeValueAsString(eWayPaload);
			String encryptedName = encryptBySymmetricKey1(name, sek);
			System.out.println("Encrypted Name: " + encryptedName);
			payloadDTO.setData(encryptedName);
			payloadDTO.setAction("GENEWAYBILL");
			// Sandbox
			String url = "https://ewb1api.gstsandbox.nic.in/ewaybillapi/v1.03/ewayapi";

			// Live
//		    String url = "https://api.ewaybillgst.gov.in/v1.03/ewayapi";
			HttpHeaders headers = new HttpHeaders();
			headers.set("client_id", clientId);
			headers.set("client_secret", clientSecret);
			headers.set("gstin", gstin);
			headers.set("authtoken", authToken);
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<EwayBillPayLoadDTO> request = new HttpEntity<>(payloadDTO, headers);
			RestTemplate restTemplate = new RestTemplate();
			try {
				ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

				
				System.out.println("Raw Response: " + response.getBody());
				for (EwayBillDirectVO eBillDirectVO : ewayBillDirectVOs) {
					eBillDirectVO.setEapicall("T");
					updatedEwayBillDirectVOs.add(eBillDirectVO); // ✅ Add to a separate list
				}
				ewayBillDirectRepo.saveAll(updatedEwayBillDirectVOs);
				EwayBillResponseVO ewayBillResponseVO = new EwayBillResponseVO();
				ewayBillResponseVO.setDocid(docid);
				ewayBillResponseVO.setResponse(response.getBody());
				ObjectMapper objectMapper12 = new ObjectMapper();
				Map<String, Object> mp12 = objectMapper12.readValue(response.getBody(),
						new TypeReference<Map<String, Object>>() {
						});

				String status = (String) mp12.get("status");
				if ("1".equals(status)) {
					String datas = mp12.get("data").toString();
					byte[] dt = datas.getBytes(StandardCharsets.UTF_8);
					ewayBillResponseDTO.setData(dt);
					if (ewayBillResponseDTO.getData() != null) {
						String decryptedText = decryptBySymmetricKey(datas, sek);
						ObjectMapper objectMapper3 = new ObjectMapper();
						Map<String, Object> decryptedMap = objectMapper3.readValue(decryptedText, Map.class);
						System.out.println("Decrypted Data " + decryptedMap);
						EwayResponseVO ewayResponseVO1 = new EwayResponseVO();
						ewayResponseVO1.setDocid(docid);
						ewayResponseVO1.setEwbdate(
								decryptedMap.get("ewayBillDate") != null ? decryptedMap.get("ewayBillDate").toString()
										: "");
						ewayResponseVO1.setEwbno(
								decryptedMap.get("ewayBillNo") != null ? decryptedMap.get("ewayBillNo").toString()
										: "");
						ewayResponseVO1.setEwvalidtill(
								decryptedMap.get("validUpto") != null ? decryptedMap.get("validUpto").toString() : "");
						ewayResponseVO1.setAlert(
								decryptedMap.get("alert") != null ? decryptedMap.get("alert").toString() : "");
						ewayResponseVO1.setType("Direct");
						ewayResponseRepo.save(ewayResponseVO1);
						ewayBillResponseVO.setDocid(docid);
						ewayBillResponseVO.setIserror("N");
						ewayBillResponseVO.setMessage("E-Way Generated");
						ewayBillResponseRepo.save(ewayBillResponseVO);
						
						for (EwayBillDirectVO eBillDirectVO : ewayBillDirectVOs) {
							eBillDirectVO.setEwbno(ewayResponseVO1.getEwbno());
							eBillDirectVO.setEwbdate(ewayResponseVO1.getEwbdate());
							eBillDirectVO.setEwbvalidtill(ewayResponseVO1.getEwvalidtill());
							eBillDirectVO.setEwaystatus("T");
							updatedEwayBillDirectVOs.add(eBillDirectVO); 
						}
						ewayBillDirectRepo.saveAll(updatedEwayBillDirectVOs);
					}
				} else {
					
					for (EwayBillDirectVO eBillDirectVO : ewayBillDirectVOs) {
						eBillDirectVO.setEwaystatus("F");
						updatedEwayBillDirectVOs.add(eBillDirectVO); // ✅ Add to a separate list
					}
					ewayBillDirectRepo.saveAll(updatedEwayBillDirectVOs);
					// Handle error response
					String encodedError = (String) mp12.get("error");
					if (encodedError != null) {
						byte[] decodedErrorBytes = Base64.getDecoder().decode(encodedError);
						String decodedError = new String(decodedErrorBytes, StandardCharsets.UTF_8);
						System.out.println("API Error: " + decodedError);
						ewayBillResponseVO.setIserror("Y");
						ewayBillResponseVO.setDocid(docid);
						ObjectMapper objectMapper25 = new ObjectMapper();
						JsonNode errorJson = objectMapper25.readTree(decodedError);
						String rawErrorCodes = errorJson.get("errorCodes").asText(); // "604,640,688,751,"
						ewayBillResponseVO.setMessage("ErrorCode: " + rawErrorCodes);
						// Remove the trailing comma (if any)
						String cleanedErrorCodes = rawErrorCodes.replaceAll(",$", "");
						ewayBillResponseRepo.save(ewayBillResponseVO);

					} else {
						System.out.println("Unknown error or unexpected response: " + response.getBody());
					}
				}
				message = "EwayBill Generated Successfully";

			} catch (Exception e) {
				e.printStackTrace();
				return null; // Handle errors properly based on your business logic
			}
		}
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return response;
	}

	@Scheduled(fixedRate = 2000)
	public void processEWayBillNonIRN() throws JsonProcessingException {
		System.out.println("Running E-Way service every 1 Sec...");
		// Replace with actual branchCode

		List<Object[]> getPendingEwayDetails = ewayBillResponseRepo.getPendingEwayNonIRNDetails();
		if (getPendingEwayDetails != null) {

			int length = getPendingEwayDetails.size();
			System.out.println("Length of the list: " + length);
			// Extract docIds from the list
			List<String> docIds = new ArrayList<>();
			for (Object[] record : getPendingEwayDetails) {
				if (record != null && record.length > 0) {
					String docId = record[0].toString(); 
					docIds.add(docId);
				}
			}
			// Call the service method with the collected docIds
			if (!docIds.isEmpty()) {
				System.out.println(" Process Success.");
				createEWayBillNonIRN(docIds);

			} else {
				System.out.println("No docIds found to process.");
			}
		} else {
			System.out.println("List is null.");
		}

	}

	@Override
	public CancelIRNDTO cancelIRN(String docIds) {
		CancelIRNDTO cancelIRNDTOs = new CancelIRNDTO();

		String docId = docIds;
		Set<Object[]> headerDetails1 = eInvoiceRepo.getIrnDetailsForCancel(docId);

		if (headerDetails1 != null && !headerDetails1.isEmpty()) {
			for (Object[] header : headerDetails1) {
				CancelIRNDTO cancelIRNDTO = new CancelIRNDTO();
				// Populate TransactionDetailsDTO
				cancelIRNDTO.setIrn(header[0] != null ? header[0].toString() : null);
				cancelIRNDTO.setCnlRsn("1");
				cancelIRNDTO.setCnlRem("Wrong Entry");
				cancelIRNDTOs = cancelIRNDTO;
			}
		}
		return cancelIRNDTOs;
	}
	
	@Override
	public Map<String, Object> cancelIRNInvoice(List<String> docIds) throws JsonProcessingException {

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

			PayloadDTO payloadDTO = new PayloadDTO();
			IRNResponseVO irnResponseVO = irnResponseRepo.findByDocid(docId);

			InvoiceResponseDTO invoiceResponseDTO = new InvoiceResponseDTO();
			

			Object eInvoicePayload = cancelIRN(docId);

			// Convert object to JSON string
			ObjectMapper objectMapper = new ObjectMapper();
			String name = objectMapper.writeValueAsString(eInvoicePayload);
			String encryptedName = encryptBySymmetricKey(name, sek);

			payloadDTO.setData(encryptedName);
			System.out.println(encryptedName);
			// SandBox API
			String url = "https://einv1api.gstsandbox.nic.in/eicore/v1.03/Invoice/Cancel";
			// Live API
//			 String url = "https://api.einvoice1.gst.gov.in/eicore/v1.03/Invoice";
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
						
//						irnResponseVO.setCancel("T");
//						irnResponseVO.setCanceldate(decryptedMap.get("CancelDate").toString());
						irnResponseRepo.save(irnResponseVO);

						for (EInvoiceVO eInvoiceVO : eInvoiceVOs) {
//							eInvoiceVO.setCanceldate(decryptedMap.get("CancelDate").toString());
							updatedEInvoiceVOs.add(eInvoiceVO); 
						}

						eInvoiceRepo.saveAll(updatedEInvoiceVOs);
					}
				} else {
					
				}
				message = "IRN Genaretd Successfully";
			} catch (Exception e) {
				e.printStackTrace();
				return null; 
			}
		}
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		return response;
	}
}
