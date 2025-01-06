package com.reitzel.invoiceApproval.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.reitzel.invoiceApproval.dto.BchDtlsDTO;
import com.reitzel.invoiceApproval.dto.BuyerDetailsDTO;
import com.reitzel.invoiceApproval.dto.DispatchDetailsDTO;
import com.reitzel.invoiceApproval.dto.DocumentDetailsDTO;
import com.reitzel.invoiceApproval.dto.EInvoiceDTO;
import com.reitzel.invoiceApproval.dto.EWayBillDetailsDTO;
import com.reitzel.invoiceApproval.dto.ExportDetailsDTO;
import com.reitzel.invoiceApproval.dto.ItemDTO;
import com.reitzel.invoiceApproval.dto.SelletDetailsDTO;
import com.reitzel.invoiceApproval.dto.ShippingDetailsDTO;
import com.reitzel.invoiceApproval.dto.TransactionDetailsDTO;
import com.reitzel.invoiceApproval.dto.ValueDetailsDTO;
import com.reitzel.invoiceApproval.entity.EInvoiceVO;
import com.reitzel.invoiceApproval.repo.EInvoiceRepo;

@Service
public class EInvoiceServiceImpl implements EInvoiceService {

	@Autowired
	EInvoiceRepo eInvoiceRepo;
	
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

	@Override
	public List<EInvoiceVO> getEInvoiceByDocId(String docId) {

		List<EInvoiceVO> invoiceVO = eInvoiceRepo.findBydocid(docId);
		return invoiceVO;
	}

	@Override
	public List<EInvoiceDTO> getEInvoicePayloadByDocId(List<String> docIds) {

		List<EInvoiceDTO> eInvoiceDTOList = new ArrayList<>();

		

		// Iterate through each docId in the set
		for (String docIdArray : docIds) {
			String docId = docIdArray;
			Object[] headerDetails = eInvoiceRepo.getHeaderDetails(docId);

			if (headerDetails.length > 0 && headerDetails[0] instanceof Object[]) {
				Object[] nestedArray = (Object[]) headerDetails[0];

				// Create and populate an EInvoiceDTO object
				EInvoiceDTO eInvoiceDTO = new EInvoiceDTO();

				// Populate TransactionDetailsDTO
				TransactionDetailsDTO transactionDetailsDTO = new TransactionDetailsDTO();
				transactionDetailsDTO.setSupTyp(nestedArray[2].toString());
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
				
				SelletDetailsDTO selletDetailsDTO= new SelletDetailsDTO();
				eInvoiceDTO.setSellerDtls(selletDetailsDTO);
				
				DispatchDetailsDTO dd= new DispatchDetailsDTO();
				eInvoiceDTO.setDispDtls(dd);

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
				eInvoiceDTO.setBuyerDtls(buyerDetailsDTO);
				
				ExportDetailsDTO expDtls = new ExportDetailsDTO();
				eInvoiceDTO.setExpDtls(expDtls);
				
				EWayBillDetailsDTO eWayBillDetailsDTO= new EWayBillDetailsDTO();
				eInvoiceDTO.setEwbDtls(eWayBillDetailsDTO);
				
				ShippingDetailsDTO sd= new ShippingDetailsDTO();
				eInvoiceDTO.setShipDtls(sd);
				

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
				List<EInvoiceVO> eInvoiceVO=eInvoiceRepo.getDocidDetails(docId);
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
					BchDtlsDTO bch= new BchDtlsDTO();
					itemDTO.setBchDtls(bch);
					itemList.add(itemDTO);
				}

				eInvoiceDTO.setItemList(itemList);

				// Add the populated EInvoiceDTO to the list
				eInvoiceDTOList.add(eInvoiceDTO);
			}
		}
		for(EInvoiceDTO eInvoiceDTO1:eInvoiceDTOList)
		{
			String url = "https://einv1api.gstsandbox.nic.in/eicore/v1.03/Invoice";
            HttpHeaders headers = new HttpHeaders();
            headers.set("client_id", clientId);
            headers.set("client_secret", clientSecret);
            headers.set("gstin", gstin);
            headers.set("user_name", userName);
            headers.set("authtoken", authToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<EInvoiceDTO> request = new HttpEntity<>(eInvoiceDTO1, headers);
            RestTemplate restTemplate = new RestTemplate();

            try {
                ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
                if (response.getStatusCode() == HttpStatus.OK) {
                    // Handle success
                    System.out.println("Response: " + response.getBody());
                } else {
                    // Handle error
                    System.out.println("Error: " + response.getStatusCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exceptions (timeout, connectivity issues, etc.)
            }
		}
		return eInvoiceDTOList;
	}

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
	
	
//	@Scheduled(cron = "0 */1 * * * ?") // Runs every 1 minute
//    public void processEInvoices() {
//        System.out.println("Running E-Invoice service every 1 minute...");
//         // Replace with actual branchCode
//        
//        List<Object[]>getPendingIrnDetails=eInvoiceRepo.getPendingIRNDetails();
//        if (getPendingIrnDetails != null) {
//        	
//            int length = getPendingIrnDetails.size();
//            System.out.println("Length of the list: " + length);
//         // Extract docIds from the list
//            List<String> docIds = new ArrayList<>();
//            for (Object[] record : getPendingIrnDetails) {
//                if (record != null && record.length > 0) {
//                    String docId = record[0].toString(); // Assuming docId is the first column
//                    docIds.add(docId);
//                }
//            }
//            // Call the service method with the collected docIds
//            if (!docIds.isEmpty()) {
//                List<EInvoiceDTO> result = getEInvoicePayloadByDocId(docIds);
//                System.out.println("Processed E-Invoice Payload: " + result);
//            } else {
//                System.out.println("No docIds found to process.");
//            }
//        } else {
//            System.out.println("List is null.");
//        }
//        
//        
//    }
	


}
